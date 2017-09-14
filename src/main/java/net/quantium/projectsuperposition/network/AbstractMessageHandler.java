package net.quantium.projectsuperposition.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;

public abstract class AbstractMessageHandler<T extends IMessage> implements IMessageHandler <T, IMessage>{

}
