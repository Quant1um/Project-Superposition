package net.quantium.projectsuperposition.utilities.timers;

public class ElapsedTimer implements ITimer{
	
	private long start = System.nanoTime();
	public final float cycle;
	
	public ElapsedTimer(float cycle){
		this.cycle = cycle;
	}
	
	/**
	 * checks is timer completed and reset if completed
	 * @return is completed
	 */
	public boolean completed(){
		float f = time();
		if(f > cycle){
			start = System.nanoTime();
			return true;
		}
		return false;
	}
	
	/**
	 * alias for completed();
	 */
	public void update(){
		completed();
	}
	
	public float time(){
		return (float)((System.nanoTime() - start) * MILLISECONDS_TO_NANOSECONDS);
	}
}

