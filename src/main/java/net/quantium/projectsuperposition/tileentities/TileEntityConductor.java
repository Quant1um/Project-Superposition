package net.quantium.projectsuperposition.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.energy.IEnergyConductor;
import net.quantium.projectsuperposition.energy.IEnergyNode;
import net.quantium.projectsuperposition.net.WorldNetContainer;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class TileEntityConductor extends TileEntity implements IEnergyConductor{

	private boolean isInjectedIntoNet;

	@Override
	public float resistance() {
		return 0.1f;
	}
	
	@Override
	public void updateEntity(){
		if(!isInjectedIntoNet){
			WorldNetContainer.get(getWorldObj()).getNet(getWorldObj(), IEnergyNode.class).addNode(new IntegerVector3(xCoord, yCoord, zCoord), this);
			isInjectedIntoNet = true;
		}
	}

	@Override
	public boolean connection(World w, IntegerVector3 v, ForgeDirection side) {
		return true;
	}
}
