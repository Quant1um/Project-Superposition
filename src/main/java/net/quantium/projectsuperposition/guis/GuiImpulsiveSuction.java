package net.quantium.projectsuperposition.guis;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.containers.ContainerImpulsiveSuction;

public class GuiImpulsiveSuction extends GuiContainer{
	public GuiImpulsiveSuction(ContainerImpulsiveSuction cnt) {
		super(cnt);
	}

	@Override
	public void drawScreen(int x, int y, float xs) {
		super.drawScreen(x, y, xs);
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton b) {
		super.actionPerformed(b);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(0.9F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(ModProvider.MODID, "textures/guis/ism.png"));
        int x = (width - xSize) / 2;
        int y = ((height - ySize) / 2) - 1;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
