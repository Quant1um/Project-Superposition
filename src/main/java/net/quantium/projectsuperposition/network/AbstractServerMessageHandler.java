package net.quantium.projectsuperposition.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractServerMessageHandler<T extends IMessage> extends AbstractMessageHandler<T>{

	public IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx) {
		return null;
	}
	
}
