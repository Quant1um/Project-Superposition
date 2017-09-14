package net.quantium.projectsuperposition.guis;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;

@SideOnly(Side.CLIENT)
public class GuiElementTextEditor extends GuiButton{
	
	public int cursor;
	public int maxLength = 18;
	public int maxRows = 16;
	public String text = "";
	public int selectionStart = -1, selectionEnd = -1;
	
	public void deselect(){
		selectionStart = selectionEnd = -1;
	}
	
	public String getSelectedText(){
		if(isSelected()) return text.substring(selectionStart, selectionEnd);
		return null;
	}
	
	public String getText(){
		return text;
	}
	
	///
	public String[] getLines(){
		return text.split("\n", -1);
	}
	
	public int getLineCount(){
		return text.split("\n", -1).length;
	}
	
	public String getLine(int v){
		return getLines()[v].replace('\u00a7', '\u00b6');
	}
	
	/**
	 * For future use
	 * @return false
	 */
	public boolean isSelected(){
		return false;
	}
	
	public void writeTag(EnumChatFormatting e){
		write(e.toString());
	}
	
	public void lineFeed(){
		if(isSelected()){
			String after = text.substring(0, selectionStart) + "\n" + text.substring(selectionEnd, text.length());
			if(after.split("\n").length < maxRows){
				text = after;
			}
		}else{
			if(getLineCount() < maxRows) {
				text = text.substring(0, cursor) + "\n" + text.substring(cursor, text.length());
				moveCursor(1);
			}
		}
	}
	
	public void moveCursor(int val){
		setCursorPosition(cursor + val);
	}
	
	public void setCursorPosition(int c){
		deselect();
		if(c < 0) c = 0;
		if(c >= text.length()) c = text.length();
		cursor = c;
	}
	
	public void write(String val){
		//val = ChatAllowedCharacters.filerAllowedCharacters(text);
		if(isSelected()){
			if(selectionStart > 0) text += text.substring(0, selectionStart);
			text += val;
			if(selectionEnd < text.length()) text += text.substring(selectionEnd, text.length());
		}else{
			text = text.substring(0, cursor) + val + text.substring(cursor, text.length());
			moveCursor(val.length());
		}
		
	}
	
	public GuiElementTextEditor(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_) {
		super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, "");
	}
	    
	@Override 
	public void drawButton(Minecraft mc, int x, int y){
		drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0x22222222);
        FontRenderer f = mc.fontRenderer;
        int v = 0;
        for(int i = 0; i < getLineCount(); i++){
        	String text = getLine(i);
        	f.drawString(text, 5 + xPosition, i * (f.FONT_HEIGHT + 2) + yPosition + 4, 0xffffff);
        	/*for(int j = 0; j < text.length(); j++){
        		int idx = v + j;
        		boolean selected = idx >= selectionStart && idx < selectionEnd;
        		if(selected) {
        			Gui.drawRect(3 + xPosition, i * (f.FONT_HEIGHT + 2) + yPosition + 3, f.getCharWidth(this.text.charAt(idx)) + 3, (f.FONT_HEIGHT + 2), 0x6699dd);
        		}
        		
        		//if(cursor == idx) f.drawString("_", xPosition + f.getStringWidth(text.substring(0, idx)), i * (f.FONT_HEIGHT + 2) + yPosition + 3, 0xffffff);
            }	*/
        	v += text.length() + 1;
        }
        boolean flag = ((Minecraft.getSystemTime() / 500) & 1) == 0;
        if(flag){
        	int yy = 0;
        	int j = 0;
        	//if(cursor < 0) cursor = 0;
        	//if(cursor >= getLineCount()) cursor = getLineCount() - 1;
        	while(yy < cursor) { 
        		if(j >= getLineCount()) break;
        		yy += getLine(j).length() + 1;
        		if(yy > cursor) break;
        		j++; 
        	}
        	int xx = cursor - getLineStart(j);
        	String ss = j >= getLineCount() ? "" : getLine(j);
        	String sub = (xx <= 0 || ss.length() < xx || ss.length() < 1) ? "" : ss.substring(0, xx);
        	f.drawString("_", f.getStringWidth(sub) + xPosition + 5, (f.FONT_HEIGHT + 2) * j + yPosition + 5, 0xffffff);
        	 f.drawString(cursor + ";" + getLineStart(j), 3, 3, 0xdddddd);
        }
       
	}
	
	public void onKeyTyped(char ch, int cd){
		switch(cd){
		 case 14:
              this.delete(-1);
             return;
         case 199:
        	 this.setCursorPosition(0);
             return;
         case 203:
        	 this.moveCursor(-1);
             return;
         case 208:
        	 //down
             return;
         case 200:
        	 //up
             return;
         case 205:
             this.moveCursor(1);
             return;
         case 207:
             this.setCursorPosition(text.length());
             return;
         case 211:
        	 if (this.enabled)
             {
                 this.delete(1);
             }

             return;
         default:
        	 if(ch == 10 || ch == 13){
        		 lineFeed();
        	 }
        	 else if (ChatAllowedCharacters.isAllowedCharacter(ch))
             {
                 if (this.enabled)
                 {
                     this.write(Character.toString(ch));
                 }
             }
		}
		
	}

	public void delete(int i) {
		if(isSelected()) this.write("");
		else{
			 boolean flag = i < 0;
             int j = flag ? this.cursor + i : this.cursor;
             int k = flag ? this.cursor : this.cursor + i;
             String s = "";

             if (j >= 0)
             {
                 s = this.text.substring(0, j);
             }

             if (k < this.text.length())
             {
                 s = s + this.text.substring(k);
             }

             this.text = s;

             if (flag) this.moveCursor(i);
             
		}
	}

	public int getIndexByCoords(int x, int y){
		int c = getLineCount();
		if(y >= c) y = c - 1;
		return getLineStart(y) + x;
	}
	
	public int getLineStart(int line){
		int s = 0;
		for(int i = 0; i < line; i++) s += getLine(i).length() + 1;
		return s;
	}
	
	public String[] getLines(int i) {
		String[] v = new String[i];
		String[] d = getLines();
		for(int j = 0; j < i; j++){
			v[j] = j < d.length ? d[j] : "";
		}
		return v;
	}
	
	@Override
	public boolean mousePressed(Minecraft mc, int x, int y){
		boolean flag = x >= xPosition && x <= xPosition + width && y >= yPosition && y < yPosition + height;
		if(flag){
			int l = (y - yPosition) / (mc.fontRenderer.FONT_HEIGHT + 2);
			int c = getLineCount();
			if(l >= c) l = c - 1;
			if(l < 0) l = 0;
			int xx = 0;
			String ss = getLine(l);
			int v = 0;
			while(xx < x - xPosition - 8){
				if(v >= ss.length()) break;
				xx += mc.fontRenderer.getCharWidth(ss.charAt(v));
				v++;
			}
			cursor = selectionStart = selectionEnd = getIndexByCoords(v, l);
			return true;
		}
		
		return false;
	}
}
