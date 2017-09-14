package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.energy.IEnergyNode;
import net.quantium.projectsuperposition.tileentities.TileEntityConductor;

public class BlockConductor extends BlockNodeContainer{	
	public BlockConductor() {
		super(Material.iron, IEnergyNode.class);
		this.setCreativeTab(ModProvider.TAB);
		this.setHardness(0.6f).setResistance(1.2f);
		this.setBlockName("conductor");
	}

	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileEntityConductor();
	}
}
