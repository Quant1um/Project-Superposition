package net.quantium.projectsuperposition.energy;

import java.util.List;

import ic2.api.energy.IEnergyNet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.net.Net;
import net.quantium.projectsuperposition.net.WorldNetContainer;
import net.quantium.projectsuperposition.utilities.Utils;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class EnergyDelegate implements IEnergyBuffer, IEnergyReceiver, IEnergyTransmitter{
	
	private final TileEntity container;
	private final boolean canReceive, canTransmit;
	
	private int capacity;
	private int energy;
	
	public TileEntity container() {
		return container;
	}

	public boolean canReceive() {
		return canReceive;
	}

	public boolean canTransmit() {
		return canTransmit;
	}

	public EnergyDelegate(TileEntity container, int capacity, boolean asTransmitter, boolean asReceiver){
		if(!(container instanceof IEnergyNode)) throw new RuntimeException("EnergyDelegate's container must implement IEnergyNode interface");
		this.container = container;
		this.canReceive = asReceiver;
		this.canTransmit = asTransmitter;
		this.capacity = capacity;
	}

	@Override
	public boolean connection(World w, IntegerVector3 v, ForgeDirection side) {
		if(container instanceof IEnergyNode) return ((IEnergyNode)container).connection(w, v, side);
		return false;
	}

	@Override
	public boolean transmit(int count) {
		if(!canTransmit) return false;
		if(count > energy) return false;
		if(getEnergyNet() == null) return false;
		List<IEnergyNode> receivers = getEnergyNet().getNodes(IEnergyReceiver.class);
		receivers.remove(this);
		if(receivers.size() <= 0) return false;
		int[] a = Utils.splitIntoParts(count, receivers.size());
		for(int i = 0; i < receivers.size(); i++)
			receive(((IEnergyReceiver)receivers.get(i)).receive(a[i]));
		return true;
	}

	public Net<IEnergyNode> getEnergyNet(){
		return WorldNetContainer.get(container.getWorldObj()).getNet(container.getWorldObj(), IEnergyNode.class).getNet(new IntegerVector3(container.xCoord, container.yCoord, container.zCoord));
	}
	
	@Override
	public int receive(int count) {
		int newc = count + energy;
		int remainder = newc - capacity;
		if(remainder <= 0){
			energy = newc;
			return 0;
		}
		energy = capacity;
		return remainder;
	}

	@Override
	public int energy() {
		return energy;
	}

	@Override
	public int capacity() {
		return capacity;
	}
	
	public void readEnergy(NBTTagCompound tag){
		energy = tag.getInteger("openchan_energy");
	}
	
	public void writeEnergy(NBTTagCompound tag){
		tag.setInteger("openchan_energy", energy);
	}
	
	public void setCapacity(int capacity){
		if(capacity <= 0) return;
		this.capacity = capacity;
		if(energy > capacity) energy = capacity;
	}
}
