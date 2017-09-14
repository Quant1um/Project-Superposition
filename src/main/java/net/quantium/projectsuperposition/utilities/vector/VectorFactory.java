package net.quantium.projectsuperposition.utilities.vector;

public class VectorFactory {
	
	private IVector value;
	private int expectedDimensions;
	
	private VectorFactory(IVector value){
		this(value, -1);
	}
	
	private VectorFactory(IVector value, int expected){
		this.value = value;
		this.expectedDimensions = expected;
		this.clampDimensions();
	}
	
	public static VectorFactory create(){
		return create(0);
	}
	
	public static VectorFactory create(IVector value){
		return create(value, value.dimensions());
	}
	
	public static VectorFactory create(int dim){
		return create(VectorUtils.ZERO_VECTOR, dim);
	}
	
	public static VectorFactory create(IVector value, int dim){
		return new VectorFactory(value, dim);
	}
	
	public IVector toVector(){
		clampDimensions();
		return value;
	}
	
	public VectorFactory add(IVector... vs){
		IVector[] vss = new IVector[1 + vs.length];
		vss[0] = value;
		for(int i = 0; i < vs.length; i++) vss[1 + i] = vs[i];
		value = VectorUtils.add(vss);
		return this;
	}
	
	public VectorFactory subtract(IVector... vs){
		value = VectorUtils.subtract(value, vs);
		return this;
	}
	
	public VectorFactory negate(){
		value = VectorUtils.negate(value);
		return this;
	}
	
	public VectorFactory invert(){
		value = VectorUtils.invert(value);
		return this;
	}
	
	public VectorFactory multiply(double f){
		value = VectorUtils.multiply(value, f);
		return this;
	}
	
	public VectorFactory divide(double f){
		value = VectorUtils.divide(value, f);
		return this;
	}
	
	public VectorFactory crossWith(IVector v){
		clampDimensions();
		value = VectorUtils.cross(value, v);
		return this;
	}
	
	public VectorFactory normalize(){
		clampDimensions();
		value = VectorUtils.normalize(value);
		return this;
	}
	
	private void clampDimensions(){
		if(expectedDimensions <= 0 || value.dimensions() == expectedDimensions) return;
		double[] cc = new double[expectedDimensions];
		for(int i = 0; i < cc.length; i++) cc[i] = VectorUtils.getComponent(value, i);
		value = VectorUtils.createVector(cc);
	}

	public VectorFactory lerpWith(Vector3 v2, float f) {
		value = VectorUtils.lerp(value, v2, f);
		return this;
	}
}
