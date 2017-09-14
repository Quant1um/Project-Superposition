package net.quantium.projectsuperposition.events;

import java.util.List;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.core.tickdispatcher.Metadata;
import net.quantium.projectsuperposition.core.tickdispatcher.TaskRunnable;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher.TickType;
import net.quantium.projectsuperposition.protection.EnergyUtils;
import net.quantium.projectsuperposition.protection.field.TileEntityFieldGenerator;
import net.quantium.projectsuperposition.protection.field.world.FieldInfo;
import net.quantium.projectsuperposition.protection.field.world.WorldFieldContainer;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class HandlerCardClicked {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityInteract(EntityInteractEvent event){
		if(event.entityPlayer != null){
			if(event.entityPlayer.inventory.getCurrentItem() != null){
				if(event.entityPlayer.inventory.getCurrentItem().getItem() == ModProvider.ITEMS.get(ObjectNames.ITEM_BIOMETRIC_CARD)){
					if(event.entityPlayer.inventory.getCurrentItem().getTagCompound() != null && (event.entityPlayer.inventory.getCurrentItem().getTagCompound().getInteger("protect") == 5 || event.entityPlayer.inventory.getCurrentItem().getTagCompound().getInteger("protect") == 10)){
						if(event.target instanceof EntityPlayer) {
							if(event.entityPlayer.inventory.getCurrentItem().stackSize <= 1)
								event.entityPlayer.inventory.getCurrentItem().getTagCompound().setString("name", ((EntityPlayer) event.target).getCommandSenderName());
							else{
								ItemStack item = event.entityPlayer.inventory.getCurrentItem().copy();
								item.stackSize = 1;
								item.getTagCompound().setString("name", ((EntityPlayer) event.target).getCommandSenderName());
								if(event.entityPlayer.inventory.addItemStackToInventory(item))
									event.entityPlayer.inventory.getCurrentItem().stackSize -= 1;
							}
						}
					}
				}
			}
		}
	}
}
