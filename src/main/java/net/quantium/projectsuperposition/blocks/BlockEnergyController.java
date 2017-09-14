package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.tileentities.TileEntityEnergyController;

public class BlockEnergyController extends BlockContainer{

	public BlockEnergyController() {
		super(Material.iron);
		this.setCreativeTab(ModProvider.TAB);
		this.setHardness(2.0f);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setBlockName("energyController");
		this.setBlockTextureName(ModProvider.MODID + ":energy");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEnergyController();
	}
	
}
