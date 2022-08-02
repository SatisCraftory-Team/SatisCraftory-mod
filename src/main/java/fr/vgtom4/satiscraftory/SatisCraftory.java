package fr.vgtom4.satiscraftory;

import fr.vgtom4.satiscraftory.client.renderer.blocks.ConveyorRenderer;
import fr.vgtom4.satiscraftory.common.block.PaleBerryBushBlock;
import fr.vgtom4.satiscraftory.common.init.TileEntityInit;
import fr.vgtom4.satiscraftory.common.init.BlockInit;
import fr.vgtom4.satiscraftory.common.init.ItemInit;
import fr.vgtom4.satiscraftory.common.init.MenuTypesInit;
import fr.vgtom4.satiscraftory.client.screen.MinerMk1Screen;
import fr.vgtom4.satiscraftory.common.network.PacketHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(SatisCraftory.MODID)
public class SatisCraftory {
    public static final String MODID = "satiscraftory";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Item.Properties geBaseProperties() {
        return new Item.Properties().tab(TAB);
    }

    public static PacketHandler packetHandler;
    public SatisCraftory() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::registerRenderers);

        packetHandler = new PacketHandler();
        packetHandler.init();

        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCKS_TEST.register(bus);
        TileEntityInit.TILE_ENTITY_TYPES.register(bus);

        ItemInit.ITEMS.register(bus);
        TileEntityInit.BLOCK_ENTITIES.register(bus);
        MenuTypesInit.MENUS.register(bus);

        GeckoLib.initialize();
        System.out.println("ici, c'est le goulag, préparez vous au combat.");
    }

    public void setup(FMLCommonSetupEvent e){
    }

    public void clientSetup(FMLClientSetupEvent e) {
        MenuScreens.register(MenuTypesInit.MINER_MK1_MENU.get(), MinerMk1Screen::new);
    }

    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TileEntityInit.CONVEYOR.get(), ConveyorRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.CONVEYOR_FULL.get(), ConveyorRenderer::new);
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("satiscraftoryTAB") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.LOGO_ITEM.get());
        }
    };
}
