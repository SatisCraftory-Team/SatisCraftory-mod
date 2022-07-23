package fr.vgtom4.satiscraftory;

import fr.vgtom4.satiscraftory.init.BlockEntityInit;
import fr.vgtom4.satiscraftory.init.BlockInit;
import fr.vgtom4.satiscraftory.init.ItemInit;
import fr.vgtom4.satiscraftory.init.MenuTypesInit;
import fr.vgtom4.satiscraftory.screen.MinerMk1Screen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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


    public static VoxelShape calculateShapes(Direction to, VoxelShape shape) {
        final VoxelShape[] buffer = { shape, Shapes.empty() };

        final int times = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1],
                    Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }


    public static final CreativeModeTab TAB = new CreativeModeTab("satiscraftoryTAB") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.XENO_ZAPPER.get());
        }
    };
}
