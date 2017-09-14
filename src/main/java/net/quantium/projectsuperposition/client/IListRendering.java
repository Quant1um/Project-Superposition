package net.quantium.projectsuperposition.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IListRendering {
	public boolean redraw();
	public int list();
	public void compile();
	public void validateList();
	public void prepare();
}
