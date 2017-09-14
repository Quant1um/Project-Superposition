package net.quantium.projectsuperposition.events;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.potion.Potion;
//Code by QuantiumAssembly
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.quantium.projectsuperposition.utilities.Utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;


public class HandlerOverlay extends Gui
{
	private Minecraft mc;
	public HandlerOverlay(Minecraft mc)
	{
		super();
		this.mc = mc;
	}

 

	@SubscribeEvent
	public void onWorldLastRender(RenderGameOverlayEvent event)
	{ 
		if(event.isCancelable() || event.type != ElementType.EXPERIENCE) return;
		MovingObjectPosition mop = mc.objectMouseOver;
		Tessellator t = Tessellator.instance;
		//t.startDrawingQuads();
		//t.setColorOpaque(255, 255, 0);
		//t.setBrightness(15728880);
		
	}
 
}