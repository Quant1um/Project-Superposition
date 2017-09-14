package net.quantium.projectsuperposition;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quantium.projectsuperposition.containers.ContainerImpulsiveSuction;
import net.quantium.projectsuperposition.containers.ContainerSoldering;
import net.quantium.projectsuperposition.containers.ContainerWelding;
import net.quantium.projectsuperposition.guis.GuiHoloText;
import net.quantium.projectsuperposition.guis.GuiImpulsiveSuction;
import net.quantium.projectsuperposition.guis.GuiSoldering;
import net.quantium.projectsuperposition.guis.GuiWelding;
import net.quantium.projectsuperposition.protection.IProtector;
import net.quantium.projectsuperposition.protection.ProtectorContainer;
import net.quantium.projectsuperposition.protection.ProtectorGui;
import net.quantium.projectsuperposition.protection.field.ContainerField;
import net.quantium.projectsuperposition.protection.field.GuiField;
import net.quantium.projectsuperposition.protection.field.TileEntityFieldGenerator;
import net.quantium.projectsuperposition.tileentities.TileEntityHoloText;
import net.quantium.projectsuperposition.tileentities.TileEntityImpulsiveSuctionModule;
import net.quantium.projectsuperposition.tileentities.TileEntitySolderingStation;
import net.quantium.projectsuperposition.tileentities.TileEntityWeldingStation;

public class GuiHandler implements IGuiHandler {
        @Override
        public Container getServerGuiElement(int id, EntityPlayer player, World world,
                        int x, int y, int z) {
        		                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityImpulsiveSuctionModule){
                    return new ContainerImpulsiveSuction((IInventory)tileEntity, player.inventory, x, y, z);
                }
                if(tileEntity instanceof TileEntityWeldingStation) return new ContainerWelding((TileEntityWeldingStation)tileEntity, player.inventory);
                if(tileEntity instanceof TileEntitySolderingStation) return new ContainerSoldering((TileEntitySolderingStation)tileEntity, player.inventory);
                if(tileEntity instanceof TileEntityFieldGenerator && id == 1) return new ContainerField((TileEntityFieldGenerator) tileEntity, player.inventory);
                if(tileEntity instanceof IProtector) return new ProtectorContainer((IProtector) tileEntity, tileEntity, player.inventory);
                return null;
        }

        
        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world,
                        int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityImpulsiveSuctionModule){
                    return new GuiImpulsiveSuction(new ContainerImpulsiveSuction((IInventory)tileEntity, player.inventory, x, y, z));
                }
                if(tileEntity instanceof TileEntityFieldGenerator && id == 1) return new GuiField(new ContainerField((TileEntityFieldGenerator) tileEntity, player.inventory));
                if(tileEntity instanceof TileEntityHoloText) return new GuiHoloText(tileEntity);
                if(tileEntity instanceof TileEntityWeldingStation) return new GuiWelding(new ContainerWelding((TileEntityWeldingStation)tileEntity, player.inventory));
                if(tileEntity instanceof TileEntitySolderingStation) return new GuiSoldering(new ContainerSoldering((TileEntitySolderingStation)tileEntity, player.inventory));
                if(tileEntity instanceof IProtector) 
                	return new ProtectorGui(new ProtectorContainer((IProtector) tileEntity, tileEntity, player.inventory));
                return null;

        }
}