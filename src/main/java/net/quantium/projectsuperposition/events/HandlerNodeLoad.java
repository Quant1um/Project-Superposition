package net.quantium.projectsuperposition.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.ChunkEvent;
import net.quantium.projectsuperposition.blocks.BlockNodeContainer;
import net.quantium.projectsuperposition.net.INetNode;
import net.quantium.projectsuperposition.net.WorldNetContainer;
import net.quantium.projectsuperposition.utilities.vector.IntegerVector3;

public class HandlerNodeLoad {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChunkLoaded(ChunkEvent.Load e){
        for (Object o : e.getChunk().chunkTileEntityMap.values()){
        	if (o instanceof TileEntity && o instanceof INetNode){
        		TileEntity te = (TileEntity)o;
        		if(e.world.getBlock(te.xCoord, te.yCoord, te.zCoord) instanceof BlockNodeContainer){
        			Class<? extends INetNode> clazz = ((BlockNodeContainer) e.world.getBlock(te.xCoord, te.yCoord, te.zCoord)).getNodeClass();
        			WorldNetContainer.get(e.world).getNet(e.world, clazz).addNode(new IntegerVector3(te.xCoord, te.yCoord, te.zCoord), (INetNode) te);
        		}
        	}
        }
	}
}
