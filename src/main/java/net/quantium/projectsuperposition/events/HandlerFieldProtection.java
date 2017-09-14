package net.quantium.projectsuperposition.events;

import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.quantium.projectsuperposition.core.tickdispatcher.Metadata;
import net.quantium.projectsuperposition.core.tickdispatcher.TaskRunnable;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher.TickType;
import net.quantium.projectsuperposition.protection.EnergyUtils;
import net.quantium.projectsuperposition.protection.field.TileEntityFieldGenerator;
import net.quantium.projectsuperposition.protection.field.world.FieldInfo;
import net.quantium.projectsuperposition.protection.field.world.WorldFieldContainer;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class HandlerFieldProtection {
	
	@SubscribeEvent
	public void onBlockBreak(BreakEvent ev) {
		List<FieldInfo> fields = WorldFieldContainer.get(ev.world).getFieldsOn(ev.world, new IntegerVector3(ev.x, ev.y, ev.z));
		if(fields.size() == 1){
			if(ev.x == fields.get(0).main.x() && ev.y == fields.get(0).main.y() && ev.z == fields.get(0).main.z()){
				return;
			}
			if(getTileEntity(ev.world, fields.get(0).main) != null && getTileEntity(ev.world, fields.get(0).main) instanceof TileEntityFieldGenerator){
				TileEntityFieldGenerator t = (TileEntityFieldGenerator) fields.get(0).getTileEntity(ev.world);
				if(!t.enabled() || !EnergyUtils.energyHandle(ev.world, t.xCoord, t.yCoord, t.zCoord, ForgeDirection.DOWN, 10)) return;
				if(ev.getPlayer() != null){
					if(t.preventFromBlockDestroy() && t.pattern() != null && t.pattern().protectFrom(ev.getPlayer()) && !ev.getPlayer().getCommandSenderName().equals(t.owner()))
						ev.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockHarvestDrops(final HarvestDropsEvent ev){
		List<FieldInfo> fields = WorldFieldContainer.get(ev.world).getFieldsOn(ev.world, new IntegerVector3(ev.x, ev.y, ev.z));
		if(fields.size() == 1){
			if(ev.x == fields.get(0).main.x() && ev.y == fields.get(0).main.y() && ev.z == fields.get(0).main.z()){
				return;
			}
			if(getTileEntity(ev.world, fields.get(0).main) != null && getTileEntity(ev.world, fields.get(0).main) instanceof TileEntityFieldGenerator){
				TileEntityFieldGenerator t = (TileEntityFieldGenerator) fields.get(0).getTileEntity(ev.world);
				if(!t.enabled() || !EnergyUtils.energyHandle(ev.world, t.xCoord, t.yCoord, t.zCoord, ForgeDirection.DOWN, 15)) return;
				if(ev.harvester != null){
					if(t.preventFromBlockDestroy() && t.pattern() != null && t.pattern().protectFrom(ev.harvester)){
						ev.dropChance = 0f;
						final TileEntity ee = ev.world.getTileEntity(ev.x, ev.y, ev.z);
						TickDispatcher.task(new TaskRunnable(){
							@Override
							public void run(Metadata meta) {
								ev.world.setBlock(ev.x, ev.y, ev.z, ev.block);
								ev.world.setBlockMetadataWithNotify(ev.x, ev.y, ev.z, ev.blockMetadata, 2);
								if(ee != null && ev.world.getTileEntity(ev.x, ev.y, ev.z) != null){ 
									NBTTagCompound tag = new NBTTagCompound();
									ee.writeToNBT(tag);
									ev.world.getTileEntity(ev.x, ev.y, ev.z).readFromNBT(tag);
								}
							}
							
						}, 1, TickType.WORLD);
					}
				}else if(t.preventFromBlockDestroy()){
					ev.dropChance = 0f;
					final TileEntity ee = ev.world.getTileEntity(ev.x, ev.y, ev.z);
					TickDispatcher.task(new TaskRunnable(){
						@Override
						public void run(Metadata meta) {
							ev.world.setBlock(ev.x, ev.y, ev.z, ev.block);
							ev.world.setBlockMetadataWithNotify(ev.x, ev.y, ev.z, ev.blockMetadata, 2);
							if(ee != null && ev.world.getTileEntity(ev.x, ev.y, ev.z) != null){ 
								NBTTagCompound tag = new NBTTagCompound();
								ee.writeToNBT(tag);
								ev.world.getTileEntity(ev.x, ev.y, ev.z).readFromNBT(tag);
							}
						}
						
					}, 1, TickType.WORLD);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockPlace(PlaceEvent ev) {
		List<FieldInfo> fields = WorldFieldContainer.get(ev.world).getFieldsOn(ev.world, new IntegerVector3(ev.x, ev.y, ev.z));
		if(fields.size() == 1){
			if(getTileEntity(ev.world, fields.get(0).main) != null && getTileEntity(ev.world, fields.get(0).main) instanceof TileEntityFieldGenerator){
				TileEntityFieldGenerator t = (TileEntityFieldGenerator) fields.get(0).getTileEntity(ev.world);
				if(!t.enabled() || !EnergyUtils.energyHandle(ev.world, t.xCoord, t.yCoord, t.zCoord, ForgeDirection.DOWN, 20)) return;
				if(ev.player != null){
					if(t.preventFromPlacing() && t.pattern() != null && t.pattern().protectFrom(ev.player) && !ev.player.getCommandSenderName().equals(t.owner()))
						ev.setCanceled(true);
				}else if(t.preventFromPlacing())
					ev.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockInteract(PlayerInteractEvent ev) {
		List<FieldInfo> fields = WorldFieldContainer.get(ev.world).getFieldsOn(ev.world, new IntegerVector3(ev.x, ev.y, ev.z));
		if(fields.size() == 1){
			if(getTileEntity(ev.world, fields.get(0).main) != null && getTileEntity(ev.world, fields.get(0).main) instanceof TileEntityFieldGenerator){
				TileEntityFieldGenerator t = (TileEntityFieldGenerator) fields.get(0).getTileEntity(ev.world);
				if(!t.enabled() || !EnergyUtils.energyHandle(ev.world, t.xCoord, t.yCoord, t.zCoord, ForgeDirection.DOWN, 30)) return;
				if(ev.entityPlayer != null){
					if(t.preventFromPlacing() && t.pattern() != null && t.pattern().protectFrom(ev.entityPlayer) && !ev.entityPlayer.getCommandSenderName().equals(t.owner()))
						ev.setCanceled(true);
				}else if(t.preventFromPlacing())
					ev.setCanceled(true);
			}
		}
	}
	
	public TileEntity getTileEntity(World w, IntegerVector3 v){
		return w.getTileEntity(v.x(), v.y(), v.z());
	}
}
