package net.quantium.projectsuperposition.proxies;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.quantium.projectsuperposition.ModProvider;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.client.RenderLastDispatcher;
import net.quantium.projectsuperposition.client.renderers.RendererCell;
import net.quantium.projectsuperposition.client.renderers.RendererCorner;
import net.quantium.projectsuperposition.client.renderers.RendererHand;
import net.quantium.projectsuperposition.client.renderers.RendererHolo;
import net.quantium.projectsuperposition.client.renderers.RendererItemBar;
import net.quantium.projectsuperposition.client.renderers.RendererTeleport;
import net.quantium.projectsuperposition.events.HandlerAlphaRender;
import net.quantium.projectsuperposition.protection.electriccoil.TileEntityElectricCoil;
import net.quantium.projectsuperposition.protection.electriccoil.client.RendererCoil;
import net.quantium.projectsuperposition.protection.field.TileEntityFieldGenerator;
import net.quantium.projectsuperposition.protection.field.client.RendererField;
import net.quantium.projectsuperposition.protection.turret.TileEntityTurret;
import net.quantium.projectsuperposition.protection.turret.render.RendererTurret;
import net.quantium.projectsuperposition.tileentities.TileEntityHoloText;
import net.quantium.projectsuperposition.tileentities.TileEntityTeleport;

public class ClientProxy extends CommonProxy {
	
	public static final int cornerRenderingId = RenderingRegistry.getNextAvailableRenderId();
	
	@Override
	public void registerRenderers(){
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModProvider.BLOCKS.get(ObjectNames.BLOCK_TURRET)), new RendererHand(new RendererTurret()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurret.class, new RendererTurret());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModProvider.BLOCKS.get(ObjectNames.BLOCK_ELECTRIC_COIL)), new RendererHand(new RendererCoil()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectricCoil.class, new RendererCoil());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModProvider.BLOCKS.get(ObjectNames.BLOCK_TELEPORT)), new RendererHand(new RendererTeleport()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeleport.class, new RendererTeleport());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModProvider.BLOCKS.get(ObjectNames.BLOCK_HOLO_TEXT)), new RendererHand(new RendererHolo()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHoloText.class, new RendererHolo());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModProvider.BLOCKS.get(ObjectNames.BLOCK_FIELD_GENERATOR)), new RendererHand(new RendererField()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFieldGenerator.class, new RendererField());
	
		MinecraftForgeClient.registerItemRenderer(ModProvider.ITEMS.get(ObjectNames.ITEM_WELDER), new RendererItemBar());
		MinecraftForgeClient.registerItemRenderer(ModProvider.ITEMS.get(ObjectNames.ITEM_SOLDERING_IRON), new RendererItemBar());
		MinecraftForgeClient.registerItemRenderer(ModProvider.ITEMS.get(ObjectNames.ITEM_LIQUID_CELL), new RendererCell());
		RenderingRegistry.registerBlockHandler(cornerRenderingId, new RendererCorner());
	}
	
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	@Override
	public void init(){
		MinecraftForge.EVENT_BUS.register(new HandlerAlphaRender());
		MinecraftForge.EVENT_BUS.register(RenderLastDispatcher.INSTANCE);
	}
}
