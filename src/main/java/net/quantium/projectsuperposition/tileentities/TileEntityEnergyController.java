package net.quantium.projectsuperposition.tileentities;

import java.util.List;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyAcceptor;
import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.debug.DebugEntry;
import net.quantium.projectsuperposition.debug.IDebuggable;

public class TileEntityEnergyController extends TileEntity implements IEnergyHandlerGC, IDebuggable{
	public BasicSink ic2EnergySink = new BasicSink(this, 10000, 3);
	
    @Override
    public void invalidate() {
        ic2EnergySink.invalidate();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        ic2EnergySink.onChunkUnload();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        ic2EnergySink.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        ic2EnergySink.writeToNBT(tag);
    }

    @Override
    public void updateEntity() {
        ic2EnergySink.updateEntity(); 
    }

	@Override
	public float receiveEnergyGC(EnergySource from, float amount, boolean simulate) {
		return (float) ic2EnergySink.injectEnergy(ForgeDirection.UP, amount * 5f, 256.0d);
	}

	@Override
	public float extractEnergyGC(EnergySource from, float amount, boolean simulate) {
		return 0;
	}

	@Override
	public boolean nodeAvailable(EnergySource from) {
		return true;
	}

	@Override
	public float getEnergyStoredGC(EnergySource from) {
		return (float) ic2EnergySink.getEnergyStored();
	}

	@Override
	public float getMaxEnergyStoredGC(EnergySource from) {
		return ic2EnergySink.getCapacity();
	}

	@Override
	public void debug(List<DebugEntry> list, DebugDrawer drawer, NBTTagCompound tag) {
		list.add(new DebugEntry(StatCollector.translateToLocal("info.energy"), tag.getInteger("energy") + "/10000"));
	}

	@Override
	public void capture(NBTTagCompound tag) {
		tag.setInteger("energy", Math.min((int)ic2EnergySink.getEnergyStored(), 10000));
	}
}
