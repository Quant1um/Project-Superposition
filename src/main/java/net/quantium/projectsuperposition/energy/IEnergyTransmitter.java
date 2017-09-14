package net.quantium.projectsuperposition.energy;

import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.net.INetNode;

public interface IEnergyTransmitter extends IEnergyNode{
	
	public boolean transmit(int count);
}
