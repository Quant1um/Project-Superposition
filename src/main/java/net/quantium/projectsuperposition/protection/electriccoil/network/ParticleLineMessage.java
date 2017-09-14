package net.quantium.projectsuperposition.protection.electriccoil.network;

import java.util.Random;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.quantium.projectsuperposition.client.particles.CoilLineParticle;
import net.quantium.projectsuperposition.network.AbstractClientMessageHandler;

public class ParticleLineMessage implements IMessage {
	 private double x0, y0, z0;
	 private int x, y, z;
	 
	 public ParticleLineMessage() {}

	
	 public ParticleLineMessage(int x, int y, int z, double d, double e, double f) {
		 this.x = x;
		 this.y = y;
		 this.z = z;
		 this.x0 = d;
		 this.y0 = e;
		 this.z0 = f;
	 }

	 @Override
	 public void fromBytes(ByteBuf buffer) {
		 x = buffer.readInt();
		 y = buffer.readInt();
		 z = buffer.readInt();
		 x0 = buffer.readDouble();
		 y0 = buffer.readDouble();
		 z0 = buffer.readDouble();
	 }

	 @Override
	 public void toBytes(ByteBuf buffer) {
		 buffer.writeInt((int)x);
		 buffer.writeInt((int)y);
		 buffer.writeInt((int)z);
		 buffer.writeDouble(x0);
		 buffer.writeDouble(y0);
		 buffer.writeDouble(z0);
	 }
	 
	public static class Handler extends AbstractClientMessageHandler<ParticleLineMessage> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, ParticleLineMessage message, 
			MessageContext ctx) {
			return this.onMessage(message, ctx);
		}

		@Override
		public IMessage onMessage(ParticleLineMessage message, MessageContext ctx) {
			Minecraft.getMinecraft().effectRenderer.addEffect(new CoilLineParticle(Minecraft.getMinecraft().theWorld, message.x + 0.5d, message.y + 0.5d, message.z + 0.5d, message.x0, message.y0, message.z0));
			return null;
		}
	}
}
