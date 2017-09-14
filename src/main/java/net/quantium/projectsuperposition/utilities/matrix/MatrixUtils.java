package net.quantium.projectsuperposition.utilities.matrix;

import scala.actors.threadpool.Arrays;

public class MatrixUtils {
	
	public static final IMatrix ZERO_MATRIX = createMatrix(new float[0][0]);
	
	private static void checkMatrixDimensions(IMatrix... m){
		if(m.length > 1)
			for(int i = 1; i < m.length; i++)
				if(m[0].width() != m[i].width() || m[0].height() != m[i].height()) throw new RuntimeException("cannot operate with matrices having different dimensions count");
	}
	
	public static IMatrix add(IMatrix... m){
		int width = getMaxWidth(m);
		int height = getMaxHeight(m);
		float[][] components = new float[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				for(int k = 0; k < m.length; k++)
					components[i][j] += getComponent(m[k], i, j);
		return createMatrix(components);
	}
	
	public static IMatrix subtract(IMatrix base, IMatrix... subtractors){
		IMatrix[] matrices = new IMatrix[1 + subtractors.length];
		matrices[0] = base;
		for(int i = 0; i < subtractors.length; i++)
			matrices[i + 1] = negate(subtractors[i]);
		return add(matrices);
	}
	
	public static IMatrix multiply(IMatrix m, float v){
		float[][] components = new float[m.height()][m.width()];
		for(int i = 0; i < m.height(); i++)
			for(int j = 0; j < m.width(); j++)
				components[i][j] = getComponent(m, i, j) * v;
		return createMatrix(components);
	}
	
	public static IMatrix divide(IMatrix m, float v){
		return multiply(m, 1 / v);
	}
	
	public static IMatrix negate(IMatrix m){
		return multiply(m, -1);
	}
	
	public static IMatrix inner(IMatrix m, int i, int j, int h, int w){
		float[][] components = new float[h][w];
		for(int x = 0; x < h; x++)
			for(int y = 0; y < w; y++){
				components[x][y] = getComponent(m, x + i, y + j);
			}
		return createMatrix(components);
	}
	
	public static IMatrix fit(IMatrix m, int h, int w){
		return inner(m, 0, 0, h, w);
	}
	
	public static IMatrix transpose(IMatrix m){
		float[][] components = new float[m.width()][m.height()];
		for(int i = 0; i < m.width(); i++)
			for(int j = 0; j < m.height(); j++)
				components[i][j] = getComponent(m, j, i);
		return createMatrix(components);
	}
	
	public static IMatrix fit(IMatrix m, int s){
		return fit(m, s, s);
	}
	
	public static IMatrix identity(int s){
		float[][] components = new float[s][s];
		for(int i = 0; i < s; i++) components[i][i] = 1;
		return createMatrix(components);
	}
	
	public static IMatrix createMatrix(final float[][] components){
		for(int i = 1; i < components.length; i++)
			if(components[i].length != components[0].length) throw new RuntimeException("cannot create matrix having different columns count per row");
		return new IMatrix(){

			@Override
			public int width() {
				return components.length > 0 ? components[0].length : 0;
			}

			@Override
			public int height() {
				return components.length;
			}

			@Override
			public float[][] components() {
				return components.clone();
			}
		};
	}
	
	public static boolean isSquareMatrix(IMatrix m){
		return m.width() == m.height();
	}
	
	public static float determinant(IMatrix m){
		if(m.width() <= 0 || m.height() <= 0) return 0;
		if(!isSquareMatrix(m)) throw new RuntimeException("cannot compute determinant of non-square matrix");
	    if(m.width() == 1) return m.components()[0][0];
	    else if (m.width() == 2) return m.components()[0][0] * m.components()[1][1] - m.components()[0][1] * m.components()[1][0];
	    else{
	    	float det = 0;
	        for(int j = 0; j < m.width(); j++){
	        	float[][] a = new float[m.width() - 1][m.width() - 1];
	        	int b = 0;
	        	for(int k = 0; k < m.width(); k++){
                	if(k == j) continue;
    	            for(int i = 0; i < m.width() - 1; i++)
    	            	a[i][b] = m.components()[i + 1][k];
    	            b++;
	        	}
	            det -= powerOfMinusOne(j + 1) * m.components()[0][j] * determinant(createMatrix(a));
	      	}
	        return det;
		}
	}
	
	//TODO: add matrix inversion
	/*public static IMatrix[] lowerUpperDecomposition(IMatrix m){
		
	}
	
	public static IMatrix invert(IMatrix m){
		float det = determinant(m);
		if(det == 0) return m; //degenerate matrices aren't invertible
	}*/
	
	
	public static IMatrix multiply(IMatrix multiplicant, IMatrix multiplier){
		if(multiplicant.width() != multiplier.height()) throw new RuntimeException("cannot multiply matrices within different 1st width and 2nd height");
		float[][] components = new float[multiplicant.height()][multiplier.width()];
		for(int i = 0; i < multiplicant.height(); i++)
			for(int j = 0; j < multiplier.width(); j++)
				for(int k = 0; k < multiplicant.width(); k++)
						components[i][j] += multiplicant.components()[i][k] * multiplier.components()[k][j];
		return createMatrix(components);
	}
	
	//matrix multiplication test
	public static void main(String[] args){
		IMatrix m = createMatrix(new float[][]{new float[]{1, 2, 2, 3},
		      								   new float[]{1, 0, 0, 1},
		      								   new float[]{0, 3, 1, 0}});
		
		IMatrix m2 = createMatrix(new float[][]{new float[]{1, 2, 2},
			   								    new float[]{1, 0, 0},
			   								    new float[]{0, 3, 1},
			   								    new float[]{5, 6, 7}});
		
		m = multiply(m, m2);
		for(int j = 0; j < m.height(); j++){
			for(int i = 0; i < m.width(); i++){
				System.out.print(m.components()[j][i] + "\t");
			}
			System.out.println();
		}
			
	}
	
	private static float powerOfMinusOne(int power){
		if((power & 1) == 0) return 1;
		return -1;
	}
	
	public static int getMaxWidth(IMatrix... m){
		int max = 0;
		for(int i = 0; i < m.length; i++) max = Math.max(max, m[i].width());
		return max;
	}
	
	public static int getMaxHeight(IMatrix... m){
		int max = 0;
		for(int i = 0; i < m.length; i++) max = Math.max(max, m[i].height());
		return max;
	}
	
	public static float getComponent(IMatrix m, int i, int j){
		if(i < m.height() && i >= 0)
			if(j < m.width() && j >= 0) return m.components()[i][j];
		return 0;
	}
}
