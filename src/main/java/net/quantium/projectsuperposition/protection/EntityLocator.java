package net.quantium.projectsuperposition.protection;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.core.raycast.Raycast;
import net.quantium.projectsuperposition.core.raycast.RaycastInfo;
import net.quantium.projectsuperposition.utilities.Utils;
import net.quantium.projectsuperposition.utilities.vector.Vector3;
import net.quantium.projectsuperposition.utilities.vector.VectorUtils;

public abstract class EntityLocator {
	public final boolean randomizeTargets;
	
	public final float maxDistance;
	public final RaycastInfo raycasting;
	public final LocatorTargetPart targetPart;
	
	public final Vector3 origin;
	
	public EntityLocator(World w, Vector3 origin, RaycastInfo raycasting, LocatorTargetPart targetPart, float maxDistance, boolean randomizeTargets){
		this.origin = origin;
		this.randomizeTargets = randomizeTargets;
		this.raycasting = raycasting;
		this.maxDistance = maxDistance;
		this.targetPart = targetPart;
		this.world = new WeakReference<World>(w);
	}
	
	private WeakReference<World> world;
	private WeakReference<EntityLivingBase> target;

	public World getWorld(){
		return world == null ? null : world.get();
	}
	
	private LocatorState state = LocatorState.INVALID;
	public LocatorState getState(){
		return state;
	}
	
	public EntityLivingBase get(){
		if(target == null) return null;
		return target.get();
	}
	
	public boolean isEntityReachable(EntityLivingBase e){
		if(e == null || getWorld() == null) return false;
		if(e.worldObj != getWorld()) return false;
		if(e.isDead) return false;
		if(!canTargetEntity(e)) return false;
		Vector3 entityPos = targetPart.getPosition(e, this);
		if(VectorUtils.distanceSq(entityPos, origin) > maxDistance * maxDistance) return false;
		if(raycasting != null && Raycast.trace(e.worldObj, origin, entityPos, raycasting).collided) return false;
		return true;
	}
	
	public boolean haveTarget(){
		return isEntityReachable(get());
	}
	
	public Vector3 getTargetPosition(){
		if(!haveTarget()) return Vector3.ZERO;
		return targetPart.getPosition(get(), this);
	}
	
	public void relocate(){
		if(getWorld() == null) return;
		if(!haveTarget()) target = null;
		if(target == null){
			state = LocatorState.LOCATING;
			List<EntityLivingBase> entities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(origin.x() - maxDistance, 
																																					origin.y() - maxDistance, 
																																					origin.z() - maxDistance, 
																																					origin.x() + maxDistance, 
																																					origin.y() + maxDistance, 
																																					origin.z() + maxDistance));
			
			List<EntityLivingBase> reachable = new ArrayList<EntityLivingBase>();
			
			for(int i = 0; i < entities.size(); i++){
				
				if(!isEntityReachable(entities.get(i))) continue;
				if(randomizeTargets) reachable.add(entities.get(i));
				else{
					target = new WeakReference<EntityLivingBase>(entities.get(i));
					state = LocatorState.TARGETTED;
					return;
				}
			}
			if(reachable.size() > 0){
				target = new WeakReference<EntityLivingBase>(reachable.get(Utils.RANDOM.nextInt(reachable.size())));
				state = LocatorState.TARGETTED;
			}
		}
		
	}
	
	public abstract boolean canTargetEntity(Entity e);
}
