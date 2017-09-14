package net.quantium.projectsuperposition.debug;

import java.util.List;
import net.minecraft.nbt.NBTTagCompound;

public interface IDebuggable {
	/**
	 * Invoked when player hovers over this tile entity with screwdriver, on client side.
	 * @param list add to this entries
	 * @param drawer drawing context
	 * @param tag captured data
	 */
	void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag);
	
	/**
	 * Invoked when player requests data to use in debug(List<DebugEntry>, DebugDrawer, NBTTagCompound) function, on server side.
	 * @param tag tag to save data
	 */
	void capture(NBTTagCompound tag);
}
