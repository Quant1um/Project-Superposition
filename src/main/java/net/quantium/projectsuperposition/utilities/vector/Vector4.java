package net.quantium.projectsuperposition.utilities.vector;

public class Vector4 implements IVector {
	public static final Vector4 ZERO = new Vector4();
	
	protected final double x, y, z, w;
	
	public Vector4(){
		this.x = this.y = this.z = this.w = 0;
	}
	
	public Vector4(double v){
		this.x = this.y = this.z = this.w = v;
	}
	
	public Vector4(double x, double y, double z, double w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4(IVector v){
		this.x = VectorUtils.getComponent(v, 0);
		this.y = VectorUtils.getComponent(v, 1);
		this.z = VectorUtils.getComponent(v, 2);
		this.w = VectorUtils.getComponent(v, 3);
	}
	
	@Override
	public int dimensions() {
		return 4;
	}

	@Override
	public double[] components() {
		return new double[]{x, y, z, w};
	}
	
	public double x(){
		return x;
	}
	
	public double y(){
		return y;
	}
	
	public double z(){
		return z;
	}
	
	public double w(){
		return w;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Vector4) return VectorUtils.equals(this, (IVector) o);
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
