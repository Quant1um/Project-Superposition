package net.quantium.projectsuperposition.debug.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.quantium.projectsuperposition.debug.IDebuggable;
import net.quantium.projectsuperposition.network.AbstractServerMessageHandler;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.network.messages.CreateMessage;
import net.quantium.projectsuperposition.tileentities.TileEntitySolderingStation;

public class CaptureRequestMessage implements IMessage {

	public int x, y, z;
	
	public CaptureRequestMessage(){}
	public CaptureRequestMessage(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x).writeInt(y).writeInt(z);
	}
	
	public static class Handler extends AbstractServerMessageHandler<CaptureRequestMessage> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, CaptureRequestMessage message, MessageContext ctx) {
			return this.onMessage(message, ctx);
		}

		@Override
		public IMessage onMessage(CaptureRequestMessage message, MessageContext ctx) {
			TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			if(te != null && te instanceof IDebuggable){
				NBTTagCompound tag = new NBTTagCompound();
				((IDebuggable)te).capture(tag);
				PacketDispatcher.sendTo(new CaptureResponseMessage(message.x, message.y, message.z, tag), ctx.getServerHandler().playerEntity);
			}
			return null;
		}
	}
}
