package net.quantium.projectsuperposition;

import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.quantium.projectsuperposition.api.Bridge;
import net.quantium.projectsuperposition.api.ObjectNames;
import net.quantium.projectsuperposition.api.Registry;
import net.quantium.projectsuperposition.api.recipes.soldering.BasicSolderingRecipe;
import net.quantium.projectsuperposition.api.recipes.welding.BasicWeldingRecipe;
import net.quantium.projectsuperposition.blocks.BlockConductor;
import net.quantium.projectsuperposition.blocks.BlockEnergyBuffer;
import net.quantium.projectsuperposition.blocks.BlockEnergyController;
import net.quantium.projectsuperposition.blocks.BlockGenerator;
import net.quantium.projectsuperposition.blocks.BlockHoloText;
import net.quantium.projectsuperposition.blocks.BlockHull;
import net.quantium.projectsuperposition.blocks.BlockImpulsiveSuction;
import net.quantium.projectsuperposition.blocks.BlockSlab;
import net.quantium.projectsuperposition.blocks.BlockSoldering;
import net.quantium.projectsuperposition.blocks.BlockStairs;
import net.quantium.projectsuperposition.blocks.BlockTeleport;
import net.quantium.projectsuperposition.blocks.BlockWelding;
import net.quantium.projectsuperposition.core.HeatableRecipe;
import net.quantium.projectsuperposition.core.soldering.BiometricCardRecipe;
import net.quantium.projectsuperposition.core.soldering.IntegerCardRecipe;
import net.quantium.projectsuperposition.core.soldering.PreventorCardRecipe;
import net.quantium.projectsuperposition.core.soldering.SolderingRecipeRegistry;
import net.quantium.projectsuperposition.core.tickdispatcher.TickDispatcher;
import net.quantium.projectsuperposition.core.welding.WeldingRecipeRegistry;
import net.quantium.projectsuperposition.debug.DebugDrawer;
import net.quantium.projectsuperposition.events.HandlerCardClicked;
import net.quantium.projectsuperposition.events.HandlerFieldProtection;
import net.quantium.projectsuperposition.events.HandlerFluidRegistry;
import net.quantium.projectsuperposition.events.HandlerNodeLoad;
import net.quantium.projectsuperposition.items.ItemCell;
import net.quantium.projectsuperposition.items.ItemHeatableEnergyTool;
import net.quantium.projectsuperposition.items.ItemIntegerCard;
import net.quantium.projectsuperposition.items.ItemKyouko;
import net.quantium.projectsuperposition.items.ItemPortalLinker;
import net.quantium.projectsuperposition.network.PacketDispatcher;
import net.quantium.projectsuperposition.protection.IProtector;
import net.quantium.projectsuperposition.protection.ItemBiometricCard;
import net.quantium.projectsuperposition.protection.ProtectorBlock;
import net.quantium.projectsuperposition.protection.electriccoil.TileEntityElectricCoil;
import net.quantium.projectsuperposition.protection.field.ItemFieldCard;
import net.quantium.projectsuperposition.protection.field.TileEntityFieldGenerator;
import net.quantium.projectsuperposition.protection.network.GetOwnerMessage;
import net.quantium.projectsuperposition.protection.turret.EntityBullet;
import net.quantium.projectsuperposition.protection.turret.TileEntityTurret;
import net.quantium.projectsuperposition.proxies.CommonProxy;
import net.quantium.projectsuperposition.tileentities.TileEntityBuffer;
import net.quantium.projectsuperposition.tileentities.TileEntityConductor;
import net.quantium.projectsuperposition.tileentities.TileEntityEnergyController;
import net.quantium.projectsuperposition.tileentities.TileEntityGenerator;
import net.quantium.projectsuperposition.tileentities.TileEntityHoloText;
import net.quantium.projectsuperposition.tileentities.TileEntityImpulsiveSuctionModule;
import net.quantium.projectsuperposition.tileentities.TileEntitySolderingStation;
import net.quantium.projectsuperposition.tileentities.TileEntityTeleport;
import net.quantium.projectsuperposition.tileentities.TileEntityWeldingStation;

@Mod(modid = ModProvider.MODID, version = ModProvider.VERSION)
public class ModProvider
{
    public static final String MODID = "projectsuperposition";
    public static final String VERSION = "1.0";
    
