package net.quantium.projectsuperposition.events;

import java.util.Iterator;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidRegistry.FluidRegisterEvent;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.items.ItemCell;

public class HandlerFluidRegistry {
	
	@SubscribeEvent
	public void onFluidRegistered(FluidRegisterEvent e){
    	if(FluidRegistry.getFluid(e.fluidID) != null)
    		ItemCell.registerFluid(FluidRegistry.getFluid(e.fluidID));
	}
}
