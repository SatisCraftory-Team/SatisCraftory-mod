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
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(SatisCraftory.MODID)
public class SatisCraftory {
    public static final String MODID = "satiscraftory";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Item.Properties geBaseProperties() {
        return new Item.Properties().tab(TAB);
    }

    public static ModPackets packetHandler = new ModPackets();
    public SatisCraftory() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::clientSetup);
        bus.addListener(this::registerRenderers);


        packetHandler.register();

        BlockInit.register(bus);
        ItemInit.register(bus);
        TileEntityInit.TILE_ENTITY_TYPES.register(bus);
        TileEntityInit.BLOCK_ENTITIES.register(bus);
        MenuTypesInit.MENUS.register(bus);

        StructuresInit.DEFERRED_REGISTRY_STRUCTURE.register(bus);

        GeckoLib.initialize();

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

    public static final CreativeModeTab TAB = new CreativeModeTab("satiscraftory_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockInit.LOGO.asItem());
        }
    };
}
