package net.quantium.projectsuperposition.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ListRenderer {
	
	public static void render(IListRendering renderer){
		renderer.prepare();
		if(renderer.redraw() || renderer.list() < 0) {
    		if(renderer.list() >= 0) GL11.glDeleteLists(renderer.list(), 1);
    		renderer.compile();
    		renderer.validateList();
    	}
		GL11.glCallList(renderer.list());
	}
}
