package net.quantium.projectsuperposition.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerImpulsiveSuction extends Container {

	public IInventory ent;
	public int x, y, z;
	
	public ContainerImpulsiveSuction(IInventory ee, InventoryPlayer ply, int x, int y, int z){
		this.ent = ee;
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 2; j++)
			this.addSlotToContainer(new Slot(ee, i + j * 9, 8 + i * 18, 26 + j * 18));
		bindPlayerInventory(ply);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer e) {
		return e.getDistanceSq(x, y, z) < 64;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
	    for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 9; j++) {
	                    addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
	                                    8 + j * 18, 72 + i * 18));
	            }
	    }

	    for (int i = 0; i < 9; i++) {
	            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 72 + 58));
	    }
	}
	
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		return null;
	} 
	
}
