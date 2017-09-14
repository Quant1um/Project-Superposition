package net.quantium.projectsuperposition.protection.turret;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.client.RenderHelper;
import net.quantium.projectsuperposition.core.raycast.RaycastInfo;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.debug.DebugEntry;
import net.quantium.projectsuperposition.debug.IDebuggable;
import net.quantium.projectsuperposition.protection.EnergyUtils;
import net.quantium.projectsuperposition.protection.EntityLocator;
import net.quantium.projectsuperposition.protection.IProtector;
import net.quantium.projectsuperposition.protection.LocatorState;
import net.quantium.projectsuperposition.protection.ProtectorPattern;
import net.quantium.projectsuperposition.utilities.Lazy;
import net.quantium.projectsuperposition.utilities.Utils;
import net.quantium.projectsuperposition.utilities.timers.CountTimer;
import net.quantium.projectsuperposition.utilities.timers.ElapsedTimer;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;
import net.quantium.projectsuperposition.utilities.vector.Vector3;

public class TileEntityTurret extends TileEntity implements IProtector, IDebuggable{
	
	private ProtectorPattern pattern = new ProtectorPattern();
	private String owner;
	public static final int RANGE = 20;
	public float angleX = 0, angleY = 0;
	private float targetAngleX, targetAngleY;
	
	private final CountTimer counter = new CountTimer(10);
	private final CountTimer idleCounter = new CountTimer(300);
	private static final ElapsedTimer syncTimer = new ElapsedTimer(100f);
	
	private Lazy<EntityLocator> locator = new Lazy<EntityLocator>(){
		@Override
		public EntityLocator instantiate() {
			return new EntityLocator(worldObj, new Vector3(xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f), new RaycastInfo(new ArrayList<IntegerVector3>(){
				{
					add(new IntegerVector3(xCoord, yCoord, zCoord));
				}
			}), net.quantium.projectsuperposition.protection.LocatorTargetPart.HEAD, RANGE, true){
				@Override
				public boolean canTargetEntity(Entity e) {
					return pattern.protectFrom(e);
				}
			};
		}
	};
	
	@Override
	public ProtectorPattern pattern() {
		return pattern;
	}

	@Override
	public String owner() {
		return owner;
	}

	@Override
	public boolean enabled() {
		return !worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if(pattern == null) pattern = new ProtectorPattern();
		pattern.writeToNBT(tag);
		tag.setString("owneruuid", owner);
		tag.setFloat("angleX", targetAngleX);
		tag.setFloat("angleY", targetAngleY);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if(pattern == null) pattern = new ProtectorPattern();
		pattern.readFromNBT(tag);
		owner = tag.getString("owneruuid");
		angleX = tag.getFloat("angleX");
    	angleY = tag.getFloat("angleY");
	}
	
