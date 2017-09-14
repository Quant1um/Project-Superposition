package net.quantium.projectsuperposition.protection;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.tileentities.TileEntityEnergyController;

public class EnergyUtils {
	
	
	public static boolean energyHandle(World w, int x, int y, int z, ForgeDirection d, int e){
		if(w.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ) == null || !(w.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ) instanceof TileEntityEnergyController)) return false;
		return ((TileEntityEnergyController)w.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ)).ic2EnergySink.useEnergy(e);
	}
	
	public static boolean energyHandle(World w, int x, int y, int z, int e){
		for(int i = 0; i < 6; i++)
			if(energyHandle(w, x, y, z, ForgeDirection.VALID_DIRECTIONS[i], e)) return true;
		return false;
	}
	
	public static boolean energyHandleExcept(World w, int x, int y, int z, ForgeDirection d, int e){
		for(int i = 0; i < 6; i++)
			if(ForgeDirection.VALID_DIRECTIONS[i] != d)
				if(energyHandle(w, x, y, z, ForgeDirection.VALID_DIRECTIONS[i], e)) return true;
		return false;
	}
}
