package net.quantium.projectsuperposition.events;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.quantium.projectsuperposition.client.IAlphaRendering;

public class HandlerAlphaRender {

	@SideOnly(Side.CLIENT)
	private DistanceFromPlayerComparator comparator = new DistanceFromPlayerComparator();
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onWorldLastRender(RenderWorldLastEvent e){
		List<TileEntity> list = new ArrayList<TileEntity>();
		double playerX = Minecraft.getMinecraft().thePlayer.prevPosX + (Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX) * e.partialTicks;
		double playerY = Minecraft.getMinecraft().thePlayer.prevPosY + (Minecraft.getMinecraft().thePlayer.posY - Minecraft.getMinecraft().thePlayer.prevPosY) * e.partialTicks;
		double playerZ = Minecraft.getMinecraft().thePlayer.prevPosZ + (Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * e.partialTicks;
		comparator.setPlayerCoords(playerX, playerY, playerZ);
		for(int i = 0; i < e.context.tileEntities.size(); i++)
			if(e.context.tileEntities.get(i) instanceof IAlphaRendering) list.add((TileEntity) e.context.tileEntities.get(i));
		list.sort(comparator);
		for(int i = 0; i < list.size(); i++){
			GL11.glPushMatrix();
			GL11.glTranslated(list.get(i).xCoord + 0.5 - playerX, list.get(i).yCoord + 0.5 - playerY, list.get(i).zCoord + 0.5 - playerZ);
			((IAlphaRendering)list.get(i)).renderInSecondPass();
			GL11.glPopMatrix();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public class DistanceFromPlayerComparator implements Comparator<TileEntity> {
		private double playerX, playerY, playerZ;
		
		@Override
		public int compare(TileEntity a, TileEntity b) {
			double d0 = a.getDistanceFrom(playerX, playerY, playerZ);
			double d1 = b.getDistanceFrom(playerX, playerY, playerZ);
			return d0 > d1 ? -1 : 1;
		}
		
		public void setPlayerCoords(double playerX, double playerY, double playerZ){
			this.playerX = playerX;
			this.playerY = playerY;
			this.playerZ = playerZ;
		}
	}
}


