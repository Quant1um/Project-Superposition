package net.quantium.projectsuperposition.network.messages;

import java.util.Random;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.quantium.projectsuperposition.network.AbstractClientMessageHandler;

public class ParticleMessage implements IMessage {
	 private float x, y, z;
	 
	 public ParticleMessage() {}

	
	 public ParticleMessage(float x, float y, float z) {
		 this.x = x;
		 this.y = y;
		 this.z = z;
	 }

	 @Override
	 public void fromBytes(ByteBuf buffer) {
	 
	 
		 x = buffer.readFloat();
		 y = buffer.readFloat();
		 z = buffer.readFloat();
	 
	 }

	 @Override
	 public void toBytes(ByteBuf buffer) {
		 buffer.writeFloat(x);
		 buffer.writeFloat(y);
		 buffer.writeFloat(z);
	 }
	 
	public static class Handler extends AbstractClientMessageHandler<ParticleMessage> {
		@Override
		public IMessage handleClientMessage(EntityPlayer player, ParticleMessage message, MessageContext ctx) {
			return this.onMessage(message, ctx);
		}

		@Override
		public IMessage onMessage(ParticleMessage message, MessageContext ctx) {
			Random rand = Minecraft.getMinecraft().theWorld.rand;
			for(int i = 0; i < 10; i++)
				Minecraft.getMinecraft().theWorld.spawnParticle("happyVillager", message.x + rand.nextDouble() - 0.5, message.y + rand.nextDouble() - 0.5, message.z+ rand.nextDouble() - 0.5, 0.0D, 0.0D, 0.0D);
			return null;
		}
	}
}
