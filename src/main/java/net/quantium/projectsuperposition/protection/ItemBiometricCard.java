package net.quantium.projectsuperposition.protection;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.quantium.projectsuperposition.ModProvider;

public class ItemBiometricCard extends Item implements IProtectionCard{

	@Override
	public boolean protectFrom(Entity e, ItemStack item) {
		if(item.stackTagCompound != null){
			switch (item.stackTagCompound.getInteger("protect")) {
			case 1:
				return e instanceof EntityPlayer;
			case 2:
				return e instanceof EntityMob;
			case 3:
				return e instanceof EntityAnimal;
			case 4:
				return e instanceof EntityLiving;
			case 5:
				if(item.stackTagCompound.hasKey("name")){
					return e instanceof EntityPlayer && ((EntityPlayer) e).getCommandSenderName().toString() == item.stackTagCompound.getString("name");
				}
			default:
				break;
			}
		}
		return false;
	}

	@Override
	public boolean accessTo(Entity e, ItemStack item) {
		if(item.stackTagCompound != null){
			switch (item.stackTagCompound.getInteger("protect")) {
			case 6:
				return e instanceof EntityPlayer;
			case 7:
				return e instanceof EntityMob;
			case 8:
				return e instanceof EntityAnimal;
			case 9:
				return e instanceof EntityLiving;
			case 10:
				if(item.stackTagCompound.hasKey("name")){
					return e instanceof EntityPlayer && ((EntityPlayer) e).getCommandSenderName().toString() == item.stackTagCompound.getString("name");
				}
			default:
				break;
			}
		}
		return false;
	}

	public ItemBiometricCard(){
		this.setCreativeTab(ModProvider.TAB).setTextureName(ModProvider.MODID + ":bmcard").setUnlocalizedName("biometriccard");
	}
	
	@Override
	public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List l, boolean p_77624_4_) {
		if(item.stackTagCompound != null){
			int i = item.stackTagCompound.getInteger("protect");
			if(i >= 1 && i <= 5) l.add(EnumChatFormatting.RED + StatCollector.translateToLocal("bio.protect"));
			else if(i >= 6 && i <= 10) l.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("bio.access"));
			switch(i){
				case 1: 
					l.add(StatCollector.translateToLocal("bio.anyplayer")); break;
				case 2: 
					l.add(StatCollector.translateToLocal("bio.mob")); break;
				case 3: 
					l.add(StatCollector.translateToLocal("bio.animal")); break;
				case 4: 
					l.add(StatCollector.translateToLocal("bio.living")); break;
				case 5: 
					l.add(StatCollector.translateToLocal("bio.player"));
					l.add(formatPlayerName(item.stackTagCompound.getString("name"))); break;
				case 6: 
					l.add(StatCollector.translateToLocal("bio.anyplayer")); break;
				case 7: 
					l.add(StatCollector.translateToLocal("bio.mob")); break;
				case 8: 
					l.add(StatCollector.translateToLocal("bio.animal")); break;
				case 9: 
					l.add(StatCollector.translateToLocal("bio.living")); break;
				case 10: 
					l.add(StatCollector.translateToLocal("bio.player")); 
					l.add(formatPlayerName(item.stackTagCompound.getString("name"))); break;
				case 0:
					l.add(StatCollector.translateToLocal("bio.empty")); break;
				default:
					l.add(EnumChatFormatting.RED + StatCollector.translateToLocal("bio.malformed"));
			}
		}else{
			l.add(StatCollector.translateToLocal("bio.empty"));
		}
	}
	
	public static String formatPlayerName(String name){
		if(name != null && !name.isEmpty())
			return name;
		return EnumChatFormatting.RED + StatCollector.translateToLocal("bio.notset");
	}
}
