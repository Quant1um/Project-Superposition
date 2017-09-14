package net.quantium.projectsuperposition.containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.tileentities.TileEntityWeldingStation;

public class ContainerWelding extends Container {

	public TileEntityWeldingStation ent;
	
	private int lastTime = 0;
	
	public ContainerWelding(TileEntityWeldingStation ee, InventoryPlayer ply){
		this.ent = ee;
		
		this.addSlotToContainer(new CustomSlot(ee, 17, 8 + 0 * 18, 27 - 1 * 18, ModProvider.ITEMS.get(ObjectNames.ITEM_SOLENOID)));
		
		for(int i = 0; i < 5; i++)
			for(int j = 0; j < 3; j++)
				this.addSlotToContainer(new Slot(ee, i + j * 5, 47 + i * 18, 9 + j * 18));
		
		this.addSlotToContainer(new CustomSlot(ee, 15, 8 + 8 * 18, 27 + 1 * 18, ModProvider.ITEMS.get(ObjectNames.ITEM_WELDER)));
		this.addSlotToContainer(new OutputSlot(ee, 16, 8 + 8 * 18, 27 + 0 * 18));
		bindPlayerInventory(ply);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer e) {
		return e.getDistanceSq(ent.xCoord, ent.yCoord, ent.zCoord) < 64;
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

	public void addCraftingToCrafters(ICrafting p_75132_1_)
	{
		super.addCraftingToCrafters(p_75132_1_);
	 	p_75132_1_.sendProgressBarUpdate(this, 0, this.ent.time);
	}

	    /**
	     * Looks for changes made in the container, sends them to every listener.
	     */
	public void detectAndSendChanges()
	{
	    super.detectAndSendChanges();

	    for (int i = 0; i < this.crafters.size(); ++i)
	    {
	        ICrafting icrafting = (ICrafting)this.crafters.get(i);

	        if (lastTime != this.ent.time)
	        {
	            icrafting.sendProgressBarUpdate(this, 0, this.ent.time);
	            lastTime = this.ent.time;
	        }
	    }
	}
	    
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int p_75137_1_, int p_75137_2_)
	{
		this.ent.time = p_75137_2_;
	}
}
