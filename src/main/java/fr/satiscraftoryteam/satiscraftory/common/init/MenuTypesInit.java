package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.BuilderToolMenu;
import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Menu;
import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypesInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SatisCraftory.MODID);

    public static final RegistryObject<MenuType<MinerMk1Menu>> MINER_MK1_MENU = registerMenuType(MinerMk1Menu::new, "miner_mk1_menu");

    public static final RegistryObject<MenuType<SmelterMenu>> SMELTER_MENU = registerMenuType(SmelterMenu::new, "smelter_menu");

    public static final RegistryObject<MenuType<BuilderToolMenu>> BUILDER_TOOL_MENU = registerMenuType(BuilderToolMenu::new, "builder_tool");

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    /*
    public static void register(IEventBus bus){
        MENUS.register(bus);
    }*/

}
