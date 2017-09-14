package net.quantium.projectsuperposition.client.renderers;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.proxies.ClientProxy;

@Deprecated
public class RendererCorner implements ISimpleBlockRenderingHandler{

	public static final ResourceLocation texture = new ResourceLocation(ModProvider.MODID, "textures/blocks/fayalitecorner.png");
	
	private static IIcon icon;
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		
		//Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		//renderer.renderStandardBlock(Blocks.bookshelf, x, y + 1, z);
		if(icon == null) icon = ModProvider.BLOCKS.get(ObjectNames.BLOCK_PLATED_HULL).getIcon(0, 0);
		
		int state = world.getBlockMetadata(x, y, z) & 7;
		
		renderBasic(x, y, z, 1);
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.cornerRenderingId;
	}

	public void renderBasic(int x, int y, int z, int state){
		Tessellator t = Tessellator.instance;
		
		boolean nY = (state & 4) != 0;
		/*boolean nXP = (state & 3) == 3;
		boolean nXN = (state & 3) == 0;
		boolean nZP = (state & 3) == 1;
		boolean nZN = (state & 3) == 2;*/
		
		
		if(nY){
			t.addVertexWithUV(x + 1, y + 1, z + 1, icon.getMaxU(), icon.getMinV());
			t.addVertexWithUV(x + 1, y + 1, z, icon.getMaxU(), icon.getMaxV());
			t.addVertexWithUV(x, y + 1, z, icon.getMinU(), icon.getMaxV());
			t.addVertexWithUV(x, y + 1, z + 1, icon.getMinU(), icon.getMinV());
		}else{
			t.addVertexWithUV(x + 1, y, z, icon.getMaxU(), icon.getMinV());
			t.addVertexWithUV(x + 1, y, z + 1, icon.getMaxU(), icon.getMaxV());
			t.addVertexWithUV(x, y, z + 1, icon.getMinU(), icon.getMaxV());
			t.addVertexWithUV(x, y, z, icon.getMinU(), icon.getMinV());
		}
		
		int yy = y + (nY ? 1 : 0);
		int ny = y + (nY ? 0 : 1);
		
		int zz = z + (nY ? 0 : 1);
		int nz = z + (nY ? 1 : 0);
		
		int xx = x;
		int nx = x + 1;
		
		t.addVertexWithUV(xx, ny, zz, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(xx, yy, zz, icon.getMinU(), icon.getMinV());
		t.addVertexWithUV(nx, yy, zz, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(nx, ny, zz, icon.getMaxU(), icon.getMaxV());
		
		t.addVertexWithUV(xx, yy, nz, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(xx, ny, zz, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(nx, ny, zz, icon.getMinU(), icon.getMinV());
		t.addVertexWithUV(nx, yy, nz, icon.getMinU(), icon.getMaxV());
		
		t.addVertexWithUV(nx, yy, nz, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(nx, yy, nz, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(nx, ny, zz, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(nx, yy, zz, icon.getMinU(), icon.getMinV());
		
		t.addVertexWithUV(xx, yy, zz, icon.getMinU(), icon.getMinV());
		t.addVertexWithUV(xx, yy, zz, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(xx, ny, zz, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(xx, yy, nz, icon.getMaxU(), icon.getMinV());
		
	/*	t.addVertexWithUV(x + 1, y, z, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(x + 1, y, z + 1, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(x, y, z + 1, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(x, y, z, icon.getMinU(), icon.getMinV());
		
		t.addVertexWithUV(x + 1, y, z + 1, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(x + 1, y + 1, z, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(x, y + 1, z, icon.getMinU(), icon.getMinV());
		t.addVertexWithUV(x, y, z + 1, icon.getMinU(), icon.getMaxV());
		
		t.addVertexWithUV(x, y, z + 1, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(x, y, z + 1, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(x, y + 1, z, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(x, y, z, icon.getMinU(), icon.getMinV());
		
		t.addVertexWithUV(x + 1, y, z, icon.getMinU(), icon.getMinV());
		t.addVertexWithUV(x + 1, y, z, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(x + 1, y + 1, z, icon.getMinU(), icon.getMaxV());
		t.addVertexWithUV(x + 1, y, z + 1, icon.getMaxU(), icon.getMinV());*/
	}
	
}
