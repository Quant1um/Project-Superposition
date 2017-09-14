package net.quantium.projectsuperposition.guis;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.network.messages.TextUpdateMessage;
import net.quantium.projectsuperposition.tileentities.TileEntityHoloText;

public class GuiHoloText extends GuiScreen {
	
	public GuiElementTextEditor text;
	public GuiButton apply;
	public GuiButton[] format = new GuiButton[22];
	public static final String[] formatText = {"A", "A", "A", "A", "A", "A", "A", "A",
											   "A", "A", "A", "A", "A", "A", "A", "A",
											   "O", "B", "S", "U", "I", "R"};
	public TileEntity tileEntity;
	public String loadedText = "";
	public GuiHoloText(TileEntity tileEntity) {
		this.tileEntity = tileEntity;
		Keyboard.enableRepeatEvents(true);
		if(tileEntity instanceof TileEntityHoloText){
			if(((TileEntityHoloText)tileEntity).text != null){
				int a = Math.min(16, ((TileEntityHoloText)tileEntity).text.length);
				for(int i = 0; i < a; i++){
					if(((TileEntityHoloText)tileEntity).text[i] != null && !((TileEntityHoloText)tileEntity).text[i].isEmpty()){
						loadedText += ((TileEntityHoloText)tileEntity).text[i];
						if(i < a - 1) loadedText += "\n";
					}
				}
			}
		}
	}

	@Override
	public void drawScreen(int x, int y, float xs) {
		super.drawScreen(x, y, xs);
		int xx = (width - 200) / 2;
        int yy = ((height - 200) / 2) - 1;
	}

	@Override
	public void initGui() {
		super.initGui();
		int xx = (width - 200) / 2;
        int yy = (height - 200) / 2 - 1;
        int margin = 10;
		this.buttonList.add(apply = new GuiButton(0, xx + margin, yy + 180 + margin * 2, 180, 20, StatCollector.translateToLocal("holo.done")));
		this.buttonList.add(text = new GuiElementTextEditor(1, xx + 10, yy + 10, 180, 180));
		text.text = loadedText;
		text.setCursorPosition(text.text.length());
		
		for(int i = 0; i < 22; i++){
			int xxx = i < 8 ? xx - 20 : i < 14 ? xx + 10 + (i - 8) * 32 : xx + 200;
			int yyy = i < 8 ? yy + 190 - i * 30 : i < 14 ? yy - 20 : yy + (i - 14) * 30 - 20;
			this.buttonList.add(format[i] = new GuiButton(2 + i, xxx, yyy, 20, 20, EnumChatFormatting.values()[i] + formatText[i]));
		}
	}

	@Override
	protected void actionPerformed(GuiButton b) {
		super.actionPerformed(b);
		if(b.id == apply.id){
			PacketDispatcher.sendToServer(new TextUpdateMessage(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, text.getLines(16)));
			mc.displayGuiScreen(null);
			Keyboard.enableRepeatEvents(false);
		}else if(b.id > 1 && b.id <= 23)
		    text.writeTag(EnumChatFormatting.values()[b.id - 2]);
	}
	
	@Override
	protected void mouseClicked(int a, int b, int c){
		super.mouseClicked(a, b, c);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		text.onKeyTyped(par1, par2);
	}
	
	protected void drawHoveringText(int p_146283_2_, int p_146283_3_, int w, int h)
    {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = w;

            int j2 = p_146283_2_ + 12;
            int k2 = p_146283_3_ - 12;
            int i1 = h;

            if (j2 + k > this.width)
            {
                j2 -= 28 + k;
            }

            if (k2 + i1 + 6 > this.height)
            {
                k2 = this.height - i1 - 6;
            }

            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int j1 = -267386864;
            this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
     }
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	@Override
	public void onGuiClosed(){
		Keyboard.enableRepeatEvents(false);
	}
	
}
