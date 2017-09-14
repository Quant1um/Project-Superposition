package net.quantium.projectsuperposition.protection.field;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.client.IAlphaRendering;
import net.quantium.projectsuperposition.client.IListRendering;
import net.quantium.projectsuperposition.client.ListRenderer;
import net.quantium.projectsuperposition.client.RenderHelper;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.debug.DebugEntry;
import net.quantium.projectsuperposition.debug.IDebuggable;
import net.quantium.projectsuperposition.items.ItemIntegerCard;
import net.quantium.projectsuperposition.protection.EnergyUtils;
import net.quantium.projectsuperposition.protection.IProtector;
import net.quantium.projectsuperposition.protection.LocatorState;
import net.quantium.projectsuperposition.protection.ProtectorPattern;
import net.quantium.projectsuperposition.protection.field.world.WorldFieldContainer;
import net.quantium.projectsuperposition.utilities.Utils;
import net.quantium.projectsuperposition.utilities.vector.Vector3;

public class TileEntityFieldGenerator extends TileEntity implements IProtector, IAlphaRendering, IListRendering, IDebuggable{
	
	public InventoryBasic size = new InventoryBasic("sizeSettingInventory", true, 6);
	public ProtectorPattern pattern = new ProtectorPattern();
	public String owner = "";
	public int counter = 0, counterUpdate = 0;
	
	public int clientSizeX, clientSizeY, clientSizeZ, clientOffsetX, clientOffsetY, clientOffsetZ;
	public boolean clientEnabled;
	
