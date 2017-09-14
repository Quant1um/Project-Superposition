package net.quantium.projectsuperposition.api;

import java.util.HashMap;

public abstract class Registry<T>{
	private HashMap<String, T> registry = new HashMap<String, T>();
	
	public void register(String id, T o){
		if(registry.containsKey(id)) throw new RuntimeException("registry error: dublication on " + id);
		registry.put(id, o);
		onRegister(id, o);
	}
	
	public T get(String id){
		return registry.get(id);
	}
	
	public boolean contains(String key){
		return registry.containsKey(key);
	}
	
	public void clear(){
		registry.clear();
	}
	
	public abstract void onRegister(String s, T o);

	public boolean containsValue(T o) {
		return registry.containsValue(o);
	}
}
