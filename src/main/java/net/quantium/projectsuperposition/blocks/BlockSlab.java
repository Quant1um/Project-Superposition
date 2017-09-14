package net.quantium.projectsuperposition.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.core.tickdispatcher.Metadata;
import net.quantium.projectsuperposition.core.tickdispatcher.TaskRunnable;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher.TickType;
import net.quantium.projectsuperposition.protection.IProtector;

public class BlockSlab extends net.minecraft.block.BlockSlab{

	public BlockSlab(boolean a) {
		super(a, Material.iron);
		if(!a) this.setCreativeTab(ModProvider.TAB);
		this.setBlockName("platedSlab");
		this.useNeighborBrightness = true;
		this.slipperiness = 0.55F;
	}
	
	private IIcon side;
	
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (this.field_150004_a && (meta & 8) != 0)
        {
            side = 1;
        }
        return side <= 1 ? this.blockIcon : this.side;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(ModProvider.MODID + ":fayalitehull");
        this.side = reg.registerIcon(ModProvider.MODID + ":fayaliteslab");
    }

    public Item getItemDropped(int a, Random r, int m)
    {
        return Item.getItemFromBlock(ModProvider.BLOCKS.get(ObjectNames.BLOCK_PLATED_SLAB));
    }

    protected ItemStack createStackedBlock(int c)
    {
        return new ItemStack(Item.getItemFromBlock(ModProvider.BLOCKS.get(ObjectNames.BLOCK_PLATED_SLAB)), 1, 0);
    }

	@Override
	public String func_150002_b(int f) {
		return this.getUnlocalizedName();
	}

	@Override
    public int onBlockPlaced(World w, int x, int y, int z, int side, float xx, float yy, float zz, int m){
		if(yy <= 0.5f && side != 0){
			w.setBlockMetadataWithNotify(x, y, z, 0, 0);
			return 0;
		}else if(yy > 0.5f && side != 1){
			w.setBlockMetadataWithNotify(x, y, z, 8, 0);
			return 8;
		}
		return super.onBlockPlaced(w, x, y, z, side, xx, yy, zz, m);
    }
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int side, float hx, float hy, float hz) {
		if(ply.inventory.getCurrentItem() != null && ply.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(ModProvider.BLOCKS.get(ObjectNames.BLOCK_PLATED_SLAB)) && w.getBlock(x, y, z) == ModProvider.BLOCKS.get(ObjectNames.BLOCK_PLATED_SLAB)){
			if(side != 1 && hy <= 0.5f && (w.getBlockMetadata(x, y, z) & 8) == 0) return false;
			if(side != 0 && hy > 0.5f && (w.getBlockMetadata(x, y, z) & 8) != 0) return false;
			w.setBlock(x, y, z, ModProvider.BLOCKS.get(ObjectNames.BLOCK_PLATED_SLAB_DOUBLED));
			if(ply.capabilities.isCreativeMode) return true;
			ply.inventory.getCurrentItem().stackSize--;
			if(ply.inventory.getCurrentItem().stackSize <= 0) ply.setCurrentItemOrArmor(0, null);
			return true;
		}
		return false;
	}
}
