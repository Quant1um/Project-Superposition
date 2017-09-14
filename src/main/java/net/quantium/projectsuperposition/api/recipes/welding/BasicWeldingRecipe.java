package net.quantium.projectsuperposition.api.recipes.welding;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.quantium.projectsuperposition.api.Bridge;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.api.Registry;

public class BasicWeldingRecipe implements IWeldingRecipe {

	public IInventory matrix;
	public ItemStack craft;
	public int solenoids;
	private int width, height;
	public BasicWeldingRecipe(ItemStack craft, int solenoids, ItemStack[] matrix, int width, int height){
		this.width = width;
		this.height = height;
		this.matrix = new InventoryBasic(null, false, width * height);
		for(int i = 0; i < matrix.length; i++)
			this.matrix.setInventorySlotContents(i, matrix[i]);
		this.craft = craft;
		this.solenoids = solenoids;
	}
	
	
	@Override
	public ItemStack getCraftItem(IInventory matrix) {
		if(compareMatrices(matrix, this.matrix, solenoids, getWidth(), getHeight())) return craft;
		return null;
	}

	public static boolean compareMatrices(IInventory haveMatrix, IInventory matrix, int solenoids, int w, int h) {
		if(haveMatrix.getSizeInventory() < 18) return false;
		int haveSolenoids = haveMatrix.getStackInSlot(17) != null && haveMatrix.getStackInSlot(17).getItem() == ((Registry)Bridge.getAsObject("items")).get(ObjectNames.ITEM_SOLENOID) ? haveMatrix.getStackInSlot(17).stackSize : 0;
		if(haveSolenoids < solenoids) return false;
		
		for (int i = 0; i <= 5 - w; ++i)
            for (int j = 0; j <= 3 - h; ++j){
                if (checkMatch(haveMatrix, i, j, true, w, h, matrix)) return true;
                if (checkMatch(haveMatrix, i, j, false, w, h, matrix)) return true;
        
            }
		return false;
	}

	private static boolean checkMatch(IInventory matrix, int i, int j, boolean d, int w, int h, IInventory mn)
    {
        for (int k = 0; k < 5; ++k)
            for (int l = 0; l < 3; ++l){
                int i1 = k - i;
                int j1 = l - j;
                ItemStack itemstack = null;
                if (i1 >= 0 && j1 >= 0 && i1 < w && j1 < h){
                    if (d) itemstack = mn.getStackInSlot(w - i1 - 1 + j1 * w);
                    else itemstack = mn.getStackInSlot(i1 + j1 * w);
                }
                ItemStack itemstack1 = matrix.getStackInSlot(k + l * 5);
                if (itemstack1 != null || itemstack != null){
                    if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null) return false;
                    if (itemstack.getItem() != itemstack1.getItem()) return false;
                    if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage()) return false; 
                }
            }
        return true;
    }
	
	public static boolean compareStacks(ItemStack stack1, ItemStack stack2){
		return stack1 == null ? (stack2 == null ? true : false) : (stack2 == null ? false : (stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage()));
	}
	
	public static void reduce(IInventory haveMatrix, IInventory matrix, int solenoids, int width, int height){
		for(int i = 0; i < 15; i++) 
			haveMatrix.decrStackSize(i, 1);
		if(solenoids > 0) haveMatrix.decrStackSize(17, solenoids);
	}
	
	public static BasicWeldingRecipe create(ItemStack out, int solenoids, Object... matrix){
		String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (matrix[i] instanceof String[])
        {
            String[] astring = (String[])((String[])matrix[i++]);

            for (int l = 0; l < astring.length; ++l)
            {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }
        else
        {
            while (matrix[i] instanceof String)
            {
                String s2 = (String)matrix[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < matrix.length; i += 2)
        {
            Character character = (Character)matrix[i];
            ItemStack itemstack1 = null;

            if (matrix[i + 1] instanceof Item)
            {
                itemstack1 = new ItemStack((Item)matrix[i + 1]);
            }
            else if (matrix[i + 1] instanceof Block)
            {
                itemstack1 = new ItemStack((Block)matrix[i + 1], 1, 32767);
            }
            else if (matrix[i + 1] instanceof ItemStack)
            {
                itemstack1 = (ItemStack)matrix[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1)
        {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0)))
            {
                aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
            }
            else
            {
                aitemstack[i1] = null;
            }
        }
        return new BasicWeldingRecipe(out, solenoids, aitemstack, j, k);
	}
	
	@Override
	public void onCraft(ItemStack item, IInventory matrix) {
		reduce(matrix, this.matrix, solenoids, getWidth(), getHeight());
	}

	@Override
	public IInventory getDesiredMatrix() {
		return this.matrix;
	}

	@Override
	public int getSolenoids() {
		return this.solenoids;
	}


	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}
