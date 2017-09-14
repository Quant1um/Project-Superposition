package net.quantium.projectsuperposition.core.raycast;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.quantium.projectsuperposition.utilities.TupleBlockMetadata;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class RaycastInfo {
	public List<Entity> ignoreEntities = new ArrayList<Entity>();
	public List<TupleBlockMetadata> ignoreBlocks = new ArrayList<TupleBlockMetadata>();
	public List<IntegerVector3> ignorePositions = new ArrayList<IntegerVector3>();
	public boolean collideNonOpaqueAsOpaque = false;
	public boolean collideWithEntities = false;
	
	public RaycastInfo(){
		this(new ArrayList<IntegerVector3>());
	}
	
	public RaycastInfo(List<IntegerVector3> ignorePositions){
		this(ignorePositions, new ArrayList<TupleBlockMetadata>());
	}
	
	public RaycastInfo(List<IntegerVector3> ignorePositions, List<TupleBlockMetadata> ignoreBlocks){
		this(ignorePositions, ignoreBlocks, false);
	}
	
	public RaycastInfo(List<IntegerVector3> ignorePositions, List<TupleBlockMetadata> ignoreBlocks, boolean collideWithEntities){
		this(ignorePositions, ignoreBlocks, collideWithEntities, new ArrayList<Entity>());
	}
	
	public RaycastInfo(List<IntegerVector3> ignorePositions, List<TupleBlockMetadata> ignoreBlocks, boolean collideWithEntities, List<Entity> ignoreEntities){
		this.ignoreBlocks = ignoreBlocks;
		this.ignoreEntities = ignoreEntities;
		this.ignorePositions = ignorePositions;
		this.collideWithEntities = collideWithEntities;
	}
}
