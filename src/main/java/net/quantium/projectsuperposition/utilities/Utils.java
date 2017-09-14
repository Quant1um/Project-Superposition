package net.quantium.projectsuperposition.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class Utils {
	/**
	 * Global random
	 */
	public static final Random RANDOM = new Random();
	
	/**
	 * Adds itemstacks to inventory
	 */
	public static boolean[] addToInv(IInventory inv, ItemStack... itemstacks)
	{
		boolean[] val = new boolean[itemstacks.length];
		for (int i = 0; i < itemstacks.length; i++)
		{
			val[i] = addToInv(inv, itemstacks[i]);
		}
		return val;
	}
	
	/**
	 * Transforms double coord into block integer coord
	 */
	public static int doubleToIntBlock(double f){
		if(f > 0) return (int)f;
		return (int)(f - 1);
	}
	
	/**
	 * Clamps input to range 0 - 1
	 */
	public static float clamp01(float v){
		return MathHelper.clamp_float(v, 0, 1);
	}
	
	/**
	 * @return The square of input
	 */
	public static float square(float v){
		return v * v;
	}
	
	
	/**
	 * Elastic easing with default parameters(s = 0.2, l = 0.6)
	 */
	public static float easeInElastic(float t) {
		return easeInElastic(t, 0.2f, 0.5f);
	}
	/**
	 * Elastic easing
	 */
	public static float easeInElastic(float t, float s, float l) {
		t = clamp01(t);
		//(x/0.5)^2*(x<0.5)+(sin((x-0.5)*pi/0.1)*0.1*e*(1-x)+1)*(x>0.5)
		//http://fooplot.com/#W3sidHlwZSI6MCwiZXEiOiIoeC8wLjUpXjIqKHg8MC41KSsoc2luKCh4LTAuNSkqcGkvMC4xKSowLjEqZSooMS14KSsxKSooeD4wLjUpIiwiY29sb3IiOiIjMDAwMDAwIn0seyJ0eXBlIjoxMDAwLCJ3aW5kb3ciOlsiLTExLjg5NzMzNTg1NzE5MTg4IiwiMTIuODk3MzM1ODU3MTkxNjciLCItNi42Mjc5MjI2NDM4NzAwNzUiLCI4LjYyNzkyMjY0Mzg3MDAyNyJdfV0-
		if(t < l) return square(t / l);
		return MathHelper.sin((t - l) * (float)Math.PI / s) * s * (float)Math.E * (1 - t) + 1;
	}
	
	/**
	 * Adds itemstack to inventory
	 * @author https://github.com/Fir3will/ExtremeBlocks/tree/master/java/main
	 */
	public static boolean addToInv(IInventory inv, ItemStack par1ItemStack)
	{
		List<ItemStack> inventorySlots = new ArrayList<ItemStack>();
		boolean flag1 = false;
		int k = 0;
		ItemStack itemstack1 = null;
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			inventorySlots.add(inv.getStackInSlot(i));
		}
		if (par1ItemStack == null) return false;
		if (par1ItemStack.isStackable())
		{
			while (par1ItemStack.stackSize > 0 && k < inv.getSizeInventory())
			{
				itemstack1 = inventorySlots.get(k);
				if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1))
				{
					int l = itemstack1.stackSize + par1ItemStack.stackSize;
					if (l <= par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.stackSize = 0;
						itemstack1.stackSize = l;
						inv.markDirty();
						flag1 = true;
					}
					else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = par1ItemStack.getMaxStackSize();
						inv.markDirty();
						flag1 = true;
					}
				}
				++k;
			}
		}
		if (par1ItemStack.stackSize > 0)
		{
			k = 0;
			while (k < inv.getSizeInventory())
			{
				itemstack1 = inventorySlots.get(k);
				if (itemstack1 == null)
				{
					inv.setInventorySlotContents(k, par1ItemStack.copy());
					inv.markDirty();
					par1ItemStack.stackSize = 0;
					flag1 = true;
					break;
				}
				++k;
			}
		}
		if (itemstack1 != null && itemstack1.stackSize == 0)
		{
			itemstack1 = null;
		}
		if (par1ItemStack != null && par1ItemStack.stackSize == 0)
		{
			par1ItemStack = null;
		}
		return flag1;
	}
	
	/**
	 * Create rotation metadata using entity's yaw
	 * @param e - entity
	 * @param s - separations (must be power of 2)
	 * @return metadata
	 */
	public static int getMetaFromYaw(Entity e, int s)
	{
		return MathHelper.floor_double((double)(e.rotationYaw * s / 360.0F) + 2.5D) & (s - 1);
	}
	
	public static ForgeDirection getDirection(float pitch, float yaw){
		if(pitch < -45f) return ForgeDirection.UP;
		if(pitch > 45f) return ForgeDirection.DOWN;
		yaw %= 180;
		if(yaw < -90) return ForgeDirection.SOUTH;
		if(yaw < 0) return ForgeDirection.WEST;
		if(yaw < 90) return ForgeDirection.NORTH;
		return ForgeDirection.EAST;
	}
	
	/**
	 * Drops all items from inventory
	 */
	public static void dropInventory(World w, int x, int y, int z, IInventory ii){
		for(int i = 0; i < ii.getSizeInventory(); i++) {
			ItemStack itemstack = ii.getStackInSlot(i);
			if(itemstack != null) {
				float f = w.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = w.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = w.rand.nextFloat() * 0.8F + 0.1F;

				while(itemstack.stackSize > 0) {
					int j = w.rand.nextInt(21) + 10;

					if(j > itemstack.stackSize) {
						j = itemstack.stackSize;
					}
					itemstack.stackSize -= j;
					EntityItem item = new EntityItem(w, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
					if(itemstack.hasTagCompound()) {
						item.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
					}
					w.spawnEntityInWorld(item);
				}	
			}
		}
	}
	
	/**
	 * Splits integer into approximately equal parts
	 * @author https://stackoverflow.com/questions/32542807/java-divide-number-into-equals-parts-or-approximate-to-each-other | Andreas
	 */
	public static int[] splitIntoParts(int whole, int parts) {
	    int[] arr = new int[parts];
	    for (int i = 0; i < arr.length; i++)
	        whole -= arr[i] = (whole + parts - i - 1) / (parts - i);
	    return arr;
	}
	
	public static final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
    															// to be capitalized
	
	/**
	 * @author https://stackoverflow.com/questions/1086123/string-conversion-to-title-case | scottb
	 */
	public static String toTitleCase(String s) {
	    StringBuilder sb = new StringBuilder();
	    boolean capNext = true;

	    for (char c : s.toCharArray()) {
	        c = (capNext)
	                ? Character.toUpperCase(c)
	                : Character.toLowerCase(c);
	        sb.append(c);
	        capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
	    }
	    return sb.toString();
	}
	
	public static <T extends Enum> T toEnum(Class<T> cl, int v, T def){
		T[] vs = cl.getEnumConstants();
		if(v < 0 || v >= vs.length) return def;
		return vs[v];
	}

	public static String getEntityName(Entity entity) {
		if(entity == null) return null;
		if(entity instanceof EntityLiving){
			if(entity instanceof EntityPlayer) return ((EntityPlayer) entity).getDisplayName();
			//return ((EntityLiving) entity).getCustomNameTag();
		}
		return entity.getClass().getSimpleName();
	}
}
