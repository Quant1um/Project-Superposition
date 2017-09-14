package net.quantium.projectsuperposition.client;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher;

@Deprecated
public class RenderLastDispatcher {
	
	private List<RenderTask> tasks = new ArrayList<RenderTask>();
	
	private RenderLastDispatcher(){}
	public static final RenderLastDispatcher INSTANCE = new RenderLastDispatcher();
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onWorldLastRender(RenderWorldLastEvent e){
		for(int i = 0; i < tasks.size(); i++)
			tasks.get(i).render(e);
		tasks.clear();
	}
	
	public void addTask(RenderTask task){
		tasks.add(task);
	}
	
	public static void task(RenderTask task){
		INSTANCE.addTask(task);
	}
}
