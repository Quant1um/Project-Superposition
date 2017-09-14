package net.quantium.projectsuperposition.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.items.ItemPortalLinker;
import net.quantium.projectsuperposition.tileentities.TileEntityTeleport;
import net.quantium.projectsuperposition.utilities.Utils;
import net.quantium.projectsuperposition.utilities.vector.VectorUtils;
import net.quantium.projectsuperposition.utilities.vector.WorldVector4;

public class BlockTeleport extends BlockContainer {

	public BlockTeleport() {
		super(Material.iron);
		this.setCreativeTab(ModProvider.TAB).setBlockName("enderporter").setBlockTextureName(ModProvider.MODID + ":protect").setHardness(2.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.875F, 1.0F);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
		
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int meta, float hx, float hy, float hz) {
		if(ply.isSneaking() || w.getTileEntity(x, y, z) == null || !(w.getTileEntity(x, y, z) instanceof TileEntityTeleport)) return false;
		if(ply.inventory.getCurrentItem() != null && ply.inventory.getCurrentItem().getItem() instanceof ItemPortalLinker){
			if(ply.inventory.getCurrentItem().getTagCompound() != null && ply.inventory.getCurrentItem().getTagCompound().hasKey("link")){
				WorldVector4 d = new WorldVector4(VectorUtils.readFromNBT(ply.inventory.getCurrentItem().getTagCompound().getCompoundTag("link")));
				if(d.getTileEntity() != null && d.getTileEntity() instanceof TileEntityTeleport){
					TileEntityTeleport te = (TileEntityTeleport) w.getTileEntity(x, y, z);
					if(te.connected != null && !te.connected.equals(d)){
						if(te.connected.getTileEntity() != null && te.connected.getTileEntity() instanceof TileEntityTeleport){
							((TileEntityTeleport)te.connected.getTileEntity()).connected = null;
						}
					}
					te.connected = d;
					if(((TileEntityTeleport)d.getTileEntity()).connected != null && !((TileEntityTeleport)d.getTileEntity()).connected.equals(new WorldVector4(x, y, z, w.provider.dimensionId))){
						if(((TileEntityTeleport)d.getTileEntity()).connected.getTileEntity() != null && ((TileEntityTeleport)d.getTileEntity()).connected.getTileEntity() instanceof TileEntityTeleport){
							((TileEntityTeleport)((TileEntityTeleport)d.getTileEntity()).connected.getTileEntity()).connected = null;
						}
					}
					((TileEntityTeleport)d.getTileEntity()).connected = new WorldVector4(x, y, z, w.provider.dimensionId);
					ply.inventory.getCurrentItem().getTagCompound().removeTag("link");
					if(!w.isRemote) ply.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("linker.message")));
				}else{
					ply.inventory.getCurrentItem().getTagCompound().removeTag("link");
				}
			}else{
				if(ply.inventory.getCurrentItem().getTagCompound() == null) ply.inventory.getCurrentItem().setTagCompound(new NBTTagCompound());
				NBTTagCompound tag = new NBTTagCompound();
				WorldVector4 d = new WorldVector4(x, y, z, w.provider.dimensionId);
				VectorUtils.writeToNBT(d, tag);
				if(ply.inventory.getCurrentItem().stackSize <= 1) ply.inventory.getCurrentItem().getTagCompound().setTag("link", tag);
				else{
					ItemStack i = ply.inventory.getCurrentItem().copy();
					i.stackSize = 1;
					i.getTagCompound().setTag("link", tag);
					if(Utils.addToInv(ply.inventory, i)) ply.inventory.getCurrentItem().stackSize--;
				}
			}
		}
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		return new TileEntityTeleport();
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

}
