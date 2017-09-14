package net.quantium.projectsuperposition.core.soldering;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.quantium.projectsuperposition.api.recipes.IModRecipeRegistry;
import net.quantium.projectsuperposition.api.recipes.soldering.ISolderingRecipe;
import net.quantium.projectsuperposition.api.recipes.welding.IWeldingRecipe;

public class SolderingRecipeRegistry implements IModRecipeRegistry<ISolderingRecipe>{
	private List<ISolderingRecipe> recipes = new ArrayList<ISolderingRecipe>();
	
	public ItemStack get(IInventory matrix){
		ISolderingRecipe r = getRecipe(matrix);
		if(r == null) return null;
		return r.getCraftItem(matrix);
	}
	
	public void register(ISolderingRecipe recipe){
		if(recipe == null) return;
		recipes.add(recipe);
	}

	public ISolderingRecipe getRecipe(IInventory matrix) {
		if(matrix == null) return null;
		for(int i = 0; i < recipes.size(); i++) if (recipes.get(i).getCraftItem(matrix) != null) return recipes.get(i);
		return null;
	}
	
	@Override
	public List<ISolderingRecipe> recipes() {
		return recipes;
	}
}
