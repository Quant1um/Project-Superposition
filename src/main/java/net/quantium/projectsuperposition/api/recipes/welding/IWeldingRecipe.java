package net.quantium.projectsuperposition.api.recipes.welding;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.quantium.projectsuperposition.api.recipes.IModRecipe;

public interface IWeldingRecipe extends IModRecipe {
	
	public int getSolenoids();
	
	public int getWidth();
	public int getHeight();
}
