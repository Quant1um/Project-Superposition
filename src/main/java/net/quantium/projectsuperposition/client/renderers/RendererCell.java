package net.quantium.projectsuperposition.client.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
 

import net.minecraftforge.client.model.IModelCustom;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.client.models.ModelCellBase;
import net.quantium.projectsuperposition.client.models.ModelCellLiquid;
import net.quantium.projectsuperposition.items.ItemCell;

import org.lwjgl.opengl.GL11;
 
public class RendererCell implements IItemRenderer
{
    protected ModelCellBase baseModel;
    protected ModelCellLiquid liqModel;
    public static final ResourceLocation texture = new ResourceLocation(ModProvider.MODID, "textures/items/cell.png");
    
    public RendererCell(){
        baseModel = new ModelCellBase();
        liqModel = new ModelCellLiquid();
    }
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
       return true;
    }
    
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) 
    {
        return true;
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) 
    {
    	GL11.glPushMatrix();
	    if(type == ItemRenderType.INVENTORY)
	    	GL11.glRotatef(Minecraft.getSystemTime() / 20f, 0F, 1F, 0F);
        if(type != ItemRenderType.INVENTORY && type != ItemRenderType.ENTITY){
        	GL11.glTranslatef(0.7F, 1.1F, 0.7F);
        }else
        	GL11.glTranslatef(-0.1F, 0.3F, -0.1F);
        if(type == ItemRenderType.ENTITY)
            GL11.glTranslatef(0.0F, 0.4F, -0.0F);
        GL11.glScalef(2F, 2F, 2F);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        baseModel.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        if(item.getItem() instanceof ItemCell){
        	String s = getPathSafely(item);
        	if(!s.isEmpty()){
        		String[] splitted = s.split(":");
        		if(!s.contains(":"))
        		{
        			splitted = new String[2];
        			splitted[1] = s;
        			splitted[0] = "minecraft";
        		}
        		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(splitted[0],"textures/blocks/" + splitted[1] + ".png"));
        		liqModel.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        	}
        }
        GL11.glPopMatrix();
    }
   
    public static String getPathSafely(ItemStack stack){
    	if(stack == null) return "";
    	if(!ItemCell.instance().isFilled(stack)) return "";
    	IIcon icon = ItemCell.instance().getFiller(stack).getFlowingIcon();
    	if(icon == null) return "";
    	return icon.getIconName();
    }
}
