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
import net.quantium.projectsuperposition.api.recipes.welding.IWeldingRecipe;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.debug.DebugEntry;
import net.quantium.projectsuperposition.debug.IDebuggable;
import net.quantium.projectsuperposition.items.IHeatPowerable;
import net.quantium.projectsuperposition.items.IPowerable;
import net.quantium.projectsuperposition.protection.EnergyUtils;

public class TileEntityWeldingStation extends TileEntity implements IInventory, IDebuggable{
	public InventoryBasic matrix = new InventoryBasic("matrix", true, 18);
	public int time;
	public boolean fading;

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
	public boolean isItemValidForSlot(int slot, ItemStack is) {
		if(slot == 6 && is != null && is.getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_SILICON)) return false;
		if(slot == 5 && is != null && is.getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_SOLENOID)) return false;
		return matrix.isItemValidForSlot(slot, is);
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
			if(time >= 1000)
				craft();
			else
				time += (1030 - time) / 8;
		}else{
			if(time > 0) 
				time -= (time + 10) / 8;
			else
				fading = false;
		}
		updateHeatable();
	}
	
	private void craft() {
		if(!current()) return;
		fading = true;
		IWeldingRecipe st = ModProvider.WELDING_RECIPES.getRecipe(matrix);
		ItemStack stack = st.getCraftItem(matrix).copy();
		st.onCraft(st.getCraftItem(matrix), matrix);
		getHeatableAsHeatPowerable().receiveHeat(getHeatable(), -200);
		if(this.getStackInSlot(16) == null || this.getStackInSlot(16).stackSize <= 0)
			this.setInventorySlotContents(16, stack);
		else
			this.getStackInSlot(16).stackSize += stack.stackSize;
	}

	public boolean needToCharge(){
		if(getHeatable() == null) return false;
		return getHeatableAsHeatPowerable().currentHeat(getHeatable()) < getHeatableAsHeatPowerable().capacityHeat(getHeatable()) ||
			   getHeatableAsPowerable().current(getHeatable()) < getHeatableAsPowerable().capacity(getHeatable());
	}
	
	public boolean current(){
		if(fading) return false;
		IWeldingRecipe r = ModProvider.WELDING_RECIPES.getRecipe(matrix);
		if(r == null) return false;
		ItemStack st = r.getCraftItem(matrix);
		if(st == null) return false;
		if(getHeatable() == null || getHeatable().getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_WELDER) ||
				getHeatableAsHeatPowerable().currentHeat(getHeatable()) < 200) return false;
		if(!TileEntitySolderingStation.isStackFree(this.getStackInSlot(16), st)) return false;
		return true;
	}
	
	public ItemStack getHeatable(){
		return this.getStackInSlot(15);
	}
	
	public IHeatPowerable getHeatableAsHeatPowerable(){
		if(getHeatable() == null) return null;
		return (IHeatPowerable) getHeatable().getItem();
	}
	
	public IPowerable getHeatableAsPowerable(){
		if(getHeatable() == null) return null;
		return (IPowerable) getHeatable().getItem();
	}
	
	public void updateHeatable(){
		if(getHeatable() == null || getHeatable().getItem() != ModProvider.ITEMS.get(ObjectNames.ITEM_WELDER)) return;
			if(needToCharge() && EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, 5)){
			int e = 30;
			e = getHeatableAsPowerable().receive(getHeatable(), e);
			getHeatableAsHeatPowerable().receiveHeat(getHeatable(), e * 3);
		}
	}
	
	public static boolean compareCompounds(NBTTagCompound s1, NBTTagCompound s2) {
		return s1 == null ? (s2 == null ? true : false) : (s2 == null ? false : s1.equals(s2));
	}
	
	@Override
	public void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag) {
		list.add(new DebugEntry(StatCollector.translateToLocal("info.time"), String.format("%.1f", tag.getInteger("time") / 20f) + StatCollector.translateToLocal("info.seconds")));
		if(tag.getBoolean("creating")) list.add(new DebugEntry(null, StatCollector.translateToLocal("info.pendingcreate"), 0x55ff55));
	}

	@Override
	public void capture(NBTTagCompound tag) {
		tag.setInteger("time", time);
		tag.setBoolean("creating", current());
	}
}
