package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.BuilderToolItem;
import fr.satiscraftoryteam.satiscraftory.common.item.XenoZapperItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ItemInit {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SatisCraftory.MODID);

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }

    public static final DeferredItem<Item> REGLE_HELP_DEV = ITEMS.register("regle_help_dev",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> IRON_RESIDUE = ITEMS.register("iron_residue",
            ()-> new Item(new Item.Properties().stacksTo(500)));

    public static final DeferredItem<Item> COPPER_RESIDUE = ITEMS.register("copper_residue",
            ()-> new Item(new Item.Properties().stacksTo(500)));

    public static final DeferredItem<Item> PALEBERRY = ITEMS.register("paleberry",
            ()-> new Item(new Item.Properties().food(FoodInit.PALEBERRY)));

    public static final DeferredItem<Item> XENO_ZAPPER = ITEMS.register("xeno_zapper",
            ()-> new XenoZapperItem(new Item.Properties().fireResistant().stacksTo(1)));

    public static final DeferredItem<Item> BUILDER_TOOL = ITEMS.register("builder_tool",
            ()-> new BuilderToolItem(new Item.Properties().fireResistant().stacksTo(1)));

    public static final DeferredItem<Item> POWER_SHARD = ITEMS.register("power_shard",
            ()-> new Item(new Item.Properties().fireResistant().stacksTo(16)));
}
