package net.quantium.projectsuperposition.utilities;

import net.minecraft.block.Block;

public class TupleBlockMetadata {
	public static final int METADATA_WILDCARD_VALUE = Short.MIN_VALUE;
	
	public Block block;
	public int metadata;
	
	public TupleBlockMetadata(Block b){
		this(b, METADATA_WILDCARD_VALUE);
	}
	
	public TupleBlockMetadata(Block b, int meta){
		this.block = b;
		this.metadata = meta;
	}
	
	@Override
	public boolean equals(Object o){
		if(o != null && o instanceof TupleBlockMetadata){
			TupleBlockMetadata other = (TupleBlockMetadata)o;
			if(other.block != block) return false;
			if(other.metadata == METADATA_WILDCARD_VALUE || metadata == METADATA_WILDCARD_VALUE) return true;
			if(other.metadata == metadata) return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "{" + block.getLocalizedName() + "; " + metadata + "}";
	}
}
