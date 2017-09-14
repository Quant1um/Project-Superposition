package net.quantium.projectsuperposition.core.soldering;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.api.recipes.soldering.BasicSolderingRecipe;

public class IntegerCardRecipe extends BasicSolderingRecipe {

	public IntegerCardRecipe(boolean type, boolean inverse) {
		super(new ItemStack(ModProvider.ITEMS.get(ObjectNames.ITEM_SIZE_CARD)), null, 1, 1);
		InventoryBasic inv = new InventoryBasic(null, false, 5);
		if(type){
			inv.setInventorySlotContents(1, new ItemStack(Items.redstone));
			inv.setInventorySlotContents(2, new ItemStack(ModProvider.ITEMS.get(ObjectNames.ITEM_CHIP_A)));
			inv.setInventorySlotContents(3, new ItemStack(ModProvider.ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)));
			inv.setInventorySlotContents(4, new ItemStack(ModProvider.ITEMS.get(ObjectNames.ITEM_SOLENOID)));
		}else{
			inv.setInventorySlotContents(2, new ItemStack(ModProvider.ITEMS.get(ObjectNames.ITEM_SIZE_CARD)));
		}
		
		if(inverse) inv.setInventorySlotContents(0, new ItemStack(Items.gold_nugget));
		this.matrix = inv;
	}
	
	@Override
	public ItemStack getCraftItem(IInventory matrix) {
		if(compareMatrices(matrix, this.matrix, silicon, solenoids)){
			int i = getStackValue(0, matrix.getStackInSlot(11)) + 
					getStackValue(1, matrix.getStackInSlot(10)) + 
					getStackValue(2, matrix.getStackInSlot(9)) + 
					getStackValue(3, matrix.getStackInSlot(8)) + 
					getStackValue(4, matrix.getStackInSlot(7));
			if(getStackValue(0, matrix.getStackInSlot(0)) > 0) i = -i;
			ItemStack c = craft.copy();
			c.stackTagCompound = new NBTTagCompound();
			c.stackTagCompound.setInteger("value", i);
			return c;
		}
		return null;
	}
	
	public static int getStackValue(int id, ItemStack stack){
		int v = 1 << id;
		if(stack != null && stack.stackSize >= 1 && stack.getItem() == Items.gold_nugget) return v;
		return 0;
	}
	
	@Override
	public void onCraft(ItemStack item, IInventory matrix) {
		reduce(matrix, this.matrix, silicon, solenoids);
		matrix.decrStackSize(7, 1);
		matrix.decrStackSize(8, 1);
		matrix.decrStackSize(9, 1);
		matrix.decrStackSize(10, 1);
		matrix.decrStackSize(11, 1);
	}
	
	@Override
	public boolean usesBinarySlots(){
		return true;
	}
}
