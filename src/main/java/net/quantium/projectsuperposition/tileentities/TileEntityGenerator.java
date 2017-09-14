package net.quantium.projectsuperposition.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.energy.EnergyDelegate;
import net.quantium.projectsuperposition.energy.IEnergyNode;
import net.quantium.projectsuperposition.net.WorldNetContainer;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class TileEntityGenerator extends TileEntity implements IEnergyNode{

	private EnergyDelegate delegate = new EnergyDelegate(this, 100, true, false);
	
	@Override
	public boolean connection(World w, IntegerVector3 v, ForgeDirection side) {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag){
		delegate.readEnergy(tag);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag){
		delegate.writeEnergy(tag);
	}
	
	@Override
	public void updateEntity(){
		delegate.receive(10);
		delegate.transmit(10);
	}
}
