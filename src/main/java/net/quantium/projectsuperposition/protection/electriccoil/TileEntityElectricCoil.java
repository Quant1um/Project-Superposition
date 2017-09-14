package net.quantium.projectsuperposition.protection.electriccoil;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.client.RenderHelper;
import net.quantium.projectsuperposition.core.raycast.RaycastInfo;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.debug.DebugEntry;
import net.quantium.projectsuperposition.debug.IDebuggable;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.protection.EnergyUtils;
import net.quantium.projectsuperposition.protection.EntityLocator;
import net.quantium.projectsuperposition.protection.IProtector;
import net.quantium.projectsuperposition.protection.LocatorState;
import net.quantium.projectsuperposition.protection.ProtectorPattern;
import net.quantium.projectsuperposition.protection.electriccoil.network.ParticleLineMessage;
import net.quantium.projectsuperposition.utilities.Lazy;
import net.quantium.projectsuperposition.utilities.Utils;
import net.quantium.projectsuperposition.utilities.timers.CountTimer;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;
import net.quantium.projectsuperposition.utilities.vector.Vector3;

public class TileEntityElectricCoil extends TileEntity implements IProtector, IDebuggable{
	
	private ProtectorPattern pattern = new ProtectorPattern();;
	private String owner;
	public static final int RANGE = 5;
	private final CountTimer counter = new CountTimer(10);
	private Lazy<EntityLocator> locator = new Lazy<EntityLocator>(){
		@Override
		public EntityLocator instantiate() {
			return new EntityLocator(worldObj, new Vector3(xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f), new RaycastInfo(new ArrayList<IntegerVector3>(){
				{
					add(new IntegerVector3(xCoord, yCoord, zCoord));
				}
			}), net.quantium.projectsuperposition.protection.LocatorTargetPart.NEAREST, RANGE, true){
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
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if(pattern == null) pattern = new ProtectorPattern();
		pattern.readFromNBT(tag);
		owner = tag.getString("owneruuid");
	}
	
	@Override
	public void updateEntity(){
		if(!enabled()) return;
		if(!EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, 1)) return;
		if(!counter.completed()) return;
		locator.get().relocate();
		if(locator.get().haveTarget()) applyTo(locator.get().get());
	}

	public void applyTo(EntityLivingBase e){
		if(!EnergyUtils.energyHandle(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, 150)) return;
		Vector3 v = locator.get().getTargetPosition();
		PacketDispatcher.sendToAllAround(new ParticleLineMessage(xCoord, yCoord, zCoord, v.x(), v.y(), v.z()), new TargetPoint(worldObj.provider.dimensionId, xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f, 64.0f));
		e.attackEntityFrom(DamageSource.generic, 7);
	}

	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@Override
	public void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag) {
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		GL11.glLineWidth(2f);
		
		RenderHelper.setDefaultTexture();
		Tessellator t = Tessellator.instance;
		t.startDrawing(GL11.GL_LINES);
		t.setBrightness(240);
		t.setColorOpaque(255, 124, 0);
		t.addVertex(tag.getDouble("xCoord"), tag.getDouble("yCoord"), tag.getDouble("zCoord"));
		t.addVertex(0, 0, 0);
		t.draw();
		
		LocatorState state = Utils.toEnum(LocatorState.class, tag.getInteger("locator"), LocatorState.INVALID);
		switch(state){
			case TARGETTED: list.add(new DebugEntry(StatCollector.translateToLocal("info.status"), StatCollector.translateToLocal("info.locator.status.targetted"), 0xff6666)); break;
			case LOCATING: list.add(new DebugEntry(StatCollector.translateToLocal("info.status"), StatCollector.translateToLocal("info.locator.status.locating"), 0xffaa55)); break;
			default: list.add(new DebugEntry(StatCollector.translateToLocal("info.status"), StatCollector.translateToLocal("info.locator.status.invalid"), 0x888888)); break;
		}
	
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
