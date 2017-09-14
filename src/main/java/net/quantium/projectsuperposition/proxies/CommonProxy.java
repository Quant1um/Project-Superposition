package net.quantium.projectsuperposition.proxies;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.quantium.projectsuperposition.items.ItemCell;


public class CommonProxy {

	public void registerRenderers(){}
	public void init(){}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
	
}
