package net.quantium.projectsuperposition.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.ModProvider;

public class ItemKyouko extends ItemFood {

	//yuru yuri reference
	public ItemKyouko() {
		super(6, 1f, false);
		this.setUnlocalizedName("tomatoKyouko");
	    this.setTextureName(ModProvider.MODID + ":kyouko");
	    this.setCreativeTab(null);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
	    super.onFoodEaten(stack, world, player);
	    //refreshing
	    player.clearActivePotions();
	    //hyperactive 
	    boolean flag = world.rand.nextInt(4) == 0;
	    if(flag) player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 1200, 0, false));
	    if(world.rand.nextInt(4) == 0) player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 1200, world.rand.nextInt(2), false));
	    if(world.rand.nextInt(3) == 0) player.addPotionEffect(new PotionEffect(Potion.jump.id, 1200, world.rand.nextInt(2), false));
	    if(world.rand.nextInt(2) == 0) player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1200, world.rand.nextInt(2), false));
	    //but weak
	    if(!flag && world.rand.nextInt(2) == 0) player.addPotionEffect(new PotionEffect(Potion.weakness.id, 1600, world.rand.nextInt(2) + 1, false));
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack){
		return EnumRarity.rare;
	}
}
