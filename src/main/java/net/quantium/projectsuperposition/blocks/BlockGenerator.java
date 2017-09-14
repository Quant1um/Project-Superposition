package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.energy.IEnergyNode;
import net.quantium.projectsuperposition.tileentities.TileEntityGenerator;

public class BlockGenerator extends BlockNodeContainer{	
	public BlockGenerator() {
		super(Material.iron, IEnergyNode.class);
		this.setCreativeTab(ModProvider.TAB);
		this.setHardness(2.6f).setResistance(4.2f);
		this.setBlockName("generator");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityGenerator();
	}
}
