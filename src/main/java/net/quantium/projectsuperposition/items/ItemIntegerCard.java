package net.quantium.projectsuperposition.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.quantium.projectsuperposition.ModProvider;

public class ItemIntegerCard extends Item {
	
	public ItemIntegerCard(){
		this.setCreativeTab(ModProvider.TAB).setTextureName(ModProvider.MODID + ":sizecard").setUnlocalizedName("sizecard");
	}
	
	@Override
	public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List l, boolean p_77624_4_) {
		if(item.stackTagCompound != null){
			if(item.stackTagCompound.getInteger("value") != 0){
				l.add(StatCollector.translateToLocal("card.value") + item.stackTagCompound.getInteger("value"));
			}else l.add(StatCollector.translateToLocal("bio.empty"));
		}else{
			l.add(StatCollector.translateToLocal("bio.empty"));
		}
	}
}
