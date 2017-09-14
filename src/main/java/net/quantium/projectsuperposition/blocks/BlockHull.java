package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.quantium.projectsuperposition.ModProvider;

public class BlockHull extends Block {

	public BlockHull() {
		super(Material.iron);
		this.setCreativeTab(ModProvider.TAB).setStepSound(soundTypeMetal).setHardness(5.0f).setBlockTextureName(ModProvider.MODID + ":fayalitehull").setResistance(100.0f).setHarvestLevel("pickaxe", 3);
		this.slipperiness = 0.55F;
	}
}
