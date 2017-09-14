package net.quantium.projectsuperposition.containers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomSlot extends Slot {

	private List<Item> allowed;

	public CustomSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, List<Item> allowed) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		this.allowed = allowed;
		
	}
	
	public CustomSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, Item... allowed) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		this.allowed = Arrays.asList(allowed);
	}

	@Override
	public boolean isItemValid(ItemStack is) {
		return allowed.contains(is.getItem());
	}
	
}
