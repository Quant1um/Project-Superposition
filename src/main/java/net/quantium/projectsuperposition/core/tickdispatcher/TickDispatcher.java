package net.quantium.projectsuperposition.core.tickdispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TickDispatcher {

	private HashMap<TickType, List<TickTask>> tasks = new HashMap<TickType, List<TickTask>>();
	
	private TickDispatcher(){}
	public static final TickDispatcher INSTANCE = new TickDispatcher();
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		tick(TickType.PLAYER, event);
	}
	 
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		tick(TickType.CLIENT, event);
	}
	 
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		tick(TickType.SERVER, event);
	}
	 
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
		tick(TickType.RENDER, event);
	}
	 
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		tick(TickType.WORLD, event);
	}
	
	public static final String META_EVENT_ID = "event";
	
	private void tick(TickType type, TickEvent event){
		List<TickTask> relatedTasks = tasks.get(type);
		if(relatedTasks != null){
			for(int i = 0; i < relatedTasks.size(); i++)
				if(relatedTasks.get(i).tick()){
					Metadata meta = relatedTasks.get(i).metadata;
					if(meta == null) meta = new Metadata(type);
					meta.add(META_EVENT_ID, event);
					relatedTasks.get(i).runnable.run(meta);
					relatedTasks.remove(i);
				}
		}
	}
	
	public void addTask(TaskRunnable run, int tick, Metadata meta){
		List<TickTask> relatedTasks = tasks.get(meta.type);
		if(relatedTasks == null) relatedTasks = new ArrayList<TickTask>();
		relatedTasks.add(new TickTask(tick, run, meta));
		tasks.put(meta.type, relatedTasks);
	}
	
	public void addTask(TaskRunnable run, int tick, TickType type){
		List<TickTask> relatedTasks = tasks.get(type);
		if(relatedTasks == null) relatedTasks = new ArrayList<TickTask>();
		relatedTasks.add(new TickTask(tick, run, null));
		tasks.put(type, relatedTasks);
	}
	
	public static void task(TaskRunnable run, int tick, Metadata meta){
		INSTANCE.addTask(run, tick, meta);
	}
	
	public static void task(TaskRunnable run, int tick, TickType type){
		INSTANCE.addTask(run, tick, type);
	}
	
	public enum TickType{
		PLAYER, CLIENT, SERVER, RENDER, WORLD
	}
}
