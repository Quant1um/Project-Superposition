package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.tileentities.TileEntityHoloText;
import net.quantium.projectsuperposition.utilities.Utils;

public class BlockHoloText extends BlockContainer {

	public BlockHoloText() {
		super(Material.iron);
		this.setCreativeTab(ModProvider.TAB).setBlockName("holotext").setBlockTextureName(ModProvider.MODID + ":protect").setHardness(2.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public int damageDropped(int metadata) {
	    return 0;
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int meta, float hx, float hy, float hz) {
		if(ply.isSneaking() || w.getTileEntity(x, y, z) == null) return false;
		if(ply.getCurrentEquippedItem() != null && ply.getCurrentEquippedItem().getItem() == ModProvider.ITEMS.get(ObjectNames.ITEM_SCREWDRIVER)){
			w.setBlockMetadataWithNotify(x, y, z, (w.getBlockMetadata(x, y, z) + 1) & 15, 7);
		}else if(ply.getCurrentEquippedItem() != null && ply.getCurrentEquippedItem().getItem() == ModProvider.ITEMS.get(ObjectNames.ITEM_SOLDERING_IRON)){
			if(w.getTileEntity(x, y, z) instanceof TileEntityHoloText){
				((TileEntityHoloText)w.getTileEntity(x, y, z)).rotation = !((TileEntityHoloText)w.getTileEntity(x, y, z)).rotation;
				w.markBlockForUpdate(x, y, z);
				w.getTileEntity(x, y, z).markDirty();
			}
		}else
			ply.openGui(ModProvider.INSTANCE, 0, w, x, y, z);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileEntityHoloText();
	}
	@Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase e, ItemStack i){
    	w.setBlockMetadataWithNotify(x, y, z, Utils.getMetaFromYaw(e, 16), 7);
    }
}
