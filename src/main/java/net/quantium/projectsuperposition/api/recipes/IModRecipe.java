package net.quantium.projectsuperposition.api.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IModRecipe {
	
	public ItemStack getCraftItem(IInventory matrix);
	public void onCraft(ItemStack item, IInventory matrix);
	public IInventory getDesiredMatrix();
}
