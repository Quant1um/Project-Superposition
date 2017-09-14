package net.quantium.projectsuperposition.guis;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.containers.ContainerImpulsiveSuction;
import net.quantium.projectsuperposition.containers.ContainerSoldering;
import net.quantium.projectsuperposition.containers.ContainerWelding;

public class GuiWelding extends GuiContainer{
	public GuiWelding(ContainerWelding containerWelding) {
		super(containerWelding);
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
        this.mc.renderEngine.bindTexture(new ResourceLocation(ModProvider.MODID, "textures/guis/welding.png"));
        int ii = 142, jj = 10, ij = 253, ww = 3, hh = 52;
        int offY = (1000 - ((ContainerWelding)this.inventorySlots).ent.time) * hh / 1000;
        int x = (width - xSize) / 2;
        int y = ((height - ySize) / 2) - 1;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        this.drawTexturedModalRect(x + ii, y + jj + offY, ij, offY, ww, hh - offY);
	}
}
