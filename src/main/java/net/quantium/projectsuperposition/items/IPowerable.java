package net.quantium.projectsuperposition.items;

import net.minecraft.item.ItemStack;

public interface IPowerable {
	
	public boolean canTransmit(ItemStack item);
	public int receive(ItemStack item, int energy);
	public boolean transmit(ItemStack item, int energy);
	public int capacity(ItemStack item);
	public int current(ItemStack item);
}
