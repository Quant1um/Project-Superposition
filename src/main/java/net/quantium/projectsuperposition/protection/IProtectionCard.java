package net.quantium.projectsuperposition.protection;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IProtectionCard {
	
	public boolean protectFrom(Entity e, ItemStack item);
	public boolean accessTo(Entity e, ItemStack item);
}
