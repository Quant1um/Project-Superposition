package net.quantium.projectsuperposition.protection;

import java.util.UUID;

public interface IProtector {
	
	public ProtectorPattern pattern();
	public String owner();
	void setOwner(String owner);
	public boolean enabled();
}
