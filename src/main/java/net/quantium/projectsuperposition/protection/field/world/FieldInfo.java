package net.quantium.projectsuperposition.protection.field.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class FieldInfo {
	public IntegerVector3 main = new IntegerVector3();
	public IntegerVector3 extent = new IntegerVector3();
	public IntegerVector3 offset = new IntegerVector3();
	
	public FieldInfo(int x, int y, int z, int sx, int sy, int sz, int ox, int oy, int oz){
		this.main = new IntegerVector3(x, y, z);
		this.extent = new IntegerVector3(sx, sy, sz);
		this.offset = new IntegerVector3(ox, oy, oz);
	}
	
	public boolean intersects(FieldInfo fi2){
		return getBoundingBox().intersectsWith(fi2.getBoundingBox());
	}
	
	public boolean contains(IntegerVector3 vv){
		return getBoundingBox().isVecInside(Vec3.createVectorHelper(vv.x() + 0.5, vv.y() + 0.5, vv.z() + 0.5));
	}
	
	public AxisAlignedBB getBoundingBox(){
		return AxisAlignedBB.getBoundingBox(main.x() - extent.x() + offset.x(), 
											main.y() - extent.y() + offset.y(), 
											main.z() - extent.z() + offset.z(),
											main.x() + extent.x() + offset.x() + 1d, 
											main.y() + extent.y() + offset.y() + 1d, 
											main.z() + extent.z() + offset.z() + 1d);
	}
	
	public void clone(FieldInfo fi){
		this.main = fi.main;
		this.extent = fi.extent;
		this.offset = fi.offset;
	}
	
	@Override
	public int hashCode(){
		return main.hashCode() * 3631 + extent.hashCode() * 937 + offset.hashCode();
	}

	@Override
	public boolean equals(Object a){
		if(a instanceof FieldInfo){
			FieldInfo f = (FieldInfo)a;
			return f.main.equals(main) && f.extent.equals(extent) && f.offset.equals(offset);
		}
		return false;
	}
	
	public TileEntity getTileEntity(IBlockAccess access){
		return access.getTileEntity(main.x(), main.y(), main.z());
	}
}
