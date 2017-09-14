package net.quantium.projectsuperposition.net;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.quantium.projectsuperposition.ModProvider;

public class WorldNetContainer extends WorldSavedData{
	public static final String DATA_ID = ModProvider.MODID + "_NetContainer";

	private HashMap<Class<? extends INetNode>, GlobalNet> nets = new HashMap<Class<? extends INetNode>, GlobalNet>();
	
	public WorldNetContainer() {
		super(DATA_ID);
	}
	public WorldNetContainer(String s) {
		super(s);
	}
	
	public static WorldNetContainer get(World world) {
		MapStorage storage = world.perWorldStorage;
		WorldNetContainer instance = (WorldNetContainer) storage.loadData(WorldNetContainer.class, DATA_ID);
		if (instance == null) {
		    instance = new WorldNetContainer();
		    storage.setData(DATA_ID, instance);
		}
		return instance;
	}
	
	public GlobalNet getNet(World world, Class<? extends INetNode> type){
		if(nets.containsKey(type)) return nets.get(type);
		GlobalNet g = new GlobalNet(world);
		nets.put(type, g);
		return g;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {}
}
