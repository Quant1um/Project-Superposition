package net.quantium.projectsuperposition.core.tickdispatcher;

import java.util.HashMap;

import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher.TickType;

public class Metadata{

	protected TickType type;
	protected HashMap<String, Object> data = new HashMap<String, Object>();
	
	public Metadata(TickType type){
		this.type = type;
	}
	
	public TickType getType() {
		return type;
	}

	public Object get(String id) {
		return data.get(id);
	}
	
	public void add(String id, Object obj){
		data.put(id, obj);
	}
	
}
