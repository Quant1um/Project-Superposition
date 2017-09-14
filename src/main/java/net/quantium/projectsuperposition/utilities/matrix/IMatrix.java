package net.quantium.projectsuperposition.utilities.matrix;

/**
 * Immutable Matrix Interface
 * @author Quant1um
 */
public interface IMatrix {
	
	public int width();
	public int height();
	public float[][] components();
}
