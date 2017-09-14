package net.quantium.projectsuperposition.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterLogic extends Teleporter {

	private int dim;
	private double x, y, z;
	
	public TeleporterLogic(int dim, double x, double y, double z) {
		super(MinecraftServer.getServer().worldServerForDimension(dim));
		this.dim = dim;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
    public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        return false;
    }
 
    @Override
    public void removeStalePortalLocations(long par1) {}
 
    @Override
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {}
    
    public boolean makePortal(Entity par1Entity) 
    {
    	return false;
    }
    
    public void teleport(Entity e){
    	if(dim != e.worldObj.provider.dimensionId){
    		if(e instanceof EntityPlayerMP){
    			EntityPlayerMP mp = (EntityPlayerMP) e;
    			mp.mcServer.getConfigurationManager().transferPlayerToDimension(mp, dim, this);
    		}else e.travelToDimension(dim);
    	}
    	if(e instanceof EntityLivingBase) ((EntityLivingBase) e).setPositionAndUpdate(x, y, z);
		else e.setPosition(x, y, z);
    }
    
    public static void teleport(Entity e, int dim, double x, double y, double z){
    	new TeleporterLogic(dim, x, y, z).teleport(e);
    }
}
