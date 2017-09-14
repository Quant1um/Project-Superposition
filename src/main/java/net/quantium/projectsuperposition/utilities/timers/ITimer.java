package net.quantium.projectsuperposition.utilities.timers;

public interface ITimer {
	static final double MILLISECONDS_TO_NANOSECONDS = 1 / 1000000f;
	
	/**
	 * @return time in milliseconds
	 */
	public float time();
}
