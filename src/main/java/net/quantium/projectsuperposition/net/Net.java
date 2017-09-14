package net.quantium.projectsuperposition.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.world.World;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class Net<T extends INetNode> {
	
	private HashMap<IntegerVector3, T> table = new HashMap<IntegerVector3, T>();
	
	private GlobalNet<T> globalNet;
	
	public Net(GlobalNet<T> globalNet){
		this.globalNet = globalNet;
	}
	
	public T get(IntegerVector3 v){
		return table.get(v);
	}
	
	void add(IntegerVector3 v, T n){
		table.put(v, n);
	}
	
	void remove(IntegerVector3 v){
		table.remove(v);
	}
	
	public HashMap<IntegerVector3, T> get(){
		return table;
	}
	
	public List<T> getNodes(Class<? extends T> type){
		List<T> l = new ArrayList<T>();
		for (Entry<IntegerVector3, T> entry : table.entrySet())
			if(type.isInstance(entry.getKey())) l.add((T) entry.getKey());
		return l;
	}
}
