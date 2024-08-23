package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.BuilderToolItem;
import fr.satiscraftoryteam.satiscraftory.common.item.XenoZapperItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SatisCraftory.MODID);

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }

    public static final DeferredHolder<Item, Item> REGLE_HELP_DEV = ITEMS.register("regle_help_dev",
            ()-> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> IRON_RESIDUE = ITEMS.register("iron_residue",
            ()-> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> COPPER_RESIDUE = ITEMS.register("copper_residue",
            ()-> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> PALEBERRY = ITEMS.register("paleberry",
            ()-> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> XENO_ZAPPER = ITEMS.register("xeno_zapper",
            ()-> new XenoZapperItem(new Item.Properties().fireResistant().stacksTo(1)));

    public static final DeferredHolder<Item, Item> BUILDER_TOOL = ITEMS.register("builder_tool",
            ()-> new BuilderToolItem(new Item.Properties().fireResistant().stacksTo(1)));

    public static final DeferredHolder<Item, Item> POWER_SHARD = ITEMS.register("power_shard",
            ()-> new Item(new Item.Properties().fireResistant().stacksTo(16)));
}
