package net.quantium.projectsuperposition.utilities.vector;

import net.minecraft.util.Vec3;

public class Vector3 implements IVector {
	public static final Vector3 ZERO = new Vector3();
	
	protected final double x, y, z;
	
	public Vector3(){
		this.x = this.y = this.z = 0;
	}
	
	public Vector3(double v){
		this.x = this.y = this.z = v;
	}
	
	public Vector3(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(IVector v){
		this.x = VectorUtils.getComponent(v, 0);
		this.y = VectorUtils.getComponent(v, 1);
		this.z = VectorUtils.getComponent(v, 2);
	}
	
	public Vector3(Vec3 vec){
		this.x = vec.xCoord;
		this.y = vec.yCoord;
		this.z = vec.zCoord;
	}

	@Override
	public int dimensions() {
		return 3;
	}

	@Override
	public double[] components() {
		return new double[]{x, y, z};
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
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Vector3) return VectorUtils.equals(this, (IVector) o);
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

	public Vec3 toVec3() {
		return Vec3.createVectorHelper(x, y, z);
	}
}
