package net.quantium.projectsuperposition.utilities;

public abstract class Lazy<T> {
	public abstract T instantiate();
	
	private T value;
	public T get(){
		if(value == null) value = instantiate();
		return value;
	}
}
