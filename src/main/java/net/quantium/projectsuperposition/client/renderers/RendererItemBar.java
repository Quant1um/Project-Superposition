package net.quantium.projectsuperposition.client.renderers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.quantium.projectsuperposition.items.IHeatPowerable;
import net.quantium.projectsuperposition.items.IPowerable;

public class RendererItemBar implements IItemRenderer {
	private final RenderItem itemRenderer = new RenderItem();
	private final Minecraft mc = Minecraft.getMinecraft();
	
	private static List<Bar> bars = new ArrayList<Bar>();
	
	static{
		bars.add(new DamageBar());
		bars.add(new PowerableBar());
		bars.add(new HeatPowerableBar());
	}
	
	public static void addBarToRenderers(Bar bar){
		bars.add(bar);
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
			
		GL11.glColorMask(true, true, true, true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);

        Item item2 = item.getItem();
        int k = item.getItemDamage();
        float f;
        for (int l = 0; l < item2.getRenderPasses(k); ++l)
        {
        	OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            this.mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
            IIcon iicon = item2.getIcon(item, l);
            int i1 = item.getItem().getColorFromItemStack(item, l);
            f = (float)(i1 >> 16 & 255) / 255.0F;
            float f1 = (float)(i1 >> 8 & 255) / 255.0F;
            float f2 = (float)(i1 & 255) / 255.0F;
            GL11.glColor4f(f, f1, f2, 1.0F);
            itemRenderer.renderIcon(0, 0, iicon, 16, 16);
         }
       
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        int y = 13; 
        List<Bar> render = new ArrayList<Bar>();        
        for(int i = 0; i < bars.size(); i++){
        	Bar b = bars.get(i);
        	if(b.needToRender(item)){
        		y--;
        		render.add(b);
        	}
        }
        if(y < 13){
        	Tessellator.instance.startDrawingQuads();
        	drawQuad(Tessellator.instance, 2, y + 1, 13, 13 - y + 1, 0);
        	for(int i = 0; i < render.size(); i++){
        		Bar b = render.get(i);
        		drawQuad(Tessellator.instance, 2, 13 - i, (int) (12 * b.percentage(item)), 1, b.getColor(item));
        	}
        	Tessellator.instance.draw();
        }
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
       
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private void drawQuad(Tessellator tesselator, int x, int y, int w, int h, int color) {
        tesselator.setColorOpaque_I(color);
        tesselator.addVertex(x + 0, y + 0, 0);
        tesselator.addVertex(x + 0, y + h, 0);
        tesselator.addVertex(x + w, y + h, 0);
        tesselator.addVertex(x + w, y + 0, 0);  
    }
	
	public abstract static class Bar{
		public final int lowColor;
		public final int highColor;
		
		public Bar(int lowColor, int highColor){
			this.lowColor = lowColor;
			this.highColor = highColor;
		}
		
		public int getColor(ItemStack item){
			return lerpColor(lowColor, highColor, percentage(item));
		}
		
		public abstract int max(ItemStack item);
		public abstract int current(ItemStack item);
		
		public float percentage(ItemStack item){
			return current(item) / (float)max(item);
		}
		
		public abstract boolean needToRender(ItemStack item);
		
		public static int lerpColor(int a, int b, float t){
			int MASK1 = 0xff00ff; 
			int MASK2 = 0x00ff00; 

			int f2 = (int)(256 * t);
			int f1 = 256 - f2;

		    return   (((((a & MASK1) * f1) + ((b & MASK1) * f2)) >> 8) & MASK1) 
		           | (((((a & MASK2) * f1) + ((b & MASK2) * f2)) >> 8) & MASK2);
		}
	}
	
	public static class DamageBar extends Bar{

		public DamageBar() {
			super(0xff0000, 0x00ff00);
		}

		@Override
		public int max(ItemStack item) {
			return item.getMaxDamage();
		}

		@Override
		public int current(ItemStack item) {
			return item.getItemDamage();
		}

		@Override
		public boolean needToRender(ItemStack item) {
			return !item.getItem().getHasSubtypes() && item.getItemDamage() > 0 && item.getMaxDamage() > 0;
		}
		
	}
	
	public static class PowerableBar extends Bar{

		public PowerableBar() {
			super(0xff1100, 0x107fff);
		}

		@Override
		public int max(ItemStack item) {
			return ((IPowerable)item.getItem()).capacity(item);
		}

		@Override
		public int current(ItemStack item) {
			return ((IPowerable)item.getItem()).current(item);
		}

		@Override
		public boolean needToRender(ItemStack item) {
			return item.getItem() instanceof IPowerable && current(item) > 0;
		}
		
	}
	
	public static class HeatPowerableBar extends Bar{

		public HeatPowerableBar() {
			super(0x666666, 0xff8000);
		}

		@Override
		public int max(ItemStack item) {
			return ((IHeatPowerable)item.getItem()).capacityHeat(item);
		}

		@Override
		public int current(ItemStack item) {
			return ((IHeatPowerable)item.getItem()).currentHeat(item);
		}

		@Override
		public boolean needToRender(ItemStack item) {
			return item.getItem() instanceof IHeatPowerable && current(item) > 0;
		}
		
	}

}
