package net.quantium.projectsuperposition.items;

import net.minecraft.item.ItemStack;

public interface IHeatPowerable {
	
	public int receiveHeat(ItemStack item, int energy);
	public int capacityHeat(ItemStack item);
	public int currentHeat(ItemStack item);
}
