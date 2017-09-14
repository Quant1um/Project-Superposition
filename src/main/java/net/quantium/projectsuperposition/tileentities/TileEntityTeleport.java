package net.quantium.projectsuperposition.tileentities;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.client.IAlphaRendering;
import net.quantium.projectsuperposition.client.IListRendering;
import net.quantium.projectsuperposition.client.ListRenderer;
import net.quantium.projectsuperposition.client.RenderHelper;
import net.quantium.projectsuperposition.core.TeleporterLogic;
import net.quantium.projectsuperposition.core.tickdispatcher.Metadata;
import net.quantium.projectsuperposition.core.tickdispatcher.TaskRunnable;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher.TickType;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.debug.DebugEntry;
import net.quantium.projectsuperposition.debug.IDebuggable;
import net.quantium.projectsuperposition.protection.EnergyUtils;
import net.quantium.projectsuperposition.utilities.vector.VectorUtils;
import net.quantium.projectsuperposition.utilities.vector.WorldVector4;

public class TileEntityTeleport extends TileEntity implements IAlphaRendering, IListRendering, IDebuggable {
	public WorldVector4 connected = null;
	public int counter;
	public int state;
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		connected = new WorldVector4(VectorUtils.readFromNBT(tag.getCompoundTag("connected")));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		if(connected != null){
			NBTTagCompound tag2 = new NBTTagCompound();
			VectorUtils.writeToNBT(connected, tag2);
			tag.setTag("connected", tag2);
		}
	}
	
	@Override
    public Packet getDescriptionPacket() {
    	NBTTagCompound tagCompound = new NBTTagCompound();
    	tagCompound.setInteger("state", state);
    	return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tagCompound);
    }
    
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    	NBTTagCompound tag = pkt.func_148857_g();
    	state = tag.getInteger("state");
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox(){
		return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1.0F, yCoord + 3.0F, zCoord + 1.0F);
    }
    
    @Override
    public void updateEntity(){
    	if(worldObj.isRemote) return;
    	if(connected == null){
    		updateState(1);
    		return;
    	}
    	if(connected.getTileEntity() == null || !(connected.getTileEntity() instanceof TileEntityTeleport)){
    		updateState(1);
    		connected = null;
    		return;
    	}else if(isBlocked(connected)){
    		updateState(1);
    		return;
    	}
    		
    	if(!EnergyUtils.energyHandleExcept(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP, 2)){
    		updateState(2);
    		return;
    	}
    	List<Entity> list = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1.0F, yCoord + 3.0F, zCoord + 1.0F));
    	if(list.size() > 0){
    		counter++;
    		if(!EnergyUtils.energyHandleExcept(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP, 50)){
    			updateState(2);
        		counter = 0;
        		return;
        	}
    		updateState(3);
    		if(counter >= 65){
    			updateState(0);
    			counter = 0;
    			
    			for(int i = 0; i < list.size(); i++){
    				final Entity ee = list.get(i);
    				//System.out.println(connected.dim + ";" + worldObj.provider.dimensionId);
    				final double xOffset = list.get(i).posX - xCoord - 0.5f;
    				final double yOffset = list.get(i).posY - yCoord - 0.5f;
    				final double zOffset = list.get(i).posZ - zCoord - 0.5f;
    				
    				TickDispatcher.task(new TaskRunnable(){

						@Override
						public void run(Metadata meta) {
							TeleporterLogic.teleport(ee, connected.dimension(), 
								                     	 connected.x() + 0.5f + xOffset, 
								                     	 connected.y() + 0.5f + yOffset, 
								                     	 connected.z() + 0.5f + zOffset);
						}
    					
    				}, 2, TickType.SERVER);
    			}
    		}
    	}else{
    		updateState(0);
    		counter = 0;
    	}
    }
    
    private void updateState(int newState){
    	if(state != newState){
			state = newState;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			markDirty();
		}
    }
    
    public static boolean isBlocked(WorldVector4 v){
    	WorldVector4 bv = new WorldVector4(VectorUtils.add(v, VectorUtils.unit(3, 1)));
    	if(bv.getBlock().getMaterial() != Material.air) return true;
    	bv = new WorldVector4(VectorUtils.add(v, VectorUtils.unit(3, 1)));
    	if(bv.getBlock().getMaterial() != Material.air) return true;
    	return false;
    }

	@Override
	public void renderInSecondPass() {
    	ListRenderer.render(this);
	}
    
    public int getColor(){
    	float r = 1f, g = 1f, b = 1f;
    	switch(state){
  			case 0: r = 0.70f; g = 0.40f; b = 0.97f; break;
  			case 1: r = 0.97f; g = 0.40f; b = 0.70f; break;
  			case 2: r = 0.97f; g = 0.70f; b = 0.40f; break;
  			case 3: r = 0.40f; g = 0.97f; b = 0.70f; break;
    	}
    	int rgb = (int) (r * 255);
    	rgb = (rgb << 8) | (int)(g * 255);
    	rgb = (rgb << 8) | (int)(b * 255);
    	return rgb | 70 << 24;
    }

    private int lastState = -1;
    
	@Override
	public boolean redraw() {
		return lastState != state;
	}

	private int compiledList = -1;
	
	@Override
	public int list() {
		return compiledList;
	}

	@Override
	public void compile() {
		compiledList = GLAllocation.generateDisplayLists(1);
    	GL11.glNewList(compiledList, GL11.GL_COMPILE);
		RenderHelper.setDefaultTexture();
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    	GL11.glDisable(GL11.GL_LIGHTING);
    	OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 65535, 0);
		RenderHelper.start();
    	RenderHelper.color(getColor());
		RenderHelper.drawAxisAlignedCubeWithoutBottom(-0.49f, 0.365f, -0.49f, 0.49f, 2.475f, 0.49f);
		RenderHelper.end();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEndList();
		
	}

	@Override
	public void prepare() {
		
	}
	
	@Override
	public void validateList(){
		lastState = state;
	}
    
	@Override
	public void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag) {
		if(tag.hasKey("connected")){
			WorldVector4 vec = new WorldVector4(VectorUtils.readFromNBT(tag.getCompoundTag("connected")));
			list.add(new DebugEntry(StatCollector.translateToLocal("info.teleporter.dimension"), vec.dimension()));
			list.add(new DebugEntry(StatCollector.translateToLocal("info.teleporter.position.x"), vec.x()));
			list.add(new DebugEntry(StatCollector.translateToLocal("info.teleporter.position.y"), vec.y()));
			list.add(new DebugEntry(StatCollector.translateToLocal("info.teleporter.position.z"), vec.z()));
		}else
			list.add(new DebugEntry(null, StatCollector.translateToLocal("info.teleporter.noentanglement"), 0xff5555));
		
	}

	@Override
	public void capture(NBTTagCompound tag) {
		if(connected != null){
			NBTTagCompound tag2 = new NBTTagCompound();
			VectorUtils.writeToNBT(connected, tag2);
			tag.setTag("connected", tag2);
		}
	}
}
