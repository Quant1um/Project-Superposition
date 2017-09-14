package net.quantium.projectsuperposition.protection.turret.render;

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
import net.quantium.projectsuperposition.protection.turret.TileEntityTurret;
import net.quantium.projectsuperposition.utilities.Utils;


public class RendererTurret extends TileEntitySpecialRenderer {
    
    private final ModelTurret modelbase;
	private ResourceLocation textures = new ResourceLocation(ModProvider.MODID + ":textures/blocks/turret.png");
    public static RendererTurret INSTANCE = new RendererTurret();
    public RendererTurret() {
    	this.modelbase = new ModelTurret();        
    }
    
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
    	GL11.glPushMatrix();
    	GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        Minecraft.getMinecraft().renderEngine.bindTexture(textures);
        GL11.glRotatef(180F, 0F, 0F, 1.0F);
        GL11.glPushMatrix();
        int v = 0;
        if(te.hasWorldObj()){
            int meta = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
            if(meta < 1) v = 1;
            float rX = meta == 0 ? 180f : 0;
            GL11.glRotatef(rX, 1, 0, 0);
        }
        if(te instanceof TileEntityTurret){
        	TileEntityTurret t = (TileEntityTurret)te;
        	this.modelbase.render((Entity)null, t.angleX, t.angleY, v, 0.0F, 0.0F, 0.0625F);
        }else
        	this.modelbase.render((Entity)null, 0, 0, v, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

  

    
}
