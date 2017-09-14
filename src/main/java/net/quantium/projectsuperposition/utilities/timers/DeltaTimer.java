package net.quantium.projectsuperposition.utilities.timers;

public class DeltaTimer implements ITimer{
	private long start = System.nanoTime();
	
	/**
	 * resets the timer
	 * @return time in milliseconds
	 */
	public float cycle(){
		float f = time();
		start = System.nanoTime();
		return f;
	}
	
	public float time(){
		return (float)((System.nanoTime() - start) * MILLISECONDS_TO_NANOSECONDS);
	}
}
