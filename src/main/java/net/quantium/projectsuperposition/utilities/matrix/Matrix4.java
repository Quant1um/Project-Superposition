package net.quantium.projectsuperposition.utilities.matrix;

public class Matrix4 implements IMatrix {

	private final float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;
	
	public Matrix4(){
		m00 = m01 = m02 = m03 = 
		m10 = m11 = m12 = m13 = 
		m20 = m21 = m22 = m23 = 
		m30 = m31 = m32 = m33 = 0;
	}
	
	public Matrix4(IMatrix m){
		m00 = MatrixUtils.getComponent(m, 0, 0);
		m01 = MatrixUtils.getComponent(m, 0, 1);
		m02 = MatrixUtils.getComponent(m, 0, 2);
		m03 = MatrixUtils.getComponent(m, 0, 3);
		m10 = MatrixUtils.getComponent(m, 1, 0);
		m11 = MatrixUtils.getComponent(m, 1, 1);
		m12 = MatrixUtils.getComponent(m, 1, 2);
		m13 = MatrixUtils.getComponent(m, 1, 3);
		m20 = MatrixUtils.getComponent(m, 2, 0);
		m21 = MatrixUtils.getComponent(m, 2, 1);
		m22 = MatrixUtils.getComponent(m, 2, 2);
		m23 = MatrixUtils.getComponent(m, 2, 3);
		m30 = MatrixUtils.getComponent(m, 3, 0);
		m31 = MatrixUtils.getComponent(m, 3, 1);
		m32 = MatrixUtils.getComponent(m, 3, 2);
		m33 = MatrixUtils.getComponent(m, 3, 3);
	}
	
	@Override
	public int width() {
		return 4;
	}

	@Override
	public int height() {
		return 4;
	}

	@Override
	public float[][] components() {
		return new float[][]{new float[]{m00, m01, m02, m03},
							 new float[]{m10, m11, m12, m13},
							 new float[]{m20, m21, m22, m23},
							 new float[]{m30, m31, m32, m33}};
	}

	public float getM00() {
		return m00;
	}

	public float getM01() {
		return m01;
	}

	public float getM02() {
		return m02;
	}

	public float getM03() {
		return m03;
	}

	public float getM10() {
		return m10;
	}

	public float getM11() {
		return m11;
	}

	public float getM12() {
		return m12;
	}

	public float getM13() {
		return m13;
	}

	public float getM20() {
		return m20;
	}

	public float getM21() {
		return m21;
	}

	public float getM22() {
		return m22;
	}

	public float getM23() {
		return m23;
	}

	public float getM30() {
		return m30;
	}

	public float getM31() {
		return m31;
	}

	public float getM32() {
		return m32;
	}

	public float getM33() {
		return m33;
	}

}
