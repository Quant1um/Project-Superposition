package net.quantium.projectsuperposition.debug.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.debug.CapturedInfoContainer;
import net.quantium.projectsuperposition.debug.IDebuggable;
import net.quantium.projectsuperposition.network.AbstractServerMessageHandler;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.network.messages.CreateMessage;
import net.quantium.projectsuperposition.tileentities.TileEntitySolderingStation;

public class CaptureResponseMessage implements IMessage {

	public NBTTagCompound tag;
	public int x, y, z;
	
	public CaptureResponseMessage(){}
	public CaptureResponseMessage(int x, int y, int z, NBTTagCompound tag){
		this.x = x;
		this.y = y;
		this.z = z;
		this.tag = tag;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
		buf.writeInt(x).writeInt(y).writeInt(z);
	}
	
	public static class Handler extends AbstractServerMessageHandler<CaptureResponseMessage> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, CaptureResponseMessage message, MessageContext ctx) {
			return this.onMessage(message, ctx);
		}

		@Override
		public IMessage onMessage(CaptureResponseMessage message, MessageContext ctx) {
			World w = Minecraft.getMinecraft().theWorld;
			TileEntity te = w.getTileEntity(message.x, message.y, message.z);
			if(te != null && te instanceof IDebuggable) CapturedInfoContainer.info.put(te, message.tag);
			return null;
		}
	}
}
