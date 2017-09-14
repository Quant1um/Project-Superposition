package net.quantium.projectsuperposition.network.messages;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.quantium.projectsuperposition.network.AbstractServerMessageHandler;
import net.quantium.projectsuperposition.tileentities.TileEntitySolderingStation;

public class CreateMessage implements IMessage {
	 private int x, y, z;
	 
	 public CreateMessage() {}	
	 public CreateMessage(int x, int y, int z) {
		 this.x = x;
		 this.y = y;
		 this.z = z;
	 }

	 @Override
	 public void fromBytes(ByteBuf buffer) {
		 x = buffer.readInt();
		 y = buffer.readInt();
		 z = buffer.readInt();
	 }

	 @Override
	 public void toBytes(ByteBuf buffer) {
		 buffer.writeInt(x);
		 buffer.writeInt(y);
		 buffer.writeInt(z);
	 }
	 
	public static class Handler extends AbstractServerMessageHandler<CreateMessage> {
		@Override
		public IMessage handleServerMessage(EntityPlayer player, CreateMessage message, MessageContext ctx) {
			return this.onMessage(message, ctx);
		}

		@Override
		public IMessage onMessage(CreateMessage message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
			if(tile != null){
				if(tile instanceof TileEntitySolderingStation){
					((TileEntitySolderingStation)tile).mustCraft = true;	
				}
			}
			return null;
		}
	}
}
