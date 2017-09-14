package net.quantium.projectsuperposition.api.recipes.soldering;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.quantium.projectsuperposition.api.recipes.IModRecipe;

public interface ISolderingRecipe extends IModRecipe{
	
	public int getSolenoids();
	public int getSilicon();
	
	public boolean usesBinarySlots();
}
