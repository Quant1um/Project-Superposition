package net.quantium.projectsuperposition.client;

import net.minecraftforge.client.event.RenderWorldLastEvent;

public abstract class RenderTask{
	public abstract void render(RenderWorldLastEvent e);
}
