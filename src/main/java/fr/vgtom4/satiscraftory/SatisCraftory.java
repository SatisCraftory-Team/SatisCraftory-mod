package fr.vgtom4.satiscraftory;

import fr.vgtom4.satiscraftory.common.init.BlockEntityInit;
import fr.vgtom4.satiscraftory.common.init.BlockInit;
import fr.vgtom4.satiscraftory.common.init.ItemInit;
import fr.vgtom4.satiscraftory.common.init.MenuTypesInit;
import fr.vgtom4.satiscraftory.client.screen.MinerMk1Screen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(SatisCraftory.MODID)
public class SatisCraftory {
    public static final String MODID = "satiscraftory";

    public SatisCraftory() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);

        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        MenuTypesInit.MENUS.register(bus);

        GeckoLib.initialize();
        System.out.println("ici, c'est le goulag, préparez vous au combat.");
    }

    public void setup(FMLCommonSetupEvent e){
    }

    public void clientSetup(FMLClientSetupEvent e) {
        MenuScreens.register(MenuTypesInit.MINER_MK1_MENU.get(), MinerMk1Screen::new);
    }


    public static final CreativeModeTab TAB = new CreativeModeTab("satiscraftoryTAB") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.XENO_ZAPPER.get());
        }
    };
}
