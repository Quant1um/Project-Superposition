package net.quantium.projectsuperposition.tileentities;

import java.util.List;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.network.messages.ParticleMessage;
import net.quantium.projectsuperposition.protection.EnergyUtils;
import net.quantium.projectsuperposition.utilities.timers.CountTimer;

public class TileEntityImpulsiveSuctionModule extends TileEntity implements IInventory{
	
	public static final int RANGE = 20;
	public InventoryBasic inventory = new InventoryBasic("tileEntity_ism", true, 18);
	private CountTimer counter = new CountTimer(20);
	@Override
	public void updateEntity(){
		if(!counter.completed()) return;
		if(!EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, 24)) return;
		List<EntityItem> list = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord - RANGE + 0.5d, yCoord - RANGE + 0.5d, zCoord - RANGE + 0.5d, xCoord + RANGE + 0.5d, yCoord + RANGE + 0.5d, zCoord + RANGE + 0.5d));
		for(int i = 0; i < list.size(); i++){
			ItemStack item = list.get(i).getEntityItem();
			if(!list.get(i).isDead && list.get(i).age >= 1170){
				if(net.quantium.projectsuperposition.utilities.Utils.addToInv(inventory, item)) {
					list.get(i).setDead();
					PacketDispatcher.sendToAllAround(new ParticleMessage((float)list.get(i).posX, (float)list.get(i).posY, (float)list.get(i).posZ), new TargetPoint(worldObj.provider.dimensionId, list.get(i).posX, list.get(i).posY, list.get(i).posZ, 64.0f));
				}
			}else{
				if(worldObj.rand.nextInt(1171 - list.get(i).age) / 10 == 0) PacketDispatcher.sendToAllAround(new ParticleMessage((float)list.get(i).posX, (float)list.get(i).posY, (float)list.get(i).posZ), new TargetPoint(worldObj.provider.dimensionId, list.get(i).posX, list.get(i).posY, list.get(i).posZ, 64.0f));
			}
		}
	}
	@Override
	public int getSizeInventory() {
		return 18;
	}
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return inventory.getStackInSlot(p_70301_1_);
	}
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return inventory.decrStackSize(p_70298_1_, p_70298_2_);
	}
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return inventory.getStackInSlotOnClosing(p_70304_1_);
	}
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		inventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
	}
	@Override
	public String getInventoryName() {
		return inventory.getInventoryName();
	}
	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return p_70300_1_.getDistanceSq(xCoord + 0.5d, yCoord + 0.5d, zCoord + 0.5d) < 64;
	}
	@Override
	public void openInventory() {
		
	}
	@Override
	public void closeInventory() {
		
	}
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return inventory.isItemValidForSlot(p_94041_1_, p_94041_2_);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
	        NBTTagList tagList = par1NBTTagCompound.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
	        for (int i = 0; i < tagList.tagCount(); i++) 
	          {
	                NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
	                byte slot = tag.getByte("Slot");                                                 
	                if (slot >= 0 && slot < 18)   
	                  {
	                	this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tag)); 
	                  }                                                                                                
	          }
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);

		NBTTagList itemList = new NBTTagList();
	        for (int i = 0; i < 18; i++)                             
	          {
	                ItemStack stack = this.getStackInSlot(i);                                               
	                if (stack != null)                                                                      
	                  {
	                        NBTTagCompound tag = new NBTTagCompound();  
	                        tag.setByte("Slot", (byte) i);                   
	                        stack.writeToNBT(tag);  
	                        itemList.appendTag(tag);
	                  }
	          }
	        par1NBTTagCompound.setTag("Inventory", itemList);     
	}
}
