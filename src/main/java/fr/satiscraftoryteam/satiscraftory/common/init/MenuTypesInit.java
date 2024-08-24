package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.BuilderToolMenu;
import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Menu;
import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MenuTypesInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, SatisCraftory.MODID);


    public static final Supplier<MenuType<MinerMk1Menu>> MINER_MK1_MENU = MENUS.register("miner_mk1_menu", () -> new MenuType<>(MinerMk1Menu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<SmelterMenu>> SMELTER_MENU = MENUS.register( "smelter_menu", () -> new MenuType<>(SmelterMenu::new, FeatureFlags.DEFAULT_FLAGS));

   public static final Supplier<MenuType<BuilderToolMenu>> BUILDER_TOOL_MENU = MENUS.register("builder_tool", () -> new MenuType<>(BuilderToolMenu::new, FeatureFlags.DEFAULT_FLAGS));

//    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
//        return MENUS.register(name, () -> IForgeMenuType.create(factory));
//    }

}
