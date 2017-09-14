package net.quantium.projectsuperposition.utilities.vector;

public class IntegerVector3 implements IVector {

	protected final int x, y, z;
	
	public IntegerVector3(){
		this.x = this.y = this.z = 0;
	}
	
	public IntegerVector3(int v){
		this.x = this.y = this.z = v;
	}
	
	public IntegerVector3(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public IntegerVector3(IVector v){
		this.x = (int)VectorUtils.getComponent(v, 0);
		this.y = (int)VectorUtils.getComponent(v, 1);
		this.z = (int)VectorUtils.getComponent(v, 2);
	}
	
	@Override
	public int dimensions() {
		return 3;
	}

	@Override
	public double[] components() {
		return new double[]{x, y, z};
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public int z(){
		return z;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof IntegerVector3) return VectorUtils.equals(this, (IVector) o);
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
