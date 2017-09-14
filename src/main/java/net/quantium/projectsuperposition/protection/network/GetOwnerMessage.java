package net.quantium.projectsuperposition.protection.network;

import java.util.Random;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.quantium.projectsuperposition.network.AbstractClientMessageHandler;
import net.quantium.projectsuperposition.protection.IProtector;

public class GetOwnerMessage implements IMessage {
	 private int x, y, z;
	 private String owner;
	 
	 public GetOwnerMessage() {}

	
	 public GetOwnerMessage(int x, int y, int z, String owner) {
		 this.x = x;
		 this.y = y;
		 this.z = z;
		 this.owner = owner;
	 }

	 @Override
	 public void fromBytes(ByteBuf buffer) {
		 x = buffer.readInt();
		 y = buffer.readInt();
		 z = buffer.readInt();
		 int l = buffer.readInt();
		 byte[] b = buffer.readBytes(l).array();
		 owner = new String(b, Charsets.UTF_8);
		 
	 }

	 @Override
	 public void toBytes(ByteBuf buffer) {
		 buffer.writeInt(x);
		 buffer.writeInt(y);
		 buffer.writeInt(z);
		 byte[] b = owner == null ? new byte[0] : owner.getBytes(Charsets.UTF_8);
		 buffer.writeInt(b.length);
		 buffer.writeBytes(b);
	 }
	 
	public static class Handler extends AbstractClientMessageHandler<GetOwnerMessage> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, GetOwnerMessage message, 
			MessageContext ctx) {
			return this.onMessage(message, ctx);
		}

		@Override
		public IMessage onMessage(GetOwnerMessage message, MessageContext ctx) {
			if(message.owner == null) return null;
			if(Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z) instanceof IProtector) {
				((IProtector)Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z)).setOwner(message.owner);
			}
			return null;
		}
	}
}
