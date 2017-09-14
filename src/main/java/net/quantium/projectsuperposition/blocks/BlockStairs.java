package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.Block;
import net.quantium.projectsuperposition.ModProvider;

public class BlockStairs extends net.minecraft.block.BlockStairs{

	public BlockStairs(Block block) {
		super(block, 0);
		this.setCreativeTab(ModProvider.TAB);
		this.useNeighborBrightness = true;
		this.slipperiness = 0.55F;
	}
}
