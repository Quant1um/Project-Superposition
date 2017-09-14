package net.quantium.projectsuperposition.client.renderers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.client.models.ModelTeleport;
import net.quantium.projectsuperposition.tileentities.TileEntityTeleport;


public class RendererTeleport extends TileEntitySpecialRenderer {
    
    private final ModelTeleport modelbase;
	private ResourceLocation textures = new ResourceLocation(ModProvider.MODID, "textures/blocks/enderporter.png");
    public static RendererTeleport INSTANCE = new RendererTeleport();
    public RendererTeleport() {
    	this.modelbase = new ModelTeleport();        
    }
    
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float i) {
    	
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(textures);
        GL11.glRotatef(180F, 0F, 0F, 1.0F);
        this.modelbase.render((Entity)null, 0, 0, 0, 0, 0, 0.0625f);
        GL11.glPopMatrix();
    }
}
