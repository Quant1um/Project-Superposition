package net.quantium.projectsuperposition.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.core.raycast.Raycast;
import net.quantium.projectsuperposition.core.raycast.RaycastHit;
import net.quantium.projectsuperposition.core.raycast.RaycastInfo;
import net.quantium.projectsuperposition.utilities.vector.Vector3;
import net.quantium.projectsuperposition.utilities.vector.VectorUtils;

public class ItemCell extends Item{

	private static List<Fluid> metadataFluids = new ArrayList<Fluid>();
	private static HashMap<Fluid, Integer> byFluidMetadata = new HashMap<Fluid, Integer>();
	
	static{
		metadataFluids.add(0, null);
	}
	
	private static ItemCell _instance;
	
	public static ItemCell instance(){
		if(_instance == null) _instance = new ItemCell();
		return _instance;
	}
	
	private ItemCell(){
		this.maxStackSize = 32;
	    this.setUnlocalizedName("cell");
	    this.setContainerItem(this);
	    this.setCreativeTab(ModProvider.TAB);
	    this.setHasSubtypes(true);
        this.setMaxDamage(0);
	}
	
	public static void registerFluid(Fluid fluid){
		if(byFluidMetadata.containsKey(fluid)) return;
		int meta = metadataFluids.size();
		metadataFluids.add(fluid);
		byFluidMetadata.put(fluid, meta);
		FluidContainerRegistry.registerFluidContainer(fluid, new ItemStack(instance(), 1, meta), new ItemStack(instance()));
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer ply, List l, boolean wtf){
		if(this.isFilled(is)) {
			Fluid f = this.getFiller(is);
			FluidStack fs = new FluidStack(f, 1000);
			l.add(StatCollector.translateToLocal("cell.liquid") + f.getLocalizedName(fs));
			l.add(StatCollector.translateToLocal("cell.density") + f.getDensity(fs));
			l.add(StatCollector.translateToLocal("cell.luminosity") + f.getLuminosity(fs));
			l.add(StatCollector.translateToLocal("cell.temperature") + f.getTemperature(fs));
			l.add(StatCollector.translateToLocal("cell.viscosity") + f.getViscosity(fs));
		}
	}
	
	public boolean place(World w, int x, int y, int z, ItemStack stack){
    	if(!isFilled(stack)) return false;
    	Material material = w.getBlock(x, y, z).getMaterial();
    	boolean flag = !material.isSolid();
    	if (!w.isAirBlock(x, y, z) && !flag){
    		return false;
    	}else{
    		if(getFiller(stack).canBePlacedInWorld())
    		w.setBlock(x, y, z, this.getFiller(stack).getBlock(), 0, 3);
    	}
    	return true;
    }
	
	public Fluid getFiller(ItemStack stack){
		if(stack == null) return null;
		if(stack.getItemDamage() < metadataFluids.size() && stack.getItemDamage() >= 0) return metadataFluids.get(stack.getItemDamage());
		return null;
	}
	
	public boolean isFilled(ItemStack stack){
		return getFiller(stack) != null;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void getSubItems(Item it, CreativeTabs tabs, List list){
    	for(int i = 0; i < metadataFluids.size(); ++i){
        	list.add(new ItemStack(it, 1, i));
    	}
     }
	
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer ply, World w, int x, int y, int z, int side, float hx, float hy, float hz){
		if(w.isRemote) return false;
		int x0 = x, y0 = y, z0 = z;
	     
	        switch(side){
	        case 0: y0--; break;
	        case 1: y0++; break;
	        case 2: z0--; break;
	        case 3: z0++; break;
	        case 4: x0--; break;
	        case 5: x0++; break;
	        default: return false;
	        }
	        if(!this.isFilled(is)){
	        	Fluid f = FluidRegistry.lookupFluidForBlock(w.getBlock(x0, y0, z0));
	        	if(f != null && byFluidMetadata.containsKey(f) && w.getBlockMetadata(x0, y0, z0) == 0 && w.getBlock(x0, y0, z0) != Blocks.air){
	        		int newCount = is.stackSize - 1;
	        		ItemStack is2 = new ItemStack(instance(), 1, byFluidMetadata.get(f));
	        		if(newCount > 0){
	        			ply.setCurrentItemOrArmor(0, new ItemStack(is.getItem(), newCount));
	        			if(!ply.inventory.addItemStackToInventory(is2)) ply.dropPlayerItemWithRandomChoice(is2, true);
	        		}else
	        			ply.setCurrentItemOrArmor(0, is2);
	        		w.setBlockToAir(x0, y0, z0);
	        		return true;
	        	}
	        	return false;
	        }
	        if((w.isAirBlock(x0, y0, z0) || (w.getBlock(x0, y0, z0) == this.getFiller(is).getBlock() && w.getBlockMetadata(x0, y0, z0) != 0))&& ply.canPlayerEdit(x0, y0, z0, side, is)){
	        	if(place(w, x0, y0, z0, is)){
	        		if(!ply.capabilities.isCreativeMode){
	        			int newCount = is.stackSize - 1;
		        		if(newCount > 0) ply.setCurrentItemOrArmor(0, new ItemStack(is.getItem(), newCount));
		        		else ply.setCurrentItemOrArmor(0, null);
		        		if(!ply.inventory.addItemStackToInventory(new ItemStack(instance()))) ply.dropPlayerItemWithRandomChoice(new ItemStack(instance()), true);
	        		}
	        		return true;
	        	}
	        }
		return false;
	}
}