	@Override
	public ProtectorPattern pattern() {
		return pattern;
	}
	@Override
	public String owner() {
		return owner;
	}
	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}
	@Override
	public boolean enabled() {
		return !worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && getSizeX() != 0 && getSizeY() != 0 && getSizeZ() != 0;
	}
	
	public int getSizeX(){
		if(size == null) return 0;
		if(size.getStackInSlot(0) == null) return 0;
		if(!(size.getStackInSlot(0).getItem() instanceof ItemIntegerCard)) return 0;
		if(size.getStackInSlot(0).getTagCompound() == null) return 0;
		int v = Math.abs(size.getStackInSlot(0).getTagCompound().getInteger("value"));
		if(v > 32) v = 32;
		return v;
	}
	
	public int getSizeY(){
		if(size == null) return 0;
		if(size.getStackInSlot(1) == null) return 0;
		if(!(size.getStackInSlot(1).getItem() instanceof ItemIntegerCard)) return 0;
		if(size.getStackInSlot(1).getTagCompound() == null) return 0;
		int v = Math.abs(size.getStackInSlot(1).getTagCompound().getInteger("value"));
		if(v > 32) v = 32;
		return v;	
	}
	
	public int getSizeZ(){
		if(size == null) return 0;
		if(size.getStackInSlot(2) == null) return 0;
		if(!(size.getStackInSlot(2).getItem() instanceof ItemIntegerCard)) return 0;
		if(size.getStackInSlot(2).getTagCompound() == null) return 0;
		int v = Math.abs(size.getStackInSlot(2).getTagCompound().getInteger("value"));
		if(v > 32) v = 32;
		return v;
	}
	
	public int getOffsetX(){
		if(size == null) return 0;
		if(size.getStackInSlot(3) == null) return 0;
		if(!(size.getStackInSlot(3).getItem() instanceof ItemIntegerCard)) return 0;
		if(size.getStackInSlot(3).getTagCompound() == null) return 0;
		int v = size.getStackInSlot(3).getTagCompound().getInteger("value");
		int i = getSizeX();
		if(v > i) v = i;
		return v;
	}
	
	public int getOffsetY(){
		if(size == null) return 0;
		if(size.getStackInSlot(4) == null) return 0;
		if(!(size.getStackInSlot(4).getItem() instanceof ItemIntegerCard)) return 0;
		if(size.getStackInSlot(4).getTagCompound() == null) return 0;
		int v = size.getStackInSlot(4).getTagCompound().getInteger("value");
		int i = getSizeY();
		if(v > i) v = i;
		return v;
	}
	
	public int getOffsetZ(){
		if(size == null) return 0;
		if(size.getStackInSlot(5) == null) return 0;
		if(!(size.getStackInSlot(5).getItem() instanceof ItemIntegerCard)) return 0;
		if(size.getStackInSlot(5).getTagCompound() == null) return 0;
		int v = size.getStackInSlot(5).getTagCompound().getInteger("value");
		int i = getSizeZ();
		if(v > i) v = i;
		return v;
	}

	public boolean preventFromBlockDestroy(){
		if(pattern == null) return false;
		for(int i = 0; i < pattern.getSizeInventory(); i++)
			if(pattern.getStackInSlot(i) != null && pattern.getStackInSlot(i).getItem() instanceof ItemFieldCard && pattern.getStackInSlot(i).getTagCompound() != null){
				if(pattern.getStackInSlot(i).getTagCompound().getBoolean("destroy")) return true;
			}
		return false;
	}
	
	public boolean preventFromInteraction(){
		if(pattern == null) return false;
		for(int i = 0; i < pattern.getSizeInventory(); i++)
			if(pattern.getStackInSlot(i) != null && pattern.getStackInSlot(i).getItem() instanceof ItemFieldCard && pattern.getStackInSlot(i).getTagCompound() != null){
				if(pattern.getStackInSlot(i).getTagCompound().getBoolean("interact")) return true;
			}
		return false;
	}
	
	public boolean preventFromPlacing(){
		if(pattern == null) return false;
		for(int i = 0; i < pattern.getSizeInventory(); i++)
			if(pattern.getStackInSlot(i) != null && pattern.getStackInSlot(i).getItem() instanceof ItemFieldCard && pattern.getStackInSlot(i).getTagCompound() != null){
				if(pattern.getStackInSlot(i).getTagCompound().getBoolean("place")) return true;
			}
		return false;
	}
	
	public boolean preventFromEntering(){
		if(pattern == null) return false;
		for(int i = 0; i < pattern.getSizeInventory(); i++)
			if(pattern.getStackInSlot(i) != null && pattern.getStackInSlot(i).getItem() instanceof ItemFieldCard && pattern.getStackInSlot(i).getTagCompound() != null){
				if(pattern.getStackInSlot(i).getTagCompound().getBoolean("enter")) return true;
			}
		return false;
	}
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if(pattern == null) pattern = new ProtectorPattern();
		pattern.writeToNBT(tag);
		tag.setString("owneruuid", owner);
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < 6; i++)                             
        {
        	ItemStack stack = size.getStackInSlot(i);                                               
            if (stack != null)                                                                      
            {
            	NBTTagCompound tag2 = new NBTTagCompound();  
                tag2.setByte("Slot", (byte) i);                   
                stack.writeToNBT(tag2);  
                itemList.appendTag(tag2);
            }
         }
       tag.setTag("Inventory", itemList);     
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if(pattern == null) pattern = new ProtectorPattern();
		pattern.readFromNBT(tag);
		owner = tag.getString("owneruuid");
		NBTTagList tagList = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
	    for (int i = 0; i < tagList.tagCount(); i++) 
	    {
	    	NBTTagCompound tag2 = (NBTTagCompound) tagList.getCompoundTagAt(i);
	        byte slot = tag2.getByte("Slot");                                                 
	        if (slot >= 0 && slot < 6)   
	        {
	        	size.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tag2)); 
	        }                                                                                                
	    }
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		return INFINITE_EXTENT_AABB;
	}
	
	private boolean haveEnergy = false;
	
	@Override
	public void updateEntity(){
		if(worldObj.isRemote) return;
		if(counterUpdate > 20) counterUpdate = 0;
		if(counterUpdate++ == 0){
			WorldFieldContainer.get(worldObj).addIfNotExistsAndUpdate(this);
		}
		if(counter > 2) counter = 0;
		if(counter++ != 0) return;
		if(!enabled()) return;
		haveEnergy = EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, (int) Math.min(getSizeX() * getSizeY() * getSizeZ() * 0.001f, 1));
		if(!haveEnergy) return;
		if(!this.preventFromEntering()) return;
		//System.out.println(getOffsetX());//(int) (getSizeX() * getSizeY() * getSizeZ() * 8 * 0.02f));
		List<EntityLivingBase> list = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord - getSizeX() + getOffsetX(), 
																						  								  yCoord - getSizeY() + getOffsetY(), 
																						  								  zCoord - getSizeZ() + getOffsetZ(), 
																						  								  xCoord + getSizeX() + getOffsetX() + 1, 
																						  								  yCoord + getSizeY() + getOffsetY() + 1, 
															  								  zCoord + getSizeZ() + getOffsetZ() + 1));
		for(int i = 0; i < list.size(); i++){
			if(pattern.protectFrom(list.get(i)))
				if(!(list.get(i) instanceof EntityPlayer && ((EntityPlayer)list.get(i)).getCommandSenderName().equals(owner)))
					if(EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, 30))
						applyTo(list.get(i));
			
		}
	}
	
	private void applyTo(EntityLivingBase e) {
		if(worldObj.isRemote) return;
		double speedfactor = 0.8;
		e.motionY = Math.min((e.posY - yCoord - 0.5f) / 5f, 1) * speedfactor;
		if(e.motionY < 0.2f && e.motionY >= 0) e.motionY = 0.2f;
		if(e.motionY > -0.2f && e.motionY < 0) e.motionY = -0.2f;
		e.motionX = Math.min((e.posX - xCoord - 0.5f) / 5f, 1) * speedfactor / Math.abs(e.motionY * 5);
		e.motionZ = Math.min((e.posZ - zCoord - 0.5f) / 5f, 1) * speedfactor / Math.abs(e.motionY * 5);
		if(e instanceof EntityPlayerMP) ((EntityPlayerMP) e).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(e));
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
	 	tagCompound.setInteger("sizeX", getSizeX());
	    tagCompound.setInteger("sizeY", getSizeY());
	    tagCompound.setInteger("sizeZ", getSizeZ());
	    tagCompound.setInteger("offsetX", getOffsetX());
	    tagCompound.setInteger("offsetY", getOffsetY());
	    tagCompound.setInteger("offsetZ", getOffsetZ());
	    tagCompound.setBoolean("enabled", enabled());
	    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tagCompound);
	}
	    
	private boolean redraw = false;    
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
	    NBTTagCompound tag = pkt.func_148857_g();
	    clientSizeX = tag.getInteger("sizeX");
	    clientSizeY = tag.getInteger("sizeY");
	    clientSizeZ = tag.getInteger("sizeZ");
	    clientOffsetX = tag.getInteger("offsetX");
	    clientOffsetY = tag.getInteger("offsetY");
	    clientOffsetZ = tag.getInteger("offsetZ");
	    clientEnabled = tag.getBoolean("enabled");
	    redraw = true;
	}
	
	@Override
	public void renderInSecondPass() {
	    ListRenderer.render(this);
	}
	      
    @Override
	public boolean redraw() {
		return redraw;
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
		if(clientEnabled){
			RenderHelper.setDefaultTexture();
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_LIGHTING);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 65535, 0);
			RenderHelper.start();
			RenderHelper.color((179 << 16) | (102 << 8) | 247 | (70 << 24));
			RenderHelper.drawAxisAlignedCube(-clientSizeX + clientOffsetX - 0.47f, 
										     -clientSizeY + clientOffsetY - 0.47f, 
										     -clientSizeZ + clientOffsetZ - 0.47f,
										     clientSizeX + clientOffsetX + 0.47f, 
										     clientSizeY + clientOffsetY + 0.47f, 
										     clientSizeZ + clientOffsetZ + 0.47f);
			RenderHelper.end();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		GL11.glEndList();	
	}

	@Override
	public void prepare() {
		
	}
	
	@Override
	public void validateList(){
		redraw = false;
	}
	
	@Override
	public void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag) {
		list.add(new DebugEntry(StatCollector.translateToLocal("info.field.length"), clientSizeX));
		list.add(new DebugEntry(StatCollector.translateToLocal("info.field.width"), clientSizeZ));
		list.add(new DebugEntry(StatCollector.translateToLocal("info.field.height"), clientSizeY));
		
		list.add(new DebugEntry(StatCollector.translateToLocal("info.field.offset.x"), clientOffsetX));
		list.add(new DebugEntry(StatCollector.translateToLocal("info.field.offset.y"), clientOffsetY));
		list.add(new DebugEntry(StatCollector.translateToLocal("info.field.offset.z"), clientOffsetZ));
		
		if(!clientEnabled) list.add(new DebugEntry(null, StatCollector.translateToLocal("info.disabled"), 0xff5555));
	}

	@Override
	public void capture(NBTTagCompound tag) {}
}
