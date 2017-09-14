package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.tileentities.TileEntityImpulsiveSuctionModule;

public class BlockImpulsiveSuction extends BlockContainer{

	public BlockImpulsiveSuction() {
		super(Material.iron);
		this.setCreativeTab(ModProvider.TAB);
		this.setHardness(2.0f);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
		this.setBlockName("ism");
		this.setBlockTextureName(ModProvider.MODID + ":ism");
	}

	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int meta, float hx, float hy, float hz) {
		if(ply.isSneaking() || w.getTileEntity(x, y, z) == null) return false;
		ply.openGui(ModProvider.INSTANCE, 0, w, x, y, z);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityImpulsiveSuctionModule();
	}
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
    	dropInventory(world, x, y, z);
		return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }
    
    public void dropInventory(World w, int x, int y, int z){
    	TileEntity te = w.getTileEntity(x, y, z);
        if(te == null || !(te instanceof IInventory)) return;
        net.quantium.projectsuperposition.utilities.Utils.dropInventory(w, x, y, z, ((IInventory) te));
    }
	
}
