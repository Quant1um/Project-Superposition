package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.net.INetNode;
import net.quantium.projectsuperposition.net.WorldNetContainer;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public abstract class BlockNodeContainer extends BlockContainer{

	protected Class<? extends INetNode> nodeClass;
	
	protected BlockNodeContainer(Material material, Class<? extends INetNode> nodeClass) {
		super(material);
		this.nodeClass = nodeClass;
	}

	@Override
	public void onBlockAdded(World w, int x, int y, int z)
    {
        super.onBlockAdded(w, x, y, z);
        if(w.getTileEntity(x, y, z) != null)
        	WorldNetContainer.get(w).getNet(w, this.nodeClass).addNode(new IntegerVector3(x, y, z), (INetNode) w.getTileEntity(x, y, z));
    }

	@Override
    public void breakBlock(World w, int x, int y, int z, Block b, int m)
    {
        WorldNetContainer.get(w).getNet(w, this.nodeClass).removeNode(new IntegerVector3(x, y, z));
        super.breakBlock(w, x, y, z, b, m);
    }
	
	public Class<? extends INetNode> getNodeClass(){
		return nodeClass;
	}
}
