package net.quantium.projectsuperposition.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.utilities.vector.VectorUtils;
import net.quantium.projectsuperposition.utilities.vector.WorldVector4;

public class ItemPortalLinker extends Item {
	
	public ItemPortalLinker(){
		this.setTextureName(ModProvider.MODID + ":plinker").setCreativeTab(ModProvider.TAB).setUnlocalizedName("portallinker");
	}
	
	@Override
	public void addInformation(ItemStack item, EntityPlayer p, List l, boolean a) {
		if(item.getTagCompound() != null && item.getTagCompound().hasKey("link") && item.getTagCompound().getCompoundTag("link") != null){
			l.add(StatCollector.translateToLocal("linker.linkInfo"));
			WorldVector4 d = new WorldVector4(VectorUtils.readFromNBT(item.getTagCompound().getCompoundTag("link")));
			l.add("X: " + d.x());
			l.add("Y: " + d.y());
			l.add("Z: " + d.z());
			l.add("D: " + d.dimension());
		}
	}
}
