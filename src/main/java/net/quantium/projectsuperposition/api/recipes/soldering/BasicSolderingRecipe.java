package net.quantium.projectsuperposition.api.recipes.soldering;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.quantium.projectsuperposition.api.Bridge;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.api.Registry;

public class BasicSolderingRecipe implements ISolderingRecipe {

	public IInventory matrix;
	public ItemStack craft;
	public int silicon, solenoids;
	
	
	public BasicSolderingRecipe(ItemStack craft, IInventory matrix, int silicon, int solenoids){
		this.matrix = matrix;
		this.craft = craft;
		this.silicon = silicon;
		this.solenoids = solenoids;
	}
	
	
	@Override
	public ItemStack getCraftItem(IInventory matrix) {
		if(compareMatrices(matrix, this.matrix, silicon, solenoids)) return craft;
		return null;
	}

	public static boolean compareMatrices(IInventory haveMatrix, IInventory matrix, int silicon, int solenoids) {
		if(haveMatrix.getSizeInventory() < 7) return false;
		int haveSolenoids = haveMatrix.getStackInSlot(5) != null && haveMatrix.getStackInSlot(5).getItem() == ((Registry)Bridge.getAsObject("items")).get(ObjectNames.ITEM_SOLENOID) ? haveMatrix.getStackInSlot(5).stackSize : 0;
		int haveSilicon = haveMatrix.getStackInSlot(6) != null && haveMatrix.getStackInSlot(6).getItem() == ((Registry)Bridge.getAsObject("items")).get(ObjectNames.ITEM_SILICON) ? haveMatrix.getStackInSlot(6).stackSize : 0;
		if(haveSolenoids < solenoids) return false;
		if(haveSilicon < silicon) return false;
		
		if(!compareStacks(matrix.getStackInSlot(0), haveMatrix.getStackInSlot(0))) return false;
		if(!compareStacks(matrix.getStackInSlot(4), haveMatrix.getStackInSlot(4))) return false;
		if(!compareStacks(matrix.getStackInSlot(1), haveMatrix.getStackInSlot(1))) return false;
		if(!compareStacks(matrix.getStackInSlot(3), haveMatrix.getStackInSlot(3))) return false;
		if(!compareStacks(matrix.getStackInSlot(2), haveMatrix.getStackInSlot(2))) return false;
		
		return true;
	}

	public static boolean compareStacks(ItemStack stack1, ItemStack stack2){
		return stack1 == null ? (stack2 == null ? true : false) : (stack2 == null ? false : (stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage()));
	}
	
	public static void reduce(IInventory haveMatrix, IInventory matrix, int silicon, int solenoids){
		if(matrix.getStackInSlot(0) != null) haveMatrix.decrStackSize(0, matrix.getStackInSlot(0).stackSize);
		if(matrix.getStackInSlot(1) != null) haveMatrix.decrStackSize(1, matrix.getStackInSlot(1).stackSize);
		if(matrix.getStackInSlot(2) != null) haveMatrix.decrStackSize(2, matrix.getStackInSlot(2).stackSize);
		if(matrix.getStackInSlot(3) != null) haveMatrix.decrStackSize(3, matrix.getStackInSlot(3).stackSize);
		if(matrix.getStackInSlot(4) != null) haveMatrix.decrStackSize(4, matrix.getStackInSlot(4).stackSize);
		
		if(solenoids > 0) haveMatrix.decrStackSize(5, solenoids);
		if(silicon > 0) haveMatrix.decrStackSize(6, silicon);
	}
	
	public static BasicSolderingRecipe create(ItemStack out, int solenoids, int silicon, ItemStack i1, ItemStack i2, ItemStack i3, ItemStack i4, ItemStack i5){
		InventoryBasic inv = new InventoryBasic(null, false, 5);
		inv.setInventorySlotContents(0, i1);
		inv.setInventorySlotContents(1, i2);
		inv.setInventorySlotContents(2, i3);
		inv.setInventorySlotContents(3, i4);
		inv.setInventorySlotContents(4, i5);
		return new BasicSolderingRecipe(out, inv, silicon, solenoids);
	}
	
	@Override
	public void onCraft(ItemStack item, IInventory matrix) {
		reduce(matrix, this.matrix, silicon, solenoids);
	}

	@Override
	public IInventory getDesiredMatrix() {
		return this.matrix;
	}


	@Override
	public int getSolenoids() {
		return this.solenoids;
	}


	@Override
	public int getSilicon() {
		return this.silicon;
	}

	@Override
	public boolean usesBinarySlots() {
		return false;
	}
}
