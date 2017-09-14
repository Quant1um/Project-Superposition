package net.quantium.projectsuperposition.api.recipes;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IModRecipeRegistry<T extends IModRecipe> {
	
	public void register(T recipe);
	public ItemStack get(IInventory matrix);
	public T getRecipe(IInventory matrix);
	public List<T> recipes();
}
