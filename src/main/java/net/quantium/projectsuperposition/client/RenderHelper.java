package net.quantium.projectsuperposition.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.core.tickdispatcher.Metadata;
import net.quantium.projectsuperposition.core.tickdispatcher.TaskRunnable;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher.TickType;

@SideOnly(Side.CLIENT)
public class RenderHelper {
	
	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(ModProvider.MODID, "textures/default.png");
	
	public static void start(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		GL11.glBegin(GL11.GL_TRIANGLES);
	}
	
	
	public static void end(){
		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public static void setDefaultTexture(){
		Minecraft.getMinecraft().renderEngine.bindTexture(DEFAULT_TEXTURE);
	}
	
	public static void color(int color){
		GL11.glColor4f(((color >> 16) & 0xff) / 255f, ((color >> 8) & 0xff) / 255f, (color & 0xff) / 255f, ((color >> 24) & 0xff) / 255f);
	}
	
	public static void drawTwoSidedPlane(float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3){
		
		GL11.glVertex3f(x0, y0, z0);
        GL11.glVertex3f(x1, y1, z1);
        GL11.glVertex3f(x2, y2, z2);
        
        GL11.glVertex3f(x0, y0, z0);
        GL11.glVertex3f(x3, y3, z3);
        GL11.glVertex3f(x2, y2, z2);
        
        GL11.glVertex3f(x2, y2, z2);
        GL11.glVertex3f(x1, y1, z1);
        GL11.glVertex3f(x0, y0, z0);
        
        GL11.glVertex3f(x2, y2, z2);
        GL11.glVertex3f(x3, y3, z3);
        GL11.glVertex3f(x0, y0, z0);
	}
	
	public static void drawAxisAlignedCube(float x0, float y0, float z0, float x1, float y1, float z1){
		drawTwoSidedPlane(x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0);
		drawTwoSidedPlane(x1, y0, z0, x1, y0, z1, x1, y1, z1, x1, y1, z0);
		
		drawTwoSidedPlane(x0, y0, z0, x0, y0, z1, x1, y0, z1, x1, y0, z0);
		drawTwoSidedPlane(x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0);
		
		drawTwoSidedPlane(x0, y0, z0, x1, y0, z0, x1, y1, z0, x0, y1, z0);
		drawTwoSidedPlane(x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1);
	}
	
	public static void drawAxisAlignedCubeWithoutBottom(float x0, float y0, float z0, float x1, float y1, float z1){
		drawTwoSidedPlane(x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0);
		drawTwoSidedPlane(x1, y0, z0, x1, y0, z1, x1, y1, z1, x1, y1, z0);
		
		drawTwoSidedPlane(x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0);
		
		drawTwoSidedPlane(x0, y0, z0, x1, y0, z0, x1, y1, z0, x0, y1, z0);
		drawTwoSidedPlane(x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1);
	}
}