    @Instance
    public static ModProvider INSTANCE;
    public static CreativeTabs TAB;
    
    @SidedProxy(clientSide = "net.quantium.projectsuperposition.proxies.ClientProxy", serverSide = "net.quantium.projectsuperposition.proxies.CommonProxy")
    public static CommonProxy proxy;
    
	public static final Registry<Item> ITEMS = new Registry<Item>(){
		@Override
		public void onRegister(String s, Item o) {
			GameRegistry.registerItem(o, s);
			OreDictionary.registerOre(s, o);
		}
	};
	
	public static final Registry<Block> BLOCKS = new Registry<Block>(){
		@Override
		public void onRegister(String s, Block o) {
			GameRegistry.registerBlock(o, s);
		}
	};
	
	public static final Registry<Class<? extends TileEntity>> TILE_ENTITIES = new Registry<Class<? extends TileEntity>>(){
		@Override
		public void onRegister(String s, Class<? extends TileEntity> o) {
			GameRegistry.registerTileEntity(o, MODID + ":" + s);
		}
	};
	
	public static final SolderingRecipeRegistry SOLDERING_RECIPES = new SolderingRecipeRegistry();
	public static final WeldingRecipeRegistry WELDING_RECIPES = new  WeldingRecipeRegistry();
	
	/* TODO List:
	 * 
	 *  - REDESING!
	 * 
	 *  - REFACTOR ALL CODE!!:
	 *    - endure code parts into functions
	 *    - create more more more AND MORE constants
	 *    - simplify code
	 *    - set annotations SideOnly to some functions
	 *    
	 *  - OWN VECTOR AND MATRIX CLASSES: (INDEV)
	 *    - BlockVector3
	 *    - Vector3
	 *    - BlockDimensionVector3
	 *  
	 *  - ENERGY SYSTEM!
	 *  - NETWORKING SYSTEM:
	 *    - energy transferring
	 *    - data transferring (custom data format)
	 *    - items transferring
	 *    - redstone transferring
	 *    - liquid transferring
	 * 
	 *  - CELL CONTROL SYSTEM!
	 *  
	 *  - RAILWAYS: moving systems
	 *  - ADVANCED TELEPORTERS!
	 *  
	 *  - ORE REFINERY!
	 *  
	 *  - ARMORING & WEAPONS
	 *  - ADVANCED PROTECTION
	 *  
	 *  - data handling: 
	 *    - by function
	 *    - by js code
	 */
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new HandlerCardClicked());
    	MinecraftForge.EVENT_BUS.register(new HandlerNodeLoad());
    	MinecraftForge.EVENT_BUS.register(new HandlerFieldProtection());
    	MinecraftForge.EVENT_BUS.register(new DebugDrawer());
    	FMLCommonHandler.instance().bus().register(TickDispatcher.INSTANCE);
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	
    	TILE_ENTITIES.register(ObjectNames.BLOCK_ELECTRIC_COIL, TileEntityElectricCoil.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_TELEPORT, TileEntityTeleport.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_TURRET, TileEntityTurret.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_SOLDERING, TileEntitySolderingStation.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_WELDING, TileEntityWeldingStation.class);
    	//TILE_ENTITIES.register("qconductor", TileEntityConductor.class);
    	//TILE_ENTITIES.register("qgenerator", TileEntityGenerator.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_HOLO_TEXT, TileEntityHoloText.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_ENERGY_BUFFER, TileEntityBuffer.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_FIELD_GENERATOR, TileEntityFieldGenerator.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_ENERGY_CONTROLLER, TileEntityEnergyController.class);
    	TILE_ENTITIES.register(ObjectNames.BLOCK_ISM, TileEntityImpulsiveSuctionModule.class);
    	
    	TAB = new CreativeTabs("projectsuperposition"){
    		@Override
    		public Item getTabIconItem() {
    			return ITEMS.get(ObjectNames.ITEM_ENDERCORE);
    		}
        };
        
        BLOCKS.register(ObjectNames.BLOCK_ELECTRIC_COIL, new ProtectorBlock(TileEntityElectricCoil.class){
        	public boolean canPlaceBlockOnSide(World w, int x, int y, int z, int side)
            {
                return (side == 1 && w.isSideSolid(x, y - 1, z, ForgeDirection.UP));
            }
            
            public void onNeighborBlockChange(World w, int x, int y, int z, Block p_149695_5_)
            {
                if(!w.isSideSolid(x, y - 1, z, ForgeDirection.UP)){
                	int meta = w.getBlockMetadata(x, y, z);
                	dropBlockAsItem(w, x, y, z, meta, 0);
                	dropInventory(w, x, y, z);
                    w.setBlockToAir(x, y, z);
                }
            }
        }.setBlockName("electriccoil"));
        BLOCKS.register(ObjectNames.BLOCK_TURRET, new ProtectorBlock(TileEntityTurret.class){
        	public int onBlockPlaced(World w, int x, int y, int z, int s, float hx, float hy, float hz, int m)
            {
            	if(s == 0) return 0;
                return 1;
            }
            public boolean canPlaceBlockOnSide(World w, int x, int y, int z, int side)
            {
                return (side == 1 && w.isSideSolid(x, y - 1, z, ForgeDirection.UP)) || (side == 0 && w.isSideSolid(x, y + 1, z, ForgeDirection.DOWN));
            }
            
            public void onNeighborBlockChange(World w, int x, int y, int z, Block p_149695_5_)
            {
                int meta = w.getBlockMetadata(x, y, z);
                boolean flag = false;
                if(meta == 0 && !w.isSideSolid(x, y + 1, z, ForgeDirection.DOWN)) flag = true;
                if(meta == 1 && !w.isSideSolid(x, y - 1, z, ForgeDirection.UP)) flag = true;
                if(flag){
                	dropBlockAsItem(w, x, y, z, meta, 0);
                	dropInventory(w, x, y, z);
                    w.setBlockToAir(x, y, z);
                }
            }
        }.setBlockName("autoturret"));
        
        
        
       // BLOCKS.register("conductorq", new BlockConductor());
       // BLOCKS.register("generatorq", new BlockGenerator());
        
        
        BLOCKS.register(ObjectNames.BLOCK_FIELD_GENERATOR, new ProtectorBlock(TileEntityFieldGenerator.class){
        	
        	public boolean canPlaceBlockOnSide(World w, int x, int y, int z, int side)
            {
                return (side == 1 && w.isSideSolid(x, y - 1, z, ForgeDirection.UP));
            }
            
            public void onNeighborBlockChange(World w, int x, int y, int z, Block p_149695_5_)
            {
                if(!w.isSideSolid(x, y - 1, z, ForgeDirection.UP)){
                	int meta = w.getBlockMetadata(x, y, z);
                	dropBlockAsItem(w, x, y, z, meta, 0);
                	dropInventory(w, x, y, z);
                    w.setBlockToAir(x, y, z);
                }else{
                	w.markBlockForUpdate(x, y, z);
                	if(w.getTileEntity(x, y, z) != null)
                		w.getTileEntity(x, y, z).markDirty();
                }
            }
            
            @Override
        	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ply, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        		if(ply.isSneaking() || w.getTileEntity(x, y, z) == null) return false;
        		if(ply.inventory.getCurrentItem() != null && ply.inventory.getCurrentItem().getItem() == ITEMS.get(ObjectNames.ITEM_SCREWDRIVER)){
        			ply.openGui(ModProvider.INSTANCE, 0, w, x, y, z);
        			if(!w.isRemote && w.getTileEntity(x, y, z) instanceof IProtector) PacketDispatcher.sendTo(new GetOwnerMessage(x, y, z, ((IProtector)w.getTileEntity(x, y, z)).owner()), (EntityPlayerMP) ply);
        			return true;
        		}
        		ply.openGui(ModProvider.INSTANCE, 1, w, x, y, z);
        		return true;
        	}
            
            @Override
        	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
            {
            	TileEntity te = world.getTileEntity(x, y, z);
            	if(te != null && te instanceof TileEntityFieldGenerator)
            		net.quantium.projectsuperposition.utilities.Utils.dropInventory(world, x, y, z, ((TileEntityFieldGenerator) te).size);
            	dropInventory(world, x, y, z);
        		return super.removedByPlayer(world, player, x, y, z, willHarvest);
            }
            
        }.setBlockName("fieldgenerator"));
        BLOCKS.register(ObjectNames.BLOCK_SOLDERING, new BlockSoldering());
        
        BLOCKS.register(ObjectNames.BLOCK_PLATED_HULL, new BlockHull().setBlockName("platedHull"));
        BLOCKS.register(ObjectNames.BLOCK_PLATED_STAIRS, new BlockStairs(BLOCKS.get(ObjectNames.BLOCK_PLATED_HULL)).setBlockName("platedStairs"));
        /*BLOCKS.register(ObjectNames.BLOCK_PLATED_CORNER_HULL, new BlockStairs(BLOCKS.get(ObjectNames.BLOCK_PLATED_HULL)){
        	@Override
        	public int getRenderType(){
        		return ClientProxy.cornerRenderingId;
        	}
        }.setBlockName("platedCorner"));*/ //WORK IN PROGRESS
        BLOCKS.register(ObjectNames.BLOCK_PLATED_SLAB, new BlockSlab(false));
        BLOCKS.register(ObjectNames.BLOCK_PLATED_SLAB_DOUBLED, new BlockSlab(true));
        
        BLOCKS.register(ObjectNames.BLOCK_WELDING, new BlockWelding());
        BLOCKS.register(ObjectNames.BLOCK_ENERGY_BUFFER, new BlockEnergyBuffer());
        BLOCKS.register(ObjectNames.BLOCK_ENERGY_CONTROLLER, new BlockEnergyController());
        BLOCKS.register(ObjectNames.BLOCK_ISM, new BlockImpulsiveSuction());
        BLOCKS.register(ObjectNames.BLOCK_TELEPORT, new BlockTeleport());
        BLOCKS.register(ObjectNames.BLOCK_HOLO_TEXT, new BlockHoloText());
        //BLOCKS.register(ObjectNames.BLOCK_SPEAKER, new BlockSpeaker());

        ITEMS.register(ObjectNames.ITEM_BIOMETRIC_CARD, new ItemBiometricCard());
        ITEMS.register(ObjectNames.ITEM_FIELD_CARD, new ItemFieldCard());
        ITEMS.register(ObjectNames.ITEM_SIZE_CARD, new ItemIntegerCard());
        ITEMS.register(ObjectNames.ITEM_CHIP_BASE, new Item().setCreativeTab(TAB).setUnlocalizedName("chipbaseq").setTextureName(ModProvider.MODID + ":basechip"));
        ITEMS.register(ObjectNames.ITEM_CHIP_A, new Item().setCreativeTab(TAB).setUnlocalizedName("chipaq").setTextureName(ModProvider.MODID + ":achip"));
        ITEMS.register(ObjectNames.ITEM_CHIP_B, new Item().setCreativeTab(TAB).setUnlocalizedName("chipbq").setTextureName(ModProvider.MODID + ":bchip"));
        ITEMS.register(ObjectNames.ITEM_IMPULSE_GENERATOR, new Item().setCreativeTab(TAB).setUnlocalizedName("impulsegen").setTextureName(ModProvider.MODID + ":impulsegen"));
        
        ITEMS.register(ObjectNames.ITEM_ENDERCORE, new Item().setCreativeTab(TAB).setUnlocalizedName("endercore").setTextureName(ModProvider.MODID + ":endercore"));
        ITEMS.register(ObjectNames.ITEM_BIOMETRIC_READER, new Item().setCreativeTab(TAB).setUnlocalizedName("bioreader").setTextureName(ModProvider.MODID + ":biometricreader"));
        ITEMS.register(ObjectNames.ITEM_SILICON, new Item().setCreativeTab(TAB).setUnlocalizedName("siliconq").setTextureName(ModProvider.MODID + ":silicium"));
        ITEMS.register(ObjectNames.ITEM_SOLENOID, new Item().setCreativeTab(TAB).setUnlocalizedName("solenoid").setTextureName(ModProvider.MODID + ":solenoid"));
        ITEMS.register(ObjectNames.ITEM_COIL_ELEMENT, new Item().setCreativeTab(TAB).setUnlocalizedName("coilelement").setTextureName(ModProvider.MODID + ":coil"));
        ITEMS.register(ObjectNames.ITEM_PIPE, new Item().setCreativeTab(TAB).setUnlocalizedName("pipe").setTextureName(ModProvider.MODID + ":pipe"));
        ITEMS.register(ObjectNames.ITEM_SCREWDRIVER, new Item().setCreativeTab(TAB).setUnlocalizedName("screwdriver").setTextureName(ModProvider.MODID + ":screwdriver").setMaxStackSize(1).setFull3D());
        ITEMS.register(ObjectNames.ITEM_SOLDERING_IRON, new ItemHeatableEnergyTool().setCreativeTab(TAB).setUnlocalizedName("solderingiron").setTextureName(ModProvider.MODID + ":sldirn").setMaxStackSize(1).setFull3D());
        ITEMS.register(ObjectNames.ITEM_WELDER, new ItemHeatableEnergyTool().setCreativeTab(TAB).setUnlocalizedName("welder").setTextureName(ModProvider.MODID + ":welder").setMaxStackSize(1).setFull3D());
        ITEMS.register(ObjectNames.ITEM_HANDLE, new Item().setCreativeTab(TAB).setUnlocalizedName("handle").setTextureName(ModProvider.MODID + ":handle").setMaxStackSize(32));
        ITEMS.register(ObjectNames.ITEM_PORTAL_LINKER, new ItemPortalLinker());
        
        
        
        ITEMS.register(ObjectNames.ITEM_FAYALITE_INGOT, new Item(){
        	@Override
        	public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List l, boolean p_77624_4_) {
        		l.add(StatCollector.translateToLocal("ingot.info"));
        	}
        }.setCreativeTab(TAB).setUnlocalizedName("whiteironingotq").setTextureName(ModProvider.MODID + ":ingot"));
        ITEMS.register(ObjectNames.ITEM_FAYALITE_CRYSTAL, new Item(){
        	@Override
        	public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List l, boolean p_77624_4_) {
        		l.add(StatCollector.translateToLocal("ingot.info"));
        	}
        }.setCreativeTab(TAB).setUnlocalizedName("unhandledingot").setTextureName(ModProvider.MODID + ":ingotunhandled"));
        
        ITEMS.register(ObjectNames.ITEM_FAYALITE_PLATE, new Item(){
        	@Override
        	public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List l, boolean p_77624_4_) {
        		l.add(StatCollector.translateToLocal("ingot.info"));
        	}
        }.setCreativeTab(TAB).setUnlocalizedName("plate").setTextureName(ModProvider.MODID + ":plate"));
        
        ITEMS.register(ObjectNames.ITEM_EASTER_TOMATO, new ItemKyouko());
        
        
        EntityRegistry.registerModEntity(EntityBullet.class, ObjectNames.ENTITY_BULLET, 0, this, 200, 1, true);
    	
        
		ITEMS.register(ObjectNames.ITEM_LIQUID_CELL, ItemCell.instance());
		Iterator<Fluid> iter = FluidRegistry.getRegisteredFluids().values().iterator();
    	while(iter.hasNext()) ItemCell.registerFluid(iter.next());
    	
    	MinecraftForge.EVENT_BUS.register(new HandlerFluidRegistry());
    	
    	GameRegistry.addShapelessRecipe(new ItemStack(Items.experience_bottle, 5), Items.glass_bottle, Items.emerald, Items.ender_pearl);
    	GameRegistry.addSmelting(Items.flint, new ItemStack(ITEMS.get(ObjectNames.ITEM_SILICON)), 1);
    	GameRegistry.addSmelting(ITEMS.get(ObjectNames.ITEM_FAYALITE_CRYSTAL), new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)), 1);
    	GameRegistry.addRecipe(new HeatableRecipe());
    	GameRegistry.addShapelessRecipe(new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_CRYSTAL), 2), ITEMS.get(ObjectNames.ITEM_SILICON), Items.iron_ingot);
    	
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_BIOMETRIC_READER)), 1, 1, new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_B)), 
    																										   new ItemStack(Items.ender_pearl),
    																										   new ItemStack(ITEMS.get(ObjectNames.ITEM_BIOMETRIC_CARD)),
    																							   new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)),
    																										   new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT))));
    	
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID), 5), 0, 0, new ItemStack(Items.gold_nugget), 
				   new ItemStack(Items.gold_nugget),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)),
				   new ItemStack(Items.gold_nugget),
				   new ItemStack(Items.gold_nugget)));
    	
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_BASE), 3), 1, 3, new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID)), 
				   new ItemStack(Items.redstone),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)),
				   new ItemStack(Items.redstone),
				   new ItemStack(Items.gold_nugget)));
    
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_A), 1), 1, 2, new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID)), 
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_BASE)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_BASE)),
				   new ItemStack(Items.gold_nugget)));
    	
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_B), 1), 1, 2, new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID)), 
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_A)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_A)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_BASE))));
    	
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT), 1), 1, 0, null, 
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID)),
				   new ItemStack(Items.iron_ingot),
				   null,
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID))));
    	
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_IMPULSE_GENERATOR), 1), 1, 0, null, 
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_BASE)),
				   null,
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT))));
    	
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_ENDERCORE), 1), 1, 1, new ItemStack(Items.ender_pearl), 
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_A)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_B)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID))));
    	
    	SOLDERING_RECIPES.register(BasicSolderingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_PORTAL_LINKER), 1), 1, 1, new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_B)), 
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLENOID)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_CHIP_B)),
				   new ItemStack(ITEMS.get(ObjectNames.ITEM_ENDERCORE))));
    	
    	
    	
    	SOLDERING_RECIPES.register(new IntegerCardRecipe(  false, false)); //000
    	SOLDERING_RECIPES.register(new IntegerCardRecipe(  true,  false)); //010
    	SOLDERING_RECIPES.register(new BiometricCardRecipe(false, false)); //100
    	SOLDERING_RECIPES.register(new BiometricCardRecipe(true,  false)); //110
    	SOLDERING_RECIPES.register(new IntegerCardRecipe(  false, true));  //001
    	SOLDERING_RECIPES.register(new IntegerCardRecipe(  true,  true));  //011
    	SOLDERING_RECIPES.register(new BiometricCardRecipe(false, true));  //101
    	SOLDERING_RECIPES.register(new BiometricCardRecipe(true,  true));  //111
    	
    	SOLDERING_RECIPES.register(new PreventorCardRecipe(false));
    	SOLDERING_RECIPES.register(new PreventorCardRecipe(true));
    	
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE)), 0, "##", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_LIQUID_CELL), 2), 0, " # ", "#0#", " # ", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), '0', Blocks.glass_pane));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_ENERGY_CONTROLLER)), 2, "#C#", "CAC", "#C#", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), 'C', ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT), 'A', ITEMS.get(ObjectNames.ITEM_CHIP_A)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(ITEMS.get(ObjectNames.ITEM_PIPE), 5), 0, "#####", "     ", "#####", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_ISM)), 3, "#P#P#", "PAIAP", "##C##", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), 'P', ITEMS.get(ObjectNames.ITEM_PIPE), 'C', ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT), 'A', ITEMS.get(ObjectNames.ITEM_CHIP_A), 'I', ITEMS.get(ObjectNames.ITEM_IMPULSE_GENERATOR)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_FIELD_GENERATOR)), 6, "##P##", "ICABI", "##C##", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), 'P', ITEMS.get(ObjectNames.ITEM_PIPE), 'C', ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT), 'A', ITEMS.get(ObjectNames.ITEM_CHIP_B), 'I', ITEMS.get(ObjectNames.ITEM_ENDERCORE), 'B', ITEMS.get(ObjectNames.ITEM_BIOMETRIC_READER)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_TELEPORT)), 6, "#PPP#", "IABAI", "##C##", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), 'P', ITEMS.get(ObjectNames.ITEM_PIPE), 'C', ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT), 'A', ITEMS.get(ObjectNames.ITEM_CHIP_B), 'I', ITEMS.get(ObjectNames.ITEM_ENDERCORE), 'B', ITEMS.get(ObjectNames.ITEM_BIOMETRIC_READER)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_ELECTRIC_COIL)), 6, "CCCA#", "PPPBI", "CCCA#", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), 'P', ITEMS.get(ObjectNames.ITEM_PIPE), 'C', ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT), 'A', ITEMS.get(ObjectNames.ITEM_CHIP_B), 'I', ITEMS.get(ObjectNames.ITEM_IMPULSE_GENERATOR), 'B', ITEMS.get(ObjectNames.ITEM_BIOMETRIC_READER)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_TURRET)), 6, "P C##", "PPABI", "I#C##", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), 'P', ITEMS.get(ObjectNames.ITEM_PIPE), 'C', ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT), 'A', ITEMS.get(ObjectNames.ITEM_CHIP_B), 'I', ITEMS.get(ObjectNames.ITEM_IMPULSE_GENERATOR), 'B', ITEMS.get(ObjectNames.ITEM_BIOMETRIC_READER)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_HOLO_TEXT)), 3, "#PS#", "#IG#", "#CA#", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), 'P', ITEMS.get(ObjectNames.ITEM_PIPE), 'C', ITEMS.get(ObjectNames.ITEM_COIL_ELEMENT), 'A', ITEMS.get(ObjectNames.ITEM_CHIP_A), 'I', ITEMS.get(ObjectNames.ITEM_IMPULSE_GENERATOR), 'S', Items.sign, 'G', Items.glowstone_dust));
    	//WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_SPEAKER)), 4, "L#W","#G#","W#L", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE), 'G', new ItemStack(Blocks.jukebox), 'W', new ItemStack(Items.dye, 1, 4), 'L', ITEMS.get(ObjectNames.ITEM_CHIP_BASE)));
    	
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_PLATED_SLAB), 6), 0, "###", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_PLATED_HULL), 24), 0, "###", "###", "###", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE)));
    	WELDING_RECIPES.register(BasicWeldingRecipe.create(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_PLATED_STAIRS), 12), 0, "#  ", "## ", "###", '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_PLATE)));
    	
    	GameRegistry.addRecipe(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_SOLDERING)),  "A A", "#C#", "###", 'A', Items.iron_ingot, 'C', Blocks.crafting_table, '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT));
    	GameRegistry.addRecipe(new ItemStack(BLOCKS.get(ObjectNames.BLOCK_WELDING)),  "A A", "#C#", "###", 'A', Items.diamond, 'C', Blocks.crafting_table, '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT));
    	
    	GameRegistry.addRecipe(new ItemStack(ITEMS.get(ObjectNames.ITEM_SCREWDRIVER)),  "A##", 'A', ITEMS.get(ObjectNames.ITEM_HANDLE), '#', Items.iron_ingot);
    	GameRegistry.addRecipe(new ItemStack(ITEMS.get(ObjectNames.ITEM_WELDER)),  "A##", "  D", 'A', ITEMS.get(ObjectNames.ITEM_HANDLE), '#', Items.iron_ingot, 'D', Items.diamond);
    	GameRegistry.addRecipe(new ItemStack(ITEMS.get(ObjectNames.ITEM_SOLDERING_IRON)),  "A##", "  #", 'A', ITEMS.get(ObjectNames.ITEM_HANDLE), '#', Items.iron_ingot);
    	
    	
    	GameRegistry.addRecipe(new ItemStack(ITEMS.get(ObjectNames.ITEM_HANDLE)),  "A##", 'A', new ItemStack(Items.dye, 1, 10), '#', ITEMS.get(ObjectNames.ITEM_FAYALITE_INGOT));
    	
    	WeightedRandomChestContent c = new WeightedRandomChestContent(ITEMS.get(ObjectNames.ITEM_EASTER_TOMATO), 0, 1, 2, 30);
    	ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(c);
    	ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(c);
    	ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(c);
    	ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(c);
    	ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(c);
    	ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(c);
    	ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(c);
    	
    	proxy.registerRenderers();
    	PacketDispatcher.registerPackets();
    	proxy.init();
    	
    	
    	//****** Brigde Loading Block ******//
    	Bridge.add(Bridge.MODID, MODID);
    	Bridge.add(Bridge.VERSION, VERSION);
    	Bridge.add(Bridge.TAB, TAB);
    	Bridge.add(Bridge.ITEMS, ITEMS);
    	Bridge.add(Bridge.BLOCKS, BLOCKS);
    	Bridge.add(Bridge.TILE_ENTITIES, TILE_ENTITIES);
    	Bridge.add(Bridge.SOLDERING_RECIPES, SOLDERING_RECIPES);
    	Bridge.add(Bridge.WELDING_RECIPES, WELDING_RECIPES);
    	//** End of Brigde Loading Block ***//
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
