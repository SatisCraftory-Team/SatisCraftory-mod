package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.EliocubeItem;
import fr.satiscraftoryteam.satiscraftory.common.item.LogoItem;
import fr.satiscraftoryteam.satiscraftory.common.item.XenoZapperItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SatisCraftory.MODID);

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }

    public static final RegistryObject<Item> IRON_RESIDUE = ITEMS.register("iron_residue",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB)));

    public static final RegistryObject<Item> COPPER_RESIDUE = ITEMS.register("copper_residue",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB)));

    public static final RegistryObject<Item> PALEBERRY = ITEMS.register("paleberry",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB).food(FoodInit.PALEBERRY)));

    public static final RegistryObject<Item> XENO_ZAPPER = ITEMS.register("xeno_zapper",
            ()-> new XenoZapperItem(new Item.Properties().tab(SatisCraftory.TAB).fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> POWER_SHARD = ITEMS.register("power_shard",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB).fireResistant().stacksTo(16)));
}
