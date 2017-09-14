package net.quantium.projectsuperposition.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class RendererHand implements IItemRenderer{
	public static final TileEntity INSTANCE = new TileEntity();
	public final TileEntitySpecialRenderer renderer;
	
	public RendererHand(TileEntitySpecialRenderer renderer){
		this.renderer = renderer;
	}
	
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}
	
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}
	
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		renderer.renderTileEntityAt(INSTANCE, 0.D, 0D, 0.0D, 0.0F);
	}

}
