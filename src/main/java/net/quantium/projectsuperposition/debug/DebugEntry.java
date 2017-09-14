package net.quantium.projectsuperposition.debug;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class DebugEntry {
	public String name;
	public Object value;
	public int color;
	
	public DebugEntry(String name, Object value){
		this(name, value, 0xaaaaaa);
	}
	
	public DebugEntry(String name, Object value, int color){
		this.name = name;
		this.value = value;
		this.color = color;
	}
	
	@Override
	public String toString(){
		String value2 = value == null ? StatCollector.translateToLocal("info.undefined") : value.toString();
		if(name == null) return value2;
		return name + ": " + value2;
	}
}
