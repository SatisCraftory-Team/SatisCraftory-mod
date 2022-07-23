package fr.vgtom4.satiscraftory.init;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.item.custom.EliocubeItem;
import fr.vgtom4.satiscraftory.item.custom.MinerMk1Item;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemInit {
    private ItemInit(){
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SatisCraftory.MODID);

    public static final RegistryObject<Item> IRON_RESIDUE = ITEMS.register("iron_residue",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB)));

    public static final RegistryObject<Item> XENO_ZAPPER = ITEMS.register("xeno_zapper",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB).fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> POWER_SHARD = ITEMS.register("power_shard",
            ()-> new Item(new Item.Properties().tab(SatisCraftory.TAB).fireResistant().stacksTo(16)));

    public static final RegistryObject<Item> ELIOCUBE_ITEM = ITEMS.register("eliocube",
            () -> new EliocubeItem(BlockInit.ELIOCUBE.get(), new Item.Properties().tab(SatisCraftory.TAB)));

    public static final RegistryObject<Item> MINER_MK1_ITEM = ITEMS.register("miner_mk1",
            () -> new MinerMk1Item(BlockInit.MINER_MK1.get(), new Item.Properties().tab(SatisCraftory.TAB)));

}
