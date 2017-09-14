package net.quantium.projectsuperposition.protection;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ProtectorPattern extends InventoryBasic{
	
	public ProtectorPattern() {
		super("protectionpattern", true, 9);
	}

	public boolean protectFrom(Entity e){
		boolean protect = false;
		for(int i = getSizeInventory() - 1; i >= 0; i--)
			if(getStackInSlot(i) != null && getStackInSlot(i).getItem() instanceof IProtectionCard && ((IProtectionCard)getStackInSlot(i).getItem()).protectFrom(e, getStackInSlot(i))) protect = true;
			else if(getStackInSlot(i) != null && getStackInSlot(i).getItem() instanceof IProtectionCard && ((IProtectionCard)getStackInSlot(i).getItem()).accessTo(e, getStackInSlot(i))) protect = false;
		return protect;
	}
	
	public void writeToNBT(NBTTagCompound tagcompound)
	{
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); ++i)
		{
			if (this.getStackInSlot(i) != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.getStackInSlot(i).writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		// We're storing our items in a custom tag list using our 'tagName' from
		// above
		// to prevent potential conflicts
		tagcompound.setTag(this.getInventoryName(), nbttaglist);
	}

	public void readFromNBT(NBTTagCompound compound) {
		// now you must include the NBTBase type ID when getting the list;
		// NBTTagCompound's ID is 10
		NBTTagList items = compound.getTagList(this.getInventoryName(), compound.getId());
		for (int i = 0; i < items.tagCount(); ++i) {
			// tagAt(int) has changed to getCompoundTagAt(int)
			NBTTagCompound item = items.getCompoundTagAt(i);
			byte slot = item.getByte("Slot");
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot,
						ItemStack.loadItemStackFromNBT(item));
			}
		}
	}
}
