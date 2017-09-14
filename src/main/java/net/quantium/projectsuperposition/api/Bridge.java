package net.quantium.projectsuperposition.api;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.quantium.projectsuperposition.api.recipes.IModRecipeRegistry;
import net.quantium.projectsuperposition.api.recipes.soldering.ISolderingRecipe;
import net.quantium.projectsuperposition.api.recipes.welding.IWeldingRecipe;

public class Bridge {
	public static final String MOD_ID = "projectsuperposition";
	
	private static Map<String, WeakReference> objects = new WeakHashMap<String, WeakReference>();
	
	public static boolean isModLoaded(){
		return Loader.isModLoaded(MOD_ID);
	}
	
	public static Object getAsObject(String id){
		WeakReference ref = objects.get(id);
		if(ref == null) return null;
		return ref.get();
	}
	
	public static boolean containsKey(String id){
		return objects.containsKey(id);
	}
	
	public static boolean containsValue(String id){
		return objects.containsValue(id);
	}
	
	public static void add(String id, Object obj){
		if(objects.containsKey(id)) throw new RuntimeException("Can\'t add " + id + " to bridge. Already exists!");
		objects.put(id, new WeakReference(obj));
	}
	
	public static Class getType(String id){
		Object obj = getAsObject(id);
		if(obj == null) return null;
		return getAsObject(id).getClass();
	}
	
	public static void print(){
		Iterator<Map.Entry<String, WeakReference>> it = objects.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, WeakReference> pair = it.next();
		    if(pair.getValue() != null){
		    	Object o = pair.getValue().get();
		    	if(o != null) System.out.println(pair.getKey() + " = (" + o.getClass().getName() + ") " + o.toString());
		    }
		}
	}
	
	public static Registry<Item> items(){
		return (Registry<Item>) getAsObject(ITEMS);
	}
	
	public static Registry<Block> blocks(){
		return (Registry<Block>) getAsObject(BLOCKS);
	}
	
	public static Registry<Class<? extends TileEntity>> tileentities(){
		return (Registry<Class<? extends TileEntity>>) getAsObject(TILE_ENTITIES);
	}
	
	public static IModRecipeRegistry<ISolderingRecipe> soldering(){
		return (IModRecipeRegistry<ISolderingRecipe>) getAsObject(SOLDERING_RECIPES);
	}
	
	public static IModRecipeRegistry<IWeldingRecipe> welding(){
		return (IModRecipeRegistry<IWeldingRecipe>) getAsObject(WELDING_RECIPES);
	}
	
	public static final String ITEMS = "items";
	public static final String BLOCKS = "blocks";
	public static final String TILE_ENTITIES = "tileEntities";
	public static final String VERSION = "version";
	public static final String MODID = "modid";
	public static final String TAB = "tab";
	public static final String SOLDERING_RECIPES = "solderingRecipes";
	public static final String WELDING_RECIPES = "weldingRecipes";
}
