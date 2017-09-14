package net.quantium.projectsuperposition.guis;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.containers.ContainerImpulsiveSuction;
import net.quantium.projectsuperposition.containers.ContainerSoldering;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.network.messages.CreateMessage;

public class GuiSoldering extends GuiContainer{
	private GuiButton button;
	
	public GuiSoldering(ContainerSoldering cnt) {
		super(cnt);
	}

	@Override
	public void drawScreen(int x, int y, float xs) {
		super.drawScreen(x, y, xs);
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(button = new GuiElementCreateButton(0, (width - xSize) / 2 + 118, (height - ySize) / 2 + 44));
	}

	@Override
	protected void actionPerformed(GuiButton b) {
		super.actionPerformed(b);
		if(b.id == button.id) PacketDispatcher.sendToServer(new CreateMessage(((ContainerSoldering)inventorySlots).ent.xCoord, ((ContainerSoldering)inventorySlots).ent.yCoord, ((ContainerSoldering)inventorySlots).ent.zCoord));
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(0.9F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation(ModProvider.MODID, "textures/guis/soldering.png"));
        int ii = 142, jj = 28, ij = 253, ww = 3, hh = 42;
        int offY = (100 - ((ContainerSoldering)this.inventorySlots).ent.time) * hh / 100;
        int x = (width - xSize) / 2;
        int y = ((height - ySize) / 2) - 1;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        this.drawTexturedModalRect(x + ii, y + jj + offY, ij, offY, ww, hh - offY);
	}
}
