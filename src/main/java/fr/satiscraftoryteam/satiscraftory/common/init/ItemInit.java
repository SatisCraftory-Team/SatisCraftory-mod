package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.ConveyorDebugger;
import fr.satiscraftoryteam.satiscraftory.common.item.BuilderToolItem;
import fr.satiscraftoryteam.satiscraftory.common.item.XenoZapperItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SatisCraftory.MODID);

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }

    public static final RegistryObject<Item> REGLE_HELP_DEV = ITEMS.register("regle_help_dev",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB)));

    public static final RegistryObject<Item> IRON_RESIDUE = ITEMS.register("iron_residue",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB)));

    public static final RegistryObject<Item> COPPER_RESIDUE = ITEMS.register("copper_residue",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB)));

    public static final RegistryObject<Item> PALEBERRY = ITEMS.register("paleberry",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB).food(FoodInit.PALEBERRY)));

    public static final RegistryObject<Item> XENO_ZAPPER = ITEMS.register("xeno_zapper",
            ()-> new XenoZapperItem(new Item.Properties().tab(SatisCraftory.TAB).fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> BUILDER_TOOL = ITEMS.register("builder_tool",
            ()-> new BuilderToolItem(new Item.Properties().tab(SatisCraftory.TAB).fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> POWER_SHARD = ITEMS.register("power_shard",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB).fireResistant().stacksTo(16)));

    public static final RegistryObject<Item> CONVEYOR_DEBUGGER = ITEMS.register("conveyor_debugger",
            () -> new ConveyorDebugger(new Item.Properties().tab(SatisCraftory.TAB).stacksTo(1)));
}
