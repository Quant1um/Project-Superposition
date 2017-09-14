package net.quantium.projectsuperposition.items;

import java.util.List;

import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemHeatableEnergyTool extends Item implements IElectricItemManager, IPowerable, IHeatPowerable{

	public static final int MAX_ENERGY = 6400;
	public static final int MAX_HEAT = 14600;
	
	/*@Override
	public void addInformation(ItemStack item, EntityPlayer ply, List l, boolean a) {
		
	}*/
	
	@Override
	public boolean canTransmit(ItemStack item) {
		return false;
	}

	@Override
	public int receive(ItemStack item, int energy) {
		int charge = current(item);
		int apl = energy;
		if(apl + charge > capacity(item)) apl = capacity(item) - charge;
		if(apl + charge < 0) apl = -charge;
		if(item.stackTagCompound == null) item.stackTagCompound = new NBTTagCompound();
		item.stackTagCompound.setInteger("energy", charge + apl);
		return energy - apl;
	}

	@Override
	public boolean transmit(ItemStack item, int energy) {
		return false;
	}

	@Override
	public int capacity(ItemStack item) {
		return MAX_ENERGY;
	}

	@Override
	public int current(ItemStack item) {
		if(item.stackTagCompound == null) return 0;
		return item.stackTagCompound.getInteger("energy");
	}

	@Override
	public double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		return receive(stack, (int)amount);
	}

	@Override
	public double discharge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally,
			boolean simulate) {
		return 0;
	}

	@Override
	public double getCharge(ItemStack stack) {
		return current(stack);
	}

	@Override
	public boolean canUse(ItemStack stack, double amount) {
		return false;
	}

	@Override
	public boolean use(ItemStack stack, double amount, EntityLivingBase entity) {
		return false;
	}

	@Override
	public void chargeFromArmor(ItemStack stack, EntityLivingBase entity) {}

	@Override
	public String getToolTip(ItemStack stack) {
		return "";
	}

	@Override
	public int receiveHeat(ItemStack item, int energy) {
		int charge = currentHeat(item);
		int apl = energy;
		if(apl + charge > capacityHeat(item)) apl = capacityHeat(item) - charge;
		if(apl + charge < 0) apl = -charge;
		if(item.stackTagCompound == null) item.stackTagCompound = new NBTTagCompound();
		item.stackTagCompound.setInteger("heat", charge + apl);
		return energy - apl;
	}

	@Override
	public int capacityHeat(ItemStack item) {
		return MAX_HEAT;
	}

	@Override
	public int currentHeat(ItemStack item) {
		if(item.stackTagCompound == null) return 0;
		return item.stackTagCompound.getInteger("heat");
	}
	
	@Override 
	public void onUpdate(ItemStack is, World w, Entity e, int a, boolean b){
		if(w.isRemote) return;
		if(current(is) > 0) receiveHeat(is, (5 + receive(is, -5)) * 3);
		else if(w.rand.nextInt(100) == 0) receiveHeat(is, -200);
	}
}
