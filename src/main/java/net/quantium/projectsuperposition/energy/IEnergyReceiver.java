package net.quantium.projectsuperposition.energy;

import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.net.INetNode;

public interface IEnergyReceiver extends IEnergyNode{
	
	public int receive(int count);
}
