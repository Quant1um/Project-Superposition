package net.quantium.projectsuperposition.core.tickdispatcher;

public class TickTask {
	public final int ticks;
	public final TaskRunnable runnable;
	public final Metadata metadata;
	
	private int counter;
	
	public TickTask(int ticks, TaskRunnable runnable, Metadata metadata){
		this.ticks = ticks;
		this.runnable = runnable;
		this.metadata = metadata;
	}
	
	public boolean tick(){
		counter++;
		return counter >= ticks;
	}
}
