package net.quantium.projectsuperposition.protection.field.world;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.protection.field.TileEntityFieldGenerator;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class WorldFieldContainer extends WorldSavedData{
	public static final String DATA_ID = ModProvider.MODID + "_FieldContainer";

	public HashMap<ChunkCoordinates, List<WeakReference<FieldInfo>>> chunkMap = new HashMap<ChunkCoordinates, List<WeakReference<FieldInfo>>>();
	public HashMap<IntegerVector3, FieldInfo> map = new HashMap<IntegerVector3, FieldInfo>();
	
	public WorldFieldContainer() {
		super(DATA_ID);
	}
	public WorldFieldContainer(String s) {
		super(s);
	}
	
	public static WorldFieldContainer get(World world) {
		MapStorage storage = world.perWorldStorage;
		WorldFieldContainer instance = (WorldFieldContainer) storage.loadData(WorldFieldContainer.class, DATA_ID);
		if (instance == null) {
		    instance = new WorldFieldContainer();
		    storage.setData(DATA_ID, instance);
		}
		return instance;
	}
	
	public void addIfNotExistsAndUpdate(TileEntityFieldGenerator f){
		FieldInfo fi = new FieldInfo(f.xCoord, f.yCoord, f.zCoord, f.getSizeX(), f.getSizeY(), f.getSizeZ(), f.getOffsetX(), f.getOffsetY(), f.getOffsetZ());
		IntegerVector3 vv = new IntegerVector3(f.xCoord, f.yCoord, f.zCoord);
		boolean exists = map.get(vv) != null;
		if(!exists){
			System.out.println("addIfNotExistsAndUpdate(TileEntityFieldGenerator) - add: " + map.size());
			ChunkCoordinates cc = new ChunkCoordinates(f.xCoord >> 4, 0, f.zCoord >> 4);
			map.put(vv, fi);
			List<WeakReference<FieldInfo>> l = chunkMap.get(cc);
			if(l == null) l = new ArrayList<WeakReference<FieldInfo>>();
			l.add(new WeakReference<FieldInfo>(map.get(vv)));
			chunkMap.put(cc, l);
		}else{
			map.get(vv).extent = fi.extent;
			map.get(vv).offset = fi.offset;
		}
	}
	
	public List<FieldInfo> getFieldsOn(World w, IntegerVector3 vec){
		List<WeakReference<FieldInfo>> l = new ArrayList<WeakReference<FieldInfo>>();
		for(int i = -2; i <= 2; i++)
			for(int j = -2; j <= 2; j++){
				ChunkCoordinates c = new ChunkCoordinates((vec.x() >> 4) + i, 0, (vec.z() >> 4) + j);
				if(chunkMap.get(c) != null) l.addAll(chunkMap.get(c));
			}
		List<FieldInfo> l2 = new ArrayList<FieldInfo>();
		for(int i = 0; i < l.size(); i++){
			FieldInfo ii = l.get(i).get();
			if(ii != null){
				if(l2.contains(ii)){
					ChunkCoordinates c = new ChunkCoordinates(ii.main.x() >> 4, 0, ii.main.z() >> 4);
					if(chunkMap.get(c) != null){
						chunkMap.get(c).remove(ii);
					}
				}else{
					if(ii.getTileEntity(w) == null || !(ii.getTileEntity(w) instanceof TileEntityFieldGenerator)){
						map.remove(ii.main);
					}
					else if(((TileEntityFieldGenerator)ii.getTileEntity(w)).enabled() && ii.contains(vec))
						l2.add(ii);
				}
			}
		}
		return l2;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {}
}
