package net.quantium.projectsuperposition.utilities.vector;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.quantium.projectsuperposition.utilities.Utils;

public final class VectorUtils {
	
	public static final IVector ZERO_VECTOR = createVector(new double[0]);
	
	private static void checkVectorDimensions(IVector... v){
		if(v.length > 1)
			for(int i = 1; i < v.length; i++)
				if(v[0].dimensions() != v[i].dimensions()) throw new RuntimeException("cannot operate with vectors having different dimensions count");
	}
	
	public static double lengthSq(IVector v0){
		return dot(v0, v0);
	}
	
	public static double length(IVector v0){
		return (double) Math.sqrt(lengthSq(v0));
	}
	
	public static double distanceSq(IVector v0, IVector v1){
		return lengthSq(subtract(v0, v1));
	}
	
	public static double distance(IVector v0, IVector v1){
		return length(subtract(v0, v1));
	}
	
	public static double dot(IVector v0, IVector v1){
		double dot = 0;
		checkVectorDimensions(v0, v1);
		for(int i = 0; i < v0.dimensions(); i++) dot += v0.components()[i] * v1.components()[i];
		return dot;
	}
	
	public static IVector cross(IVector v0, IVector v1){
		checkVectorDimensions(v0, v1);
		if(v0.dimensions() != 3) throw new RuntimeException("cannot compute cross product of vectors having dimensions different than 3");
	    double ax = v0.components()[0];
		double ay = v0.components()[1];
		double az = v0.components()[2];
		double bx = v0.components()[0];
		double by = v0.components()[1];
		double bz = v0.components()[2];
		return createVector(ay * bz - az * by, az * bx - ax * bz, ax * by - ay * bx);
	}
	
	public static IVector add(IVector... v){
		int maxDim = getMaxDimensions(v);
		double[] components = new double[maxDim];
		for(int i = 0; i < maxDim; i++)
			for(int j = 0; j < v.length; j++)
				components[i] += getComponent(v[j], i);
		return createVector(components);
	}
	
	public static IVector subtract(IVector base, IVector... subtractors){
		IVector[] vectors = new IVector[1 + subtractors.length];
		vectors[0] = base;
		for(int i = 0; i < subtractors.length; i++)
			vectors[i + 1] = negate(subtractors[i]);
		return add(vectors);
	}
	
	public static IVector multiply(IVector v, double s){
		double[] components = new double[v.dimensions()];
		for(int i = 0; i < v.dimensions(); i++)
			components[i] = v.components()[i] * s; 
		return createVector(components);
	}
	
	public static IVector divide(IVector v, double s){
		return multiply(v, 1 / s);
	}
	
	public static IVector negate(IVector v){
		return multiply(v, -1);
	}
	
	public static IVector invert(IVector v){
		double[] components = new double[v.dimensions()];
		for(int i = 0; i < v.dimensions(); i++)
			components[i] = 1 / v.components()[i]; 
		return createVector(components);
	}
	
	public static IVector normalize(IVector v){
		double len = length(v);
		if(len == 0) return v;
		return divide(v, len);
	}
	
	public static IVector lerp(IVector v0, IVector v1, double transition){
		return add(multiply(v0, 1 - transition), multiply(v1, transition));
	}
	
	public static IVector average(IVector... v){
		if(v.length <= 0) return ZERO_VECTOR;
		return divide(add(v), v.length);
	}
	
	public static boolean equals(IVector v0, IVector v1, double epsilon){
		if(v0.dimensions() != v1.dimensions()) return false;
		for(int i = 0; i < v0.dimensions(); i++)
			if(Math.abs(v0.components()[i] - v1.components()[i]) > epsilon) return false;
		return true;
	}
	
	private static final double DEFAULT_EPSILON = 5e-15f;
	
	public static boolean equals(IVector v0, IVector v1){
		return equals(v0, v1, DEFAULT_EPSILON);
	}
	
	public static String toString(IVector v){
		return Arrays.toString(v.components());
	}
	
	public static int hashCode(IVector v){
		long hCode = 0;
		for(int i = 0; i < v.dimensions(); i++)
			hCode = (hCode * 397) ^ Double.doubleToLongBits(v.components()[i]);
		return (int)hCode;
	}
	
	public static IVector createVector(final double... components){
		return new IVector(){
			@Override
			public int dimensions() {
				return components.length;
			}
			
			@Override
			public double[] components() {
				return components.clone();
			}
		};
	}
	
	public static IVector readFromNBT(NBTTagCompound tag){
		if(tag == null) return ZERO_VECTOR;
		int d = tag.getInteger("dim");
		double[] components = new double[d];
		for(int i = 0; i < d; i++)
			components[i] = tag.getDouble("d" + i);
		return createVector(components);
	}
	
	public static void writeToNBT(IVector v, NBTTagCompound tag){
		if(tag == null) return;
		tag.setInteger("dim", v.dimensions());
		for(int i = 0; i < v.dimensions(); i++)
			tag.setDouble("d" + i, v.components()[i]);
	}
	
	public static double getComponent(IVector v, int c){
		if(c < v.dimensions() && c >= 0) return v.components()[c];
		return 0;
	}
	
	public static int getMaxDimensions(IVector... v){
		int maxDim = 0;
		for(int i = 0; i < v.length; i++) maxDim = Math.max(maxDim, v[i].dimensions());
		return maxDim;
	}

	public static IVector unit(int dim, int c) {
		double[] components = new double[dim];
		components[c] = 1;
		return createVector(components);
	}

	public static IVector random(int i, float f, float g) {
		double[] components = new double[i];
		for(int j = 0; j < i; j++) components[j] = Utils.RANDOM.nextDouble() * f + g; 
		return createVector(components);
	}
	
	public static IVector gaussian(int i, float f, float g) {
		double[] components = new double[i];
		for(int j = 0; j < i; j++) components[i] = Utils.RANDOM.nextGaussian() * f + g; 
		return createVector(components);
	}
}
