package net.quantium.projectsuperposition.utilities.vector;

public class Vector2 implements IVector {
	public static final Vector2 ZERO = new Vector2();
	
	protected final double x, y;
	
	public Vector2(){
		this.x = this.y = 0;
	}
	
	public Vector2(double v){
		this.x = this.y = v;
	}
	
	public Vector2(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2(IVector v){
		this.x = VectorUtils.getComponent(v, 0);
		this.y = VectorUtils.getComponent(v, 1);
	}
	
	@Override
	public int dimensions() {
		return 2;
	}

	@Override
	public double[] components() {
		return new double[]{x, y};
	}
	
	public double x(){
		return x;
	}
	
	public double y(){
		return y;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Vector2) return VectorUtils.equals(this, (IVector) o);
		return false;
	}
	
	@Override
	public int hashCode(){
		return VectorUtils.hashCode(this);
	}
	
	@Override
	public String toString(){
		return VectorUtils.toString(this);
	}
}
