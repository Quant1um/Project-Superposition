package net.quantium.projectsuperposition.protection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.protection.electriccoil.TileEntityElectricCoil;
import net.quantium.projectsuperposition.protection.network.GetOwnerMessage;
import scala.actors.threadpool.Arrays;

public class ProtectorBlock extends BlockContainer {
	private Class<? extends TileEntity> tileEntity;
	public ProtectorBlock(Class<? extends TileEntity> te) {
		super(Material.iron);
		this.setCreativeTab(ModProvider.TAB);
		this.setBlockTextureName(ModProvider.MODID + ":protect");
		this.tileEntity = te;
		this.setBlockBounds(0.15F, 0.0F, 0.15F, 0.85F, 0.9F, 0.85F);//default
		this.setHardness(2.0f);
		this.setResistance(10.0f);
		this.setHarvestLevel("pickaxe", 2);
	}
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		if(ply.isSneaking() || w.getTileEntity(x, y, z) == null) return false;
		if(ply.inventory.getCurrentItem() != null && ply.inventory.getCurrentItem().getItem() == ModProvider.ITEMS.get(ObjectNames.ITEM_SCREWDRIVER)){
			ply.openGui(ModProvider.INSTANCE, 0, w, x, y, z);
			if(!w.isRemote && w.getTileEntity(x, y, z) instanceof IProtector) {
				PacketDispatcher.sendTo(new GetOwnerMessage(x, y, z, ((IProtector)w.getTileEntity(x, y, z)).owner()), (EntityPlayerMP) ply);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World w, int meta) {
		try {
			return (TileEntity) this.tileEntity.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
    	if(w.getTileEntity(x, y, z) != null && this.tileEntity.isInstance(w.getTileEntity(x, y, z)) && e instanceof EntityPlayer)
    		((IProtector)w.getTileEntity(x, y, z)).setOwner(((EntityPlayer) e).getCommandSenderName());
    }
    
    @Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
    	dropInventory(world, x, y, z);
		return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }
    
    public void dropInventory(World w, int x, int y, int z){
    	TileEntity te = w.getTileEntity(x, y, z);
        if(te != null && te instanceof IProtector)
        	net.quantium.projectsuperposition.utilities.Utils.dropInventory(w, x, y, z, ((IProtector) te).pattern());
    }
}
