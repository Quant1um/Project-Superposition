package net.quantium.projectsuperposition.tileentities;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.quantium.projectsuperposition.client.IAlphaRendering;
import net.quantium.projectsuperposition.client.IListRendering;
import net.quantium.projectsuperposition.client.ListRenderer;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.debug.DebugEntry;
import net.quantium.projectsuperposition.debug.IDebuggable;

public class TileEntityHoloText extends TileEntity implements IListRendering, IAlphaRendering, IDebuggable{
	public String[] text = new String[16];
	public boolean redraw = false;
	public boolean rotation = false;
	
	private int compiledList = -1;
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		text = new String[16];
		for(int i = 0; i < 16; i++) text[i] = tag.getString("text" + i);
		rotation = tag.getBoolean("rotation");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		for(int i = 0; i < 16; i++) if(text[i] != null && !text[i].isEmpty()) tag.setString("text" + i, text[i]);
		tag.setBoolean("rotation", rotation);
	}
	
	@Override
    public Packet getDescriptionPacket() {
    	NBTTagCompound tagCompound = new NBTTagCompound();
    	for(int i = 0; i < 16; i++) if(text[i] != null && !text[i].isEmpty()) tagCompound.setString("text" + i, text[i]);
    	tagCompound.setBoolean("redraw", redraw);
    	tagCompound.setBoolean("rotation", rotation);
    	return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tagCompound);
    }
    
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    	NBTTagCompound tag = pkt.func_148857_g();
    	for(int i = 0; i < 16; i++) text[i] = tag.getString("text" + i);
    	redraw = tag.getBoolean("redraw");
    	rotation = tag.getBoolean("rotation");
    }
    
    @Override
    public void compile(){
    	compiledList = GLAllocation.generateDisplayLists(1);
    	GL11.glNewList(compiledList, GL11.GL_COMPILE);
		if(text != null){
			GL11.glRotatef(180f, 1f, 0, 0);
			GL11.glTranslated(0, -3.75f, 0);
			GL11.glScalef(0.015f, 0.015f, 0.015f);
			FontRenderer render = Minecraft.getMinecraft().fontRenderer;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 65535, 0);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			int v = 0;
			for(int i = text.length - 1; i >= 0; i--) 
				if(text[i] != null && !text[i].isEmpty()){
					if(text[i].length() > 32) text[i] = text[i].substring(0, 32);
					render.drawString(text[i],  -render.getStringWidth(text[i]) / 2, (render.FONT_HEIGHT + 2) * (text.length - 1 - v), 0xddffffff);
					GL11.glPushMatrix();
					GL11.glRotatef(180f, 0f, 1f, 0f);
					render.drawString(text[i],  -render.getStringWidth(text[i]) / 2, (render.FONT_HEIGHT + 2) * (text.length - 1 - v), 0xddffffff);
					GL11.glPopMatrix();	
					v++;
				}
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		
    	GL11.glEndList();
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox(){
		return AxisAlignedBB.getBoundingBox(xCoord - 1.5f, yCoord, zCoord - 1.5f, xCoord + 1.5f, yCoord + 4f, zCoord + 1.5f);
    }

	@Override
	public boolean redraw() {
		return redraw;
	}

	@Override
	public int list() {
		return compiledList;
	}
	
	@Override
	public void validateList(){
		redraw = false;
	}
	
	@Override
	public void prepare() {
		GL11.glRotatef(-22.5f * worldObj.getBlockMetadata(xCoord, yCoord, zCoord) - 135f, 0, 1, 0);
    	if(rotation) GL11.glRotatef((Minecraft.getSystemTime() & 8191) * 360f / 8192f, 0, 1, 0);
	}

	@Override
	public void renderInSecondPass() {
		ListRenderer.render(this);
	}
	
	@Override
	public void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag) {
		list.add(new DebugEntry(StatCollector.translateToLocal("info.text.orientation"), String.format("%.1f", -22.5f * worldObj.getBlockMetadata(xCoord, yCoord, zCoord) - 135f) + StatCollector.translateToLocal("info.degrees")));
		if(tag.getBoolean("rotation")) list.add(new DebugEntry(null, StatCollector.translateToLocal("info.text.rotating"), 0xffaa55));
	}

	@Override
	public void capture(NBTTagCompound tag) {
		tag.setBoolean("rotation", rotation);
	}
}
