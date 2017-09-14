package net.quantium.projectsuperposition.tileentities;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.api.recipes.soldering.ISolderingRecipe;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.debug.DebugEntry;
import net.quantium.projectsuperposition.debug.IDebuggable;
import net.quantium.projectsuperposition.items.IHeatPowerable;
import net.quantium.projectsuperposition.items.IPowerable;
import net.quantium.projectsuperposition.protection.EnergyUtils;

public class TileEntitySolderingStation extends TileEntity implements IInventory, IDebuggable{
	public InventoryBasic matrix = new InventoryBasic("matrix", true, 14);
	public int time;
	public boolean fading;
	public boolean mustCraft;

	@Override
	public int getSizeInventory() {
		return matrix.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return matrix.getStackInSlot(p_70301_1_);
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return matrix.decrStackSize(p_70298_1_, p_70298_2_);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return matrix.getStackInSlotOnClosing(p_70304_1_);
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		matrix.setInventorySlotContents(p_70299_1_, p_70299_2_);
	}

	@Override
	public String getInventoryName() {
		return matrix.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return matrix.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return matrix.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return p_70300_1_.getDistanceSq(xCoord + 0.5d, yCoord + 0.5d, zCoord + 0.5d) < 64;
	}

	@Override
	public void openInventory() {
		matrix.openInventory();
	}

	@Override
	public void closeInventory() {
		matrix.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		if(p_94041_1_ == 6 && p_94041_2_ != null && p_94041_2_.getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_SILICON)) return false;
		if(p_94041_1_ == 5 && p_94041_2_ != null && p_94041_2_.getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_SOLENOID)) return false;
		return matrix.isItemValidForSlot(p_94041_1_, p_94041_2_);
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
	                if (slot >= 0 && slot < this.getSizeInventory())   
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
	        for (int i = 0; i < this.getSizeInventory(); i++)                             
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
	
	@Override
	public void updateEntity(){
		if(worldObj.isRemote) return;
		if(current()){
			if(time >= 100)
				craft();
			else
				time += (110 - time) / 5;		
		}else{
			if(time > 0) 
				time -= (time + 10) / 5;
			else
				fading = false;
		}
		updateHeatable();
	}
	
	private void craft() {
		if(!current()) return;
		fading = true;
		mustCraft = false;
		ISolderingRecipe st = ModProvider.SOLDERING_RECIPES.getRecipe(matrix);
		ItemStack stack = st.getCraftItem(matrix).copy();
		st.onCraft(st.getCraftItem(matrix), matrix);
		if(this.getStackInSlot(13) == null || this.getStackInSlot(13).stackSize <= 0)
			this.setInventorySlotContents(13, stack);
		else
			this.getStackInSlot(13).stackSize += stack.stackSize;
	}

	public boolean needToCharge(){
		if(getHeatable() == null) return false;
		return getHeatableAsHeatPowerable().currentHeat(getHeatable()) < getHeatableAsHeatPowerable().capacityHeat(getHeatable()) ||
			   getHeatableAsPowerable().current(getHeatable()) < getHeatableAsPowerable().capacity(getHeatable());
	}
	
	public IPowerable getHeatableAsPowerable(){
		if(getHeatable() == null) return null;
		return (IPowerable) getHeatable().getItem();
	}
	
	public boolean current(){
		if(!mustCraft) return false;
		if(fading) return false;
		boolean flag = true;
		if(ModProvider.SOLDERING_RECIPES.getRecipe(matrix) == null) flag = false;
		ItemStack st = ModProvider.SOLDERING_RECIPES.get(matrix);
		if(st == null) flag = false;
		if(!isStackFree(this.getStackInSlot(13), st)) flag = false;
		if(getHeatable() == null || getHeatable().getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_SOLDERING_IRON) ||
				getHeatableAsHeatPowerable().currentHeat(getHeatable()) <= 0) mustCraft = false;
		if(!flag) mustCraft = false;
		return true;
	}
	
	public ItemStack getHeatable(){
		return this.getStackInSlot(12);
	}
	
	public IHeatPowerable getHeatableAsHeatPowerable(){
		if(getHeatable() == null) return null;
		return (IHeatPowerable) getHeatable().getItem();
	}
	
	public void updateHeatable(){
		if(getHeatable() == null || getHeatable().getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_SOLDERING_IRON)) return;
		if(mustCraft){
			if(getHeatableAsHeatPowerable().currentHeat(getHeatable()) <= 0) return;
			getHeatableAsHeatPowerable().receiveHeat(getHeatable(), -100);
		}else if(needToCharge() && EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, 5)){
			int e = 10;
			e = ((IPowerable) getHeatable().getItem()).receive(getHeatable(), e);
			getHeatableAsHeatPowerable().receiveHeat(getHeatable(), e * 3);
		}
	}
	
	public static boolean isStackFree(ItemStack slot, ItemStack add){
		if(slot == null) return true;
		if(add == null) return true;
		return slot.getItem() == add.getItem() && TileEntityWeldingStation.compareCompounds(add.stackTagCompound, slot.stackTagCompound) && slot.stackSize + add.stackSize <= slot.getMaxStackSize();
	}
	
	@Override
	public void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag) {
		list.add(new DebugEntry(StatCollector.translateToLocal("info.time"), String.format("%.1f", tag.getInteger("time") / 20f) + StatCollector.translateToLocal("info.seconds")));
		if(tag.getBoolean("creating")) list.add(new DebugEntry(null, StatCollector.translateToLocal("info.pendingcreate"), 0x55ff55));
	}

	@Override
	public void capture(NBTTagCompound tag) {
		tag.setInteger("time", time);
		tag.setBoolean("creating", mustCraft);
	}
}
