package net.quantium.projectsuperposition.protection;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.quantium.projectsuperposition.network.PacketDispatcher;

public class ProtectorGui extends GuiContainer{
	String owner = StatCollector.translateToLocal("info.loading");
	
	public ProtectorGui(ProtectorContainer cnt) {
		super(cnt);
	}

	@Override
	public void drawScreen(int x, int y, float xs) {
		super.drawScreen(x, y, xs);
		owner = ((ProtectorContainer)inventorySlots).protector.owner();
		fontRendererObj.drawString(owner, (width + xSize) / 2 - fontRendererObj.getStringWidth(owner) - 10, (ySize + height) / 2 - ySize + 6, (45 << 16) | (45 << 8) | 45, false);
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
        this.mc.renderEngine.bindTexture(new ResourceLocation("projectsuperposition","textures/guis/protector.png"));
        int x = (width - xSize) / 2;
        int y = ((height - ySize) / 2) - 1;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
