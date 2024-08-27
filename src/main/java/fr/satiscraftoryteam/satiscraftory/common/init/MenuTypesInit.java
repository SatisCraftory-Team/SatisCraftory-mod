package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.BuilderToolMenu;
import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Menu;
import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MenuTypesInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, SatisCraftory.MODID);


    public static final Supplier<MenuType<MinerMk1Menu>> MINER_MK1_MENU = MENUS.register("miner_mk1_menu", () -> IMenuTypeExtension.create(MinerMk1Menu::new));
    public static final Supplier<MenuType<SmelterMenu>> SMELTER_MENU = MENUS.register( "smelter_menu", () -> IMenuTypeExtension.create(SmelterMenu::new));

   public static final Supplier<MenuType<BuilderToolMenu>> BUILDER_TOOL_MENU = MENUS.register("builder_tool", () -> IMenuTypeExtension.create(BuilderToolMenu::new));

//    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
//        return MENUS.register(name, () -> IForgeMenuType.create(factory));
//    }

}
