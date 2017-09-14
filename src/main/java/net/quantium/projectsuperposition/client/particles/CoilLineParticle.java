package net.quantium.projectsuperposition.client.particles;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.client.RenderHelper;
import net.quantium.projectsuperposition.utilities.vector.Vector3;
import net.quantium.projectsuperposition.utilities.vector.VectorFactory;
import net.quantium.projectsuperposition.utilities.vector.VectorUtils;

public class CoilLineParticle extends EntityFX {

	public double endX, endY, endZ;
	
	private static int LIGHTING_SEPARATIONS = 5;
	private Vector3[] offsets = new Vector3[LIGHTING_SEPARATIONS];
	
	public CoilLineParticle(World w, double x, double y, double z, double ex, double ey, double ez) {
		super(w, x, y, z);
		this.particleMaxAge = 10;
		
		this.endX = ex;
		this.endY = ey;
		this.endZ = ez;
		
		for(int i = 0; i < LIGHTING_SEPARATIONS; i++) 
			offsets[i] = new Vector3(VectorUtils.random(3, 0.5f, -0.25f));
	}
	
	@Override
	public void renderParticle(Tessellator t, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		//System.out.println("a");
		//GL11.glPushMatrix();
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
		//t.setBrightness(255);
		
		/*RenderHelper.setDefaultTexture();
		
		this.particleScale = 1;
		
		float lifepercentage = ((float)this.particleAge + par2) / (float)this.particleMaxAge * 32.0F;
		if(lifepercentage < 0) lifepercentage = 0;
		if(lifepercentage > 1) lifepercentage = 1;
		System.out.println(lifepercentage);
		//t.setColorOpaque(128, 128, 255);
		for(int i = 0; i < LIGHTING_SEPARATIONS; i++){
			float f = (i / (float)LIGHTING_SEPARATIONS);
			double xx = posX * (1 - f) + endX * f;
			double yy = posY * (1 - f) + endY * f;
			double zz = posZ * (1 - f) + endZ * f;
			t.addVertex(xx, yy, zz);
		}
		
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		//GL11.glPopMatrix();
		t.draw();
		t.startDrawingQuads();*/
		
		float f10 =  this.particleScale;
		double playerX = Minecraft.getMinecraft().thePlayer.prevPosX + (Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX) * par2;
		double playerY = Minecraft.getMinecraft().thePlayer.prevPosY + (Minecraft.getMinecraft().thePlayer.posY - Minecraft.getMinecraft().thePlayer.prevPosY) * par2;
		double playerZ = Minecraft.getMinecraft().thePlayer.prevPosZ + (Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * par2;
        t.draw();
        GL11.glLineWidth(3f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        t.startDrawing(GL11.GL_LINES);
		t.setBrightness(240);
		t.setColorRGBA(125, 200, 255, 128);
		Vector3 v = new Vector3(posX - playerX, posY - playerY, posZ - playerZ);
		Vector3 v2 = new Vector3(endX - playerX, endY - playerY, endZ - playerZ);
		
		for(int i = 0; i < LIGHTING_SEPARATIONS; i++) 
			offsets[i] = new Vector3(VectorUtils.random(3, 0.5f, -0.25f));
		for(int i = 0; i < LIGHTING_SEPARATIONS; i++){
			Vector3 vec = new Vector3(VectorFactory.create(v).lerpWith(v2, i / (float)LIGHTING_SEPARATIONS).add(i <= 0 ? VectorUtils.ZERO_VECTOR : offsets[i - 1]).toVector());
			Vector3 vec2 = new Vector3(VectorFactory.create(v).lerpWith(v2, (i + 1) / (float)LIGHTING_SEPARATIONS).add(offsets[i]).toVector());
			t.addVertex(vec.x(), vec.y(), vec.z());
			t.addVertex(vec2.x(), vec2.y(), vec2.z());
		}
	    t.draw();
	    t.startDrawingQuads();
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
