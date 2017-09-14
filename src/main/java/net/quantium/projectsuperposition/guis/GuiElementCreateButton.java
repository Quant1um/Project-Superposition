package net.quantium.projectsuperposition.guis;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.quantium.projectsuperposition.ModProvider;

public class GuiElementCreateButton extends GuiButton {
	protected static final ResourceLocation buttonTexture = new ResourceLocation(ModProvider.MODID + ":textures/guis/createbutton.png");
	
	public GuiElementCreateButton(int id, int x, int y) {
		super(id, x, y, 18, 18, "");
	}

	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
    {
        if (this.visible)
        {
        	p_146112_1_.getTextureManager().bindTexture(buttonTexture);
            this.field_146123_n = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
            int k = this.enabled ? (this.field_146123_n ? 18 : 0) : 18 * 2;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, k, 18, 18);
        }
    }
}
