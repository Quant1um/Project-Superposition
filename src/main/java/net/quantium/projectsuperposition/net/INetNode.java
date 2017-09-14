package net.quantium.projectsuperposition.net;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public interface INetNode {
	public boolean connection(World w, IntegerVector3 v, ForgeDirection side);
}
