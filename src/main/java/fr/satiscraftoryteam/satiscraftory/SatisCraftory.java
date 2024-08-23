package fr.satiscraftoryteam.satiscraftory;

import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Menu;
import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterScreen;
import fr.satiscraftoryteam.satiscraftory.common.init.*;
import fr.satiscraftoryteam.satiscraftory.common.network.ModPackets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SatisCraftory.MODID)
public class SatisCraftory {
    public static final String MODID = "satiscraftory";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static SatisCraftory instance;

    private final ModPackets packetHandler;

    public SatisCraftory(IEventBus modEventBus, Dist dist, ModContainer container) {
        instance = this;
        //modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(this::registerScreens);
        packetHandler = new ModPackets(modEventBus);
        
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

    // Event is listened to on the mod event bus
    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypesInit.MINER_MK1_MENU.get(), MinerMk1Menu::new);
        event.register(MenuTypesInit.SMELTER_MENU.get(), SmelterScreen::new);
    }

//    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.registerBlockEntityRenderer(TileEntityInit.CONVEYOR.get(), ConveyorRenderer::new);
//        event.registerBlockEntityRenderer(TileEntityInit.CONVEYOR_FULL.get(), ConveyorRenderer::new);
//    }

    public static Item.Properties geBaseProperties() {
        return new Item.Properties().tab(TAB);
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("satiscraftory_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockInit.LOGO.asItem());
        }
    };

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, path);
    }

    public static ModPackets packetHandler() {
        return instance.packetHandler;
    }
}
