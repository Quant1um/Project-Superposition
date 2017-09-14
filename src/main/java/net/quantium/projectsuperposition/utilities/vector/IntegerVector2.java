package net.quantium.projectsuperposition.utilities.vector;

public class IntegerVector2 implements IVector {

	protected final int x, y;
	
	public IntegerVector2(){
		this.x = this.y = 0;
	}
	
	public IntegerVector2(int v){
		this.x = this.y = v;
	}
	
	public IntegerVector2(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public IntegerVector2(IVector v){
		this.x = (int)VectorUtils.getComponent(v, 0);
		this.y = (int)VectorUtils.getComponent(v, 1);
	}
	
	@Override
	public int dimensions() {
		return 2;
	}

	@Override
	public double[] components() {
		return new double[]{x, y};
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	
	@Override
	public boolean equals(Object o){
		if(o instanceof IntegerVector2) return VectorUtils.equals(this, (IVector) o);
		return false;
	}
	
	private final int hashCode = VectorUtils.hashCode(this);
	@Override
	public int hashCode(){
		return hashCode;
	}
	
	@Override
	public String toString(){
		return VectorUtils.toString(this);
	}
}
