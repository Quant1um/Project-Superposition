package net.quantium.projectsuperposition.protection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ProtectorContainer extends Container {

	public IProtector protector;
	public TileEntity tileEntity;
	
	public ProtectorContainer(IProtector e, TileEntity ee, InventoryPlayer ply){
		this.protector = e;
		this.tileEntity = ee;
		for(int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(e.pattern(), i, 7 + i * 18, 26));
		bindPlayerInventory(ply);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer e) {
		return e.getDistanceSq(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord) < 64;
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
