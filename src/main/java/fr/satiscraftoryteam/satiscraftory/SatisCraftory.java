package fr.satiscraftoryteam.satiscraftory;

import fr.satiscraftoryteam.satiscraftory.client.renderer.blocks.ConveyorRenderer;
import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Screen;
import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterScreen;
import fr.satiscraftoryteam.satiscraftory.common.init.*;
import fr.satiscraftoryteam.satiscraftory.common.network.ModPackets;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SatisCraftory.MODID)
public class SatisCraftory {
    public static final String MODID = "satiscraftory";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static ModPackets packetHandler = new ModPackets();

    public SatisCraftory(IEventBus modEventBus, Dist dist, ModContainer container) {
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerRenderers);

        packetHandler.register();
        
        CreativeModeTabsInit.register(modEventBus);
        BlockInit.register(modEventBus);
        ItemInit.register(modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);
        TileEntityInit.BLOCK_ENTITIES.register(modEventBus);
        MenuTypesInit.MENUS.register(modEventBus);
        StructuresInit.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);

        //GeckoLib.initialize();
        LOGGER.info("ici, c'est le goulag, préparez vous au combat");
    }

    public void clientSetup(FMLClientSetupEvent e) {
        MenuScreens.register(MenuTypesInit.MINER_MK1_MENU.get(), MinerMk1Screen::new);
        MenuScreens.register(MenuTypesInit.SMELTER_MENU.get(), SmelterScreen::new);
    }

    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TileEntityInit.CONVEYOR.get(), ConveyorRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.CONVEYOR_FULL.get(), ConveyorRenderer::new);
    }

    public static Item.Properties geBaseProperties() {
        return new Item.Properties().tab(TAB);
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("satiscraftory_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockInit.LOGO.asItem());
        }
    };
}
