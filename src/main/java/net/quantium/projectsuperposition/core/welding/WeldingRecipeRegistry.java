package net.quantium.projectsuperposition.core.welding;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.quantium.projectsuperposition.api.recipes.IModRecipeRegistry;
import net.quantium.projectsuperposition.api.recipes.welding.IWeldingRecipe;

public class WeldingRecipeRegistry implements IModRecipeRegistry<IWeldingRecipe>{
	private List<IWeldingRecipe> recipes = new ArrayList<IWeldingRecipe>();
	
	public ItemStack get(IInventory matrix){
		IWeldingRecipe r = getRecipe(matrix);
		if(r == null) return null;
		return r.getCraftItem(matrix);
	}
	
	public void register(IWeldingRecipe recipe){
		if(recipe == null) return;
		recipes.add(recipe);
	}

	public IWeldingRecipe getRecipe(IInventory matrix) {
		if(matrix == null) return null;
		for(int i = 0; i < recipes.size(); i++) if (recipes.get(i).getCraftItem(matrix) != null) return recipes.get(i);
		return null;
	}

	@Override
	public List<IWeldingRecipe> recipes() {
		return recipes;
	}
}
