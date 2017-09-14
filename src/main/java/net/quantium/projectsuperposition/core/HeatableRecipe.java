package net.quantium.projectsuperposition.core;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.items.IHeatPowerable;
import net.quantium.projectsuperposition.items.ItemHeatableEnergyTool;

public class HeatableRecipe implements IRecipe{

	@Override
	public boolean matches(InventoryCrafting matrix, World world) {
		boolean toolFound = false, fuelFound = false;
		for(int i = 0; i < matrix.getSizeInventory(); i++)
			if(!toolFound && matrix.getStackInSlot(i) != null && matrix.getStackInSlot(i).getItem() instanceof IHeatPowerable) toolFound = true;
			else if(!fuelFound && matrix.getStackInSlot(i) != null && TileEntityFurnace.isItemFuel(matrix.getStackInSlot(i))) fuelFound = true;
		return toolFound && fuelFound;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting matrix) {
		ItemStack ii = null;
		int fuelValue = 0;
		for(int i = 0; i < matrix.getSizeInventory(); i++)
			if(ii == null && matrix.getStackInSlot(i) != null && matrix.getStackInSlot(i).getItem() instanceof IHeatPowerable) ii = matrix.getStackInSlot(i).copy();
			else if(matrix.getStackInSlot(i) != null && TileEntityFurnace.isItemFuel(matrix.getStackInSlot(i))) fuelValue += TileEntityFurnace.getItemBurnTime(matrix.getStackInSlot(i));
		if(ii != null && fuelValue > 0){
			((IHeatPowerable)ii.getItem()).receiveHeat(ii, (int)(fuelValue * 0.3f));
			return ii;
		}
		return null;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
