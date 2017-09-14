package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.energy.IEnergyNode;
import net.quantium.projectsuperposition.tileentities.TileEntityBuffer;

public class BlockEnergyBuffer extends BlockNodeContainer{

	public BlockEnergyBuffer() {
		super(Material.iron, IEnergyNode.class);
		this.setCreativeTab(ModProvider.TAB);
		this.setHardness(2.0f);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setBlockName("energyBuffer");
		this.setBlockTextureName(ModProvider.MODID + ":energy");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBuffer(2);
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if(!w.isRemote){
			TileEntityBuffer b = (TileEntityBuffer) w.getTileEntity(x, y, z);
			ply.addChatMessage(new ChatComponentText("Energy: " + b.delegate.energy() + "/" + b.delegate.capacity()));
		}
		return false;
	}
}
