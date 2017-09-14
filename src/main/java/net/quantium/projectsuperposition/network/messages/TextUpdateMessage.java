package net.quantium.projectsuperposition.network.messages;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.quantium.projectsuperposition.network.AbstractServerMessageHandler;
import net.quantium.projectsuperposition.tileentities.TileEntityHoloText;

public class TextUpdateMessage implements IMessage {
	 private int x,y,z;
	 private String[] text = new String[16];

	 
	 public TextUpdateMessage() {}

	
	 public TextUpdateMessage(int x, int y, int z, String[] text) {
		 this.text = text;
		 this.x = x;
		 this.y = y;
		 this.z = z;
	 }

	 @Override
	 public void fromBytes(ByteBuf buffer) {
		 x = buffer.readInt();
		 y = buffer.readInt();
		 z = buffer.readInt();
		 for(int i = 0; i < 16; i++){
			 int l = buffer.readInt();
			 byte[] b = buffer.readBytes(l).array();
			 text[i] = new String(b, Charsets.UTF_8);
		 }
	 }

	 @Override
	 public void toBytes(ByteBuf buffer) {
		 buffer.writeInt(x);
		 buffer.writeInt(y);
		 buffer.writeInt(z);
		 
		 for(int i = 0; i < 16; i++){
			 byte[] b = i >= text.length || text[i] == null ? new byte[0] : text[i].getBytes(Charsets.UTF_8);
			 buffer.writeInt(b.length);
			 buffer.writeBytes(b);
			 
		 }
				 
	 }
	 
	public static class Handler extends AbstractServerMessageHandler<TextUpdateMessage> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, TextUpdateMessage message, MessageContext ctx) {
			return this.onMessage(message, ctx);
		}

		@Override
		public IMessage onMessage(TextUpdateMessage message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			if(tile != null){
				if(tile instanceof TileEntityHoloText){
					((TileEntityHoloText)tile).text = message.text;
					((TileEntityHoloText)tile).redraw = true;
					ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
					tile.markDirty();
				}
			}
			return null;
		}
	}
}
