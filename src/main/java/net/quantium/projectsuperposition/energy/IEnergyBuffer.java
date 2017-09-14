package net.quantium.projectsuperposition.energy;

import net.quantium.projectsuperposition.net.INetNode;

public interface IEnergyBuffer extends IEnergyNode{
	
	public int energy();
	public int capacity();
}
