package net.quantium.projectsuperposition.core.raycast;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.utilities.TupleBlockMetadata;
import net.quantium.projectsuperposition.utilities.Utils;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;
import net.quantium.projectsuperposition.utilities.vector.Vector3;
import net.quantium.projectsuperposition.utilities.vector.VectorUtils;

public class Raycast {
	public static final float RAY_STEP = 0.125f;
	
	public static RaycastHit trace(World w, Vector3 start, Vector3 end, RaycastInfo info){
		RaycastHit hit = new RaycastHit();
		Cache cache = new Cache();
		double d = VectorUtils.distance(start, end);
		for(float f = 0; f < d; f += RAY_STEP){
			hit.distance = f;
			if(!checkBlockPassable(w, new Vector3(VectorUtils.lerp(start, end, f / d)), hit, info, cache)) break;
		}
		return hit; 
	}
	
	private static boolean checkBlockPassable(World w, Vector3 v, RaycastHit hit, RaycastInfo info, Cache cache){
		if(w == null) return false;
		
		IntegerVector3 iv = new IntegerVector3(Utils.doubleToIntBlock(v.x()), Utils.doubleToIntBlock(v.y()), Utils.doubleToIntBlock(v.z()));
		if(iv.equals(cache.position) && cache.isOpaque) return true;
		cache.position = iv;
		cache.isOpaque = true;
		
		for(int i = 0; i < info.ignorePositions.size(); i++) if(iv.equals(info.ignorePositions.get(i))) return true;
		
		TupleBlockMetadata tuple = new TupleBlockMetadata(w.getBlock(iv.x(), iv.y(), iv.z()), w.getBlockMetadata(iv.x(), iv.y(), iv.z()));
		Block b = tuple.block;
		cache.isOpaque = b.isOpaqueCube() || info.collideNonOpaqueAsOpaque;
		
		if(!b.canCollideCheck(tuple.metadata, false)) return true;
		for(int i = 0; i < info.ignoreBlocks.size(); i++) if(tuple.equals(info.ignoreBlocks.get(i))) return true;
		
		boolean flag = false;
		if(!cache.isOpaque) flag = canPassThroughBlockNonOpaque(w, v, tuple, iv);
		if(!flag){
			hit.collisionPoint = v;
			hit.collisionPointBlock = iv;
			hit.collidedBlock = tuple;
			hit.collided = true;
		}
		return flag;
	}
	
	private static class Cache{
		private IntegerVector3 position;
		private boolean isOpaque;
	}
	
	private static boolean canPassThroughBlockNonOpaque(World w, final Vector3 v, TupleBlockMetadata tuple, IntegerVector3 iv){
		List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
		tuple.block.addCollisionBoxesToList(w, iv.x(), iv.y(), iv.z(), tuple.block.getCollisionBoundingBoxFromPool(w, iv.x(), iv.y(), iv.z()), boxes, new Entity(w){
			{
				this.posX = v.x();
				this.posY = v.y();
				this.posZ = v.z();
			}
			@Override
			protected void entityInit() {}
			@Override
			protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {}
			@Override
			protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {}
		});
		for(int i = 0; i < boxes.size(); i++) if(boxes.get(i).isVecInside(v.toVec3())) return false;
		return true;
	}
	
	private static boolean isInAABB(Vector3 point, AxisAlignedBB aabb){
		return aabb.isVecInside(Vec3.createVectorHelper(point.x(), point.y(), point.z()));
	}
}
