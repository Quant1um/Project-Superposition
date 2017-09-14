package net.quantium.projectsuperposition.utilities.vector;

public class IntegerVector4 implements IVector {

	protected final int x, y, z, w;
	
	public IntegerVector4(){
		this.x = this.y = this.z = this.w = 0;
	}
	
	public IntegerVector4(int v){
		this.x = this.y = this.z = this.w = v;
	}
	
	public IntegerVector4(int x, int y, int z, int w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public IntegerVector4(IVector v){
		this.x = (int)VectorUtils.getComponent(v, 0);
		this.y = (int)VectorUtils.getComponent(v, 1);
		this.z = (int)VectorUtils.getComponent(v, 2);
		this.w = (int)VectorUtils.getComponent(v, 3);
	}
	
	@Override
	public int dimensions() {
		return 4;
	}

	@Override
	public double[] components() {
		return new double[]{x, y, z, w};
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
	
	public int w(){
		return w;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof IntegerVector4) return VectorUtils.equals(this, (IVector) o);
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
