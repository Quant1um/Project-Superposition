package net.quantium.projectsuperposition.core.raycast;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.quantium.projectsuperposition.utilities.TupleBlockMetadata;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;
import net.quantium.projectsuperposition.utilities.vector.Vector3;

public class RaycastHit {
	public boolean collided;
	public TupleBlockMetadata collidedBlock;
	public Entity collidedEntity;
	
	public float distance;
	public IntegerVector3 collisionPointBlock;
	public Vector3 collisionPoint;
	
	@Override
	public String toString(){
		return "{" + collided + "; " + collidedBlock + "; " + collidedEntity + "; " + distance + "; " + collisionPoint + "; " + collisionPointBlock + "}";
	}
}
