package net.quantium.projectsuperposition.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.protection.field.TileEntityFieldGenerator;
import net.quantium.projectsuperposition.tileentities.TileEntitySolderingStation;
import net.quantium.projectsuperposition.tileentities.TileEntityWeldingStation;

public class BlockWelding extends BlockContainer{

	private IIcon top, side, side0;
	
	public BlockWelding() {
		super(Material.iron);
		this.setCreativeTab(ModProvider.TAB).setBlockName("welding").setBlockTextureName(ModProvider.MODID + ":solderingBottom").setHardness(2.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityWeldingStation();
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int meta, float hx, float hy, float hz) {
		if(ply.isSneaking() || w.getTileEntity(x, y, z) == null) return false;
		ply.openGui(ModProvider.INSTANCE, 0, w, x, y, z);
		return true;
	}
	
	@Override
    public void registerBlockIcons(IIconRegister register)
    {
        super.registerBlockIcons(register);
        top = register.registerIcon(ModProvider.MODID + ":weldingTop");
        side = register.registerIcon(ModProvider.MODID + ":solderingSide");
        side0 = register.registerIcon(ModProvider.MODID + ":weldingSide");
    }
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if(side == 0) return this.blockIcon;
        if(side == 1) return this.top;
        if(side == 3) return this.side0;
        return this.side;
    }
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
    	TileEntity te = world.getTileEntity(x, y, z);
    	if(te != null && te instanceof IInventory)
    		net.quantium.projectsuperposition.utilities.Utils.dropInventory(world, x, y, z, (IInventory) te);
		return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

}
