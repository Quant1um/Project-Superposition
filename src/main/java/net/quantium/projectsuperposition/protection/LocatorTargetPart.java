package net.quantium.projectsuperposition.protection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.quantium.projectsuperposition.utilities.vector.Vector3;

public enum LocatorTargetPart {
	HEAD, LEGS, CENTER, NEAREST;
	
	public Vector3 getPosition(EntityLivingBase e, EntityLocator context){
		switch(this){
		case CENTER: return new Vector3(e.posX, e.posY + e.getEyeHeight() / 2, e.posZ);
		case HEAD:  return new Vector3(e.posX, e.posY + e.getEyeHeight(), e.posZ);
		case NEAREST: return new Vector3(e.posX, MathHelper.clamp_double(context.origin.y(), e.posY, e.posY + e.getEyeHeight()), e.posZ);
		default: return new Vector3(e.posX, e.posY, e.posZ);
		}
	}
}
