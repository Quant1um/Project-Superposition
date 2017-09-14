package net.quantium.projectsuperposition.protection.field.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.protection.field.TileEntityFieldGenerator;
import net.quantium.projectsuperposition.protection.turret.TileEntityTurret;
import net.quantium.projectsuperposition.utilities.Utils;


public class RendererField extends TileEntitySpecialRenderer {
    
    private final ModelField modelbase;
	private ResourceLocation textures = new ResourceLocation(ModProvider.MODID + ":textures/blocks/coil.png");
    public static RendererField INSTANCE = new RendererField();
    public RendererField() {
    	this.modelbase = new ModelField();        
    }
    
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(textures);
        GL11.glRotatef(180F, 0F, 0F, 1.0F);
        modelbase.render((Entity)null, 0f, 0f, 0f, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
