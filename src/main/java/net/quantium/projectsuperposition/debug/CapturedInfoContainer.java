package net.quantium.projectsuperposition.debug;

import java.util.WeakHashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class CapturedInfoContainer {
	private CapturedInfoContainer(){}
	public static WeakHashMap<TileEntity, NBTTagCompound> info = new WeakHashMap<TileEntity, NBTTagCompound>();
}
