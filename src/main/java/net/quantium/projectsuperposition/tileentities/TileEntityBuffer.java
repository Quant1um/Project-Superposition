package net.quantium.projectsuperposition.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.energy.EnergyDelegate;
import net.quantium.projectsuperposition.energy.IEnergyBuffer;
import net.quantium.projectsuperposition.energy.IEnergyNode;
import net.quantium.projectsuperposition.energy.IEnergyReceiver;
import net.quantium.projectsuperposition.energy.IEnergyTransmitter;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class TileEntityBuffer extends TileEntity implements IEnergyNode{

	private int tier;
	
	public EnergyDelegate delegate;
	
	public TileEntityBuffer(int tier){
		this.tier = tier;
		delegate = new EnergyDelegate(this, getCapacityByTier(tier), true, true);
	}
	
	public TileEntityBuffer(){
		this(0);
	}
	
	public static int getCapacityByTier(int tier){
		return 4096 << (tier * 2);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		tier = tag.getInteger("tier");
		delegate.readEnergy(tag);
		delegate.setCapacity(getCapacityByTier(tier));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag){
		delegate.writeEnergy(tag);
		tag.setInteger("tier", tier);
	}

	@Override
	public boolean connection(World w, IntegerVector3 v, ForgeDirection side) {
		return true;
	}
	
	@Override
	public void updateEntity(){

	}
}
