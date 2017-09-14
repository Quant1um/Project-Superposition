package net.quantium.projectsuperposition.utilities.vector;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WorldVector4 extends IntegerVector3 {
	
	private int dim;
	
	public WorldVector4(){
		super();
	}
	
	public WorldVector4(int v){
		super(v);
	}
	
	public WorldVector4(int x, int y, int z, int w){
		super(x, y, z);
		this.dim = w;
	}
	
	public WorldVector4(IVector v){
		super(v);
	}
	
	public int dimension(){
		return dim;
	}
	
	public World getWorld(){
		return MinecraftServer.getServer().worldServerForDimension(dim);
	}
	
	public TileEntity getTileEntity(){
		if(getWorld() == null) return null;
		return getWorld().getTileEntity(x, y, z);
	}
	
	public Block getBlock(){
		if(getWorld() == null) return null;
		return getWorld().getBlock(x, y, z);
	}
	
	public int getMetadata(){
		if(getWorld() == null) return 0;
		return getWorld().getBlockMetadata(x, y, z);
	}
}
