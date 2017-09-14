package net.quantium.projectsuperposition.debug;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.client.RenderHelper;
import net.quantium.projectsuperposition.client.RenderLastDispatcher;
import net.quantium.projectsuperposition.client.RenderTask;
import net.quantium.projectsuperposition.debug.network.CaptureRequestMessage;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.utilities.Utils;
import net.quantium.projectsuperposition.utilities.timers.DeltaTimer;
import net.quantium.projectsuperposition.utilities.timers.ElapsedTimer;

public class DebugDrawer {
	
	public float partialTicks;
	public RenderGlobal context;
	private List<DebugEntry> entries = new ArrayList<DebugEntry>();
	
	private DeltaTimer timer = new DeltaTimer();
	private ElapsedTimer requestTimer = new ElapsedTimer(700f);
	
	private float transitionState;
	
	private static final float PLACEHOLDER_DOTS = 3;
	private static final float PLACEHOLDER_DOT_TIME = 500f;
	private final ElapsedTimer placeholderDotTimer = new ElapsedTimer(PLACEHOLDER_DOTS * PLACEHOLDER_DOT_TIME);
	
	private DebugEntry createLoadingPlaceholder(){
		StringBuilder builder = new StringBuilder();
		builder.append(StatCollector.translateToLocal("info.loading.placeholder"));
		String dot = StatCollector.translateToLocal("info.loading.placeholder.dot");
		for(int i = 0; i < placeholderDotTimer.time() / PLACEHOLDER_DOT_TIME; i++) builder.append(dot);
		return new DebugEntry(null, builder.toString(), 0x999999);
	}
	
	@SubscribeEvent
	public void event(final DrawBlockHighlightEvent e){
		World w = e.player.worldObj;
		TileEntity t = w.getTileEntity(e.target.blockX, e.target.blockY, e.target.blockZ);
		
		float scale = 1f;
		float time = timer.cycle();
		placeholderDotTimer.update();
		
		boolean useActualInfo = false;
		if(e.target.typeOfHit != MovingObjectType.BLOCK || e.currentItem == null || e.currentItem.getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_SCREWDRIVER) || t == null || !(t instanceof IDebuggable)){
			scale = transitionState;
			if(transitionState > 0.05f) transitionState -= transitionState * 0.35f * time * 0.05f;
			else return;
		}else {
			if(transitionState < 0.999f){
				transitionState += 0.035f * time * 0.05f;
				scale = Utils.easeInElastic(transitionState);
			}else transitionState = 1f;
			
			if(CapturedInfoContainer.info.get(t) == null || requestTimer.completed()){
				PacketDispatcher.sendToServer(new CaptureRequestMessage(e.target.blockX, e.target.blockY, e.target.blockZ));
			}
			
			useActualInfo = true;
		}
		
		partialTicks = e.partialTicks;
		context = e.context;
		
		double playerX = Minecraft.getMinecraft().thePlayer.prevPosX + (Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX) * partialTicks;
		double playerY = Minecraft.getMinecraft().thePlayer.prevPosY + (Minecraft.getMinecraft().thePlayer.posY - Minecraft.getMinecraft().thePlayer.prevPosY) * partialTicks;
		double playerZ = Minecraft.getMinecraft().thePlayer.prevPosZ + (Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * partialTicks;
		
		GL11.glPushMatrix();
		GL11.glTranslated(e.target.blockX - playerX + 0.5f, e.target.blockY - playerY + 0.5f, e.target.blockZ - playerZ + 0.5f);
		GL11.glScaled(scale, scale, scale);
		if(useActualInfo){
			entries.clear();
			NBTTagCompound tag = CapturedInfoContainer.info.get(t);
			if(tag != null) ((IDebuggable)t).debug(entries, this, tag);
			else entries.add(createLoadingPlaceholder());
		}
		GL11.glPopMatrix();
		drawEntries(e.target.hitVec.xCoord - playerX, e.target.hitVec.yCoord - playerY, e.target.hitVec.zCoord - playerZ, scale);
		context = null;
	}

	private static final int MAX_ROWS = 8;
	private static final float PADDING = 1.2f, PADDING_VERTICAL = 1.6f;
	private static final float MARGIN = 2f;
	
	private void drawEntries(double x, double y, double z, double scale) {
		
		double f = Math.atan2(z, x) * 180f / Math.PI;
		GL11.glPushMatrix();
		GL11.glDepthFunc(GL11.GL_ALWAYS);
				
		GL11.glTranslated(x, y, z);
		
		scale *= 0.02f;
		GL11.glScaled(scale, scale, scale);
		GL11.glRotated(f + 90f, 0, -1, 0.1); //opengl should normalize vector by itself

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setBrightness(15728880);
		t.setColorRGBA(30, 30, 30, 255);
		int columns = entries.size() > 0 ? ((entries.size() - 1) / MAX_ROWS + 1) : 0;
		float offset = PADDING - MARGIN;
		float offsetY = PADDING_VERTICAL + fr.FONT_HEIGHT + MARGIN * 2;
		for(int i = 0; i < columns; i++){
			int rows = Math.min(MAX_ROWS, entries.size() - MAX_ROWS * i);
			float center = rows * 0.5f;
			float maxWidth = 0;
			for(int j = 0; j < rows; j++) maxWidth = Math.max(maxWidth, fr.getStringWidth(entries.get(i * MAX_ROWS + j).toString()));
			maxWidth += MARGIN * 2;
			for(int j = 0; j < rows; j++){
				float lOffsetY = ((float)j - center) * offsetY;
				t.addVertex(offset + maxWidth, lOffsetY, 0);
				t.addVertex(offset + maxWidth, lOffsetY + fr.FONT_HEIGHT + MARGIN * 2, 0);
				t.addVertex(offset, lOffsetY + fr.FONT_HEIGHT + MARGIN * 2, 0);
				t.addVertex(offset, lOffsetY, 0);				
			}
			offset += PADDING + maxWidth;
		}
		RenderHelper.setDefaultTexture();
		t.draw();
		drawEntriesData(fr, columns);
		
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glPopMatrix();
	}
	
	private void drawEntriesData(FontRenderer fr, int columns){
		GL11.glPushMatrix();
		GL11.glScaled(1, -1, 1);
		float offset = PADDING;
		float height = PADDING_VERTICAL + fr.FONT_HEIGHT + MARGIN * 2;
		for(int i = 0; i < columns; i++){
			int rows = Math.min(MAX_ROWS, entries.size() - MAX_ROWS * i);
			float center = rows * 0.5f;
			float maxWidth = 0;
			for(int j = 0; j < rows; j++){
				maxWidth = Math.max(maxWidth, fr.getStringWidth(entries.get(i * MAX_ROWS + j).toString()));
				float lOffsetY = ((float)j - center) * height + MARGIN + PADDING_VERTICAL;
				DebugEntry e = entries.get(i * MAX_ROWS + j);
				GL11.glPushMatrix();
				GL11.glTranslatef(offset, lOffsetY, 0);
				if(e.name != null) fr.drawString(e.name + ":", 0, 0, 0xefefef);
				fr.drawString(e.value == null ? StatCollector.translateToLocal("info.undefined") : e.value.toString(), e.name != null ? fr.getStringWidth(e.name + ": ") : 0, 0, e.color);
				GL11.glPopMatrix();		
			}
			offset += PADDING + maxWidth + MARGIN * 2;
		}
		
		GL11.glPopMatrix();
	}
}
