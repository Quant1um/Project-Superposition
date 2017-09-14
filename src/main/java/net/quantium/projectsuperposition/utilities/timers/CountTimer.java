package net.quantium.projectsuperposition.utilities.timers;

public class CountTimer implements ITimer {

	private int counter;
	public final int elapsed;
	
	public CountTimer(int elapsed){
		this.elapsed = elapsed;
	}
	
	/**
	 * checks is counter completed and reset if completed
	 * @return is completed
	 */
	public boolean completed(){
		if(++counter >= elapsed){
			counter = 0;
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
	
	@Override
	public float time() {
		return counter;
	}

}