	@Override
	public void updateEntity(){
		if(!enabled()) return;
		float dtheta = targetAngleX - angleX;
		if (dtheta > Math.PI) angleX += 2 * Math.PI;
		else if (dtheta < -Math.PI) angleX -= 2 * Math.PI;
		angleX += (targetAngleX - angleX) * 0.4f;
		angleY += (targetAngleY - angleY) * 0.2f;
		
		if(this.blockMetadata == 0 && !EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP, 1)) return;
		else if(this.blockMetadata > 0 && !EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, 1)) return;
		if(locator.get().haveTarget()){
			Vector3 pos = locator.get().getTargetPosition();
			float startX = (float) Math.atan2(-xCoord - 0.5f + pos.x(), zCoord + 0.5f - pos.z());
			float startY = (float) Math.atan2(yCoord + 0.5f - pos.y(), getHorizontalDistance(pos));
			setTargetAngles(startX, startY);
		}else{
			if(idleCounter.completed())
				setTargetAngles((float)(Utils.RANDOM.nextDouble() * Math.PI - Math.PI * 0.5), (float)(Utils.RANDOM.nextGaussian() * Math.PI * 0.2f));
		}
		if(!counter.completed()) return;
		locator.get().relocate();
		if(locator.get().haveTarget()) apply();
	}

	public void apply(){
		if(worldObj.isRemote) return;
		if(this.blockMetadata == 0 && !EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UP, 70)) return;
		else if(this.blockMetadata > 0 && !EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, 70)) return;		
		EntityBullet ee = new EntityBullet(worldObj, xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f);
		ee.rotationPitch = angleX * 180f / (float)Math.PI;
		ee.rotationYaw = angleY * 180f / (float)Math.PI;
		targetAngleY -= Math.random() * 0.07f;
		double tspeed = 1.2;
		ee.motionX = (double)-(-MathHelper.sin(angleX) * MathHelper.cos(angleY)) * tspeed;
        ee.motionZ = (double)-(MathHelper.cos(angleX) * MathHelper.cos(angleY)) * tspeed;
        ee.motionY = (double)(-MathHelper.sin(angleY)) * tspeed;
		worldObj.spawnEntityInWorld(ee);
		worldObj.playSoundEffect(xCoord + 0.5d, yCoord + 0.5d, zCoord + 0.5d, "random.pop", 1.0f, 1.1f);
		//e.attackEntityFrom(DamageSource.generic, 4);
	}

	public void setTargetAngles(float x, float y){
		targetAngleX = x;
		targetAngleY = y;
		
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}
	
	public void addTargetAngles(float x, float y){
		setTargetAngles(targetAngleX + x, targetAngleY + y);
	}
	
	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	  @Override
	    public Packet getDescriptionPacket() {
	    	NBTTagCompound tagCompound = new NBTTagCompound();
	    	tagCompound.setFloat("angleX", angleX);
	    	tagCompound.setFloat("angleY", angleY);
	    	return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tagCompound);
	    }
	    
	    
	    @Override
	    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
	    	NBTTagCompound tag = pkt.func_148857_g();
	    	targetAngleX = tag.getFloat("angleX");
	    	targetAngleY = tag.getFloat("angleY");
	    }
	    
	    private float getHorizontalDistance(Vector3 pos){
	    	double dx = xCoord + 0.5f - pos.x();
	    	double dz = zCoord + 0.5f - pos.z();
	    	return MathHelper.sqrt_float((float) (dx * dx + dz * dz));
	    }

		@Override
		public void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag) {
			GL11.glDepthFunc(GL11.GL_ALWAYS);
			GL11.glLineWidth(2f);
			
			RenderHelper.setDefaultTexture();
			Tessellator t = Tessellator.instance;
			t.startDrawing(GL11.GL_LINES);
			t.setColorOpaque(127, 255, 0);
			t.setBrightness(240);
			float mod = 0.1f * (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 0 ? 1 : -1);
			t.addVertex(MathHelper.sin(angleX) * MathHelper.cos(angleY), mod - Math.sin(angleY), -Math.cos(angleX) * MathHelper.cos(angleY));
			t.addVertex(0, 0.1f * mod, 0);
			t.setColorOpaque(255, 124, 0);
			t.addVertex(tag.getDouble("xCoord"), tag.getDouble("yCoord") + mod, tag.getDouble("zCoord"));
			t.addVertex(0, 0.1f * mod, 0);
			t.draw();
			
			LocatorState state = Utils.toEnum(LocatorState.class, tag.getInteger("locator"), LocatorState.INVALID);
			switch(state){
				case TARGETTED: list.add(new DebugEntry(StatCollector.translateToLocal("info.status"), StatCollector.translateToLocal("info.locator.status.targetted"), 0xff6666)); break;
				case LOCATING: list.add(new DebugEntry(StatCollector.translateToLocal("info.status"), StatCollector.translateToLocal("info.locator.status.locating"), 0xffaa55)); break;
				default: list.add(new DebugEntry(StatCollector.translateToLocal("info.status"), StatCollector.translateToLocal("info.locator.status.invalid"), 0x888888)); break;
			}
			float daX = (float) (angleX * 180f / Math.PI);
			float daY = (float) (angleY * 180f / Math.PI);
			list.add(new DebugEntry(StatCollector.translateToLocal("info.turret.facing"), Utils.toTitleCase(Utils.getDirection(daY, daX).name())));
			list.add(new DebugEntry(StatCollector.translateToLocal("info.turret.yaw"), String.format("%.2f", daX)));
			list.add(new DebugEntry(StatCollector.translateToLocal("info.turret.pitch"), String.format("%.2f", daY)));
		
			if(!tag.getBoolean("enabled")) list.add(new DebugEntry(null, StatCollector.translateToLocal("info.disabled"), 0xff5555));
		}

		@Override
		public void capture(NBTTagCompound tag) {
			tag.setInteger("locator", locator.get().getState().ordinal());
			tag.setBoolean("enabled", enabled());
			if(locator.get().haveTarget()){
				Vector3 vec = locator.get().getTargetPosition();
				tag.setDouble("xCoord", vec.x() - xCoord - 0.5f);
				tag.setDouble("yCoord", vec.y() - yCoord - 0.5f);
				tag.setDouble("zCoord", vec.z() - zCoord - 0.5f);
			}
		}
}
