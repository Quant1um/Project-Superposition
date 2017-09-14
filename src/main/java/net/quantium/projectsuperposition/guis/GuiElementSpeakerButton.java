package net.quantium.projectsuperposition.guis;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.quantium.projectsuperposition.ModProvider;

public class GuiElementSpeakerButton extends GuiButton {
	int id = 0;
	
	 protected static final ResourceLocation buttonTexture = new ResourceLocation(ModProvider.MODID + ":textures/guis/widgets.png");
	public GuiElementSpeakerButton(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, int id) {
		super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, "");
		this.id = id;
		
	}
	 public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
	    {
	        if (this.visible)
	        {
	        	GL11.glEnable(GL11.GL_BLEND);
	            p_146112_1_.getTextureManager().bindTexture(buttonTexture);
	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	            boolean flag = p_146112_2_ >= this.xPosition && p_146112_3_ >= this.yPosition && p_146112_2_ < this.xPosition + this.width && p_146112_3_ < this.yPosition + this.height;
	            int k = 106;
	            if(this.enabled){
	            if (flag){
	                k += this.height;
	            }
	            }else{
	            	k = 106 + this.height * 2;
	            }
	            
	            this.drawTexturedModalRect(this.xPosition, this.yPosition, id * this.width, k, this.width, this.height);
	            GL11.glDisable(GL11.GL_BLEND);
	        }
	    }
}
