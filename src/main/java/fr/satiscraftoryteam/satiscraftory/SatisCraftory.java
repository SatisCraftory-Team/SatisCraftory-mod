package fr.satiscraftoryteam.satiscraftory;

import fr.satiscraftoryteam.satiscraftory.common.init.*;
import fr.satiscraftoryteam.satiscraftory.common.network.ModPackets;
import fr.satiscraftoryteam.satiscraftory.common.registration.ModProcessors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
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

        packetHandler = new ModPackets(modEventBus);
        
        CreativeModeTabsInit.register(modEventBus);

        ItemInit.register(modEventBus);
        BlockInit.register(modEventBus);
        RecipeInit.register(modEventBus);

        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);
        TileEntityInit.BLOCK_ENTITIES.register(modEventBus);
        MenuTypesInit.MENUS.register(modEventBus);
        StructuresInit.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);

        ModProcessors.REGISTRY.register(modEventBus);

        LOGGER.info("ici, c'est le goulag, préparez vous au combat");
    }

    public static Item.Properties geBaseProperties() {
        return new Item.Properties();
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, path);
    }

    public static ModPackets packetHandler() {
        return instance.packetHandler;
    }
}
