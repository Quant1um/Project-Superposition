package net.quantium.projectsuperposition.protection.field;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.quantium.projectsuperposition.ModProvider;

public class ItemFieldCard extends Item {
	
	public ItemFieldCard(){
		this.setCreativeTab(ModProvider.TAB).setTextureName(ModProvider.MODID + ":fcard").setUnlocalizedName("fieldcard");
	}
	
	@Override
	public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List l, boolean p_77624_4_) {
		if(item.stackTagCompound != null){
			boolean flag = true;
			if(item.stackTagCompound.getBoolean("interact")){
				l.add(StatCollector.translateToLocal("field.interact"));
				flag = false;
			}
			if(item.stackTagCompound.getBoolean("enter")){
				l.add(StatCollector.translateToLocal("field.enter"));
				flag = false;
			}
			if(item.stackTagCompound.getBoolean("place")){
				l.add(StatCollector.translateToLocal("field.place"));
				flag = false;
			}
			if(item.stackTagCompound.getBoolean("destroy")){
				l.add(StatCollector.translateToLocal("field.destroy"));
				flag = false;
			}
			if(flag) l.add(StatCollector.translateToLocal("bio.empty"));
		}else{
			l.add(StatCollector.translateToLocal("bio.empty"));
		}
	}
}
