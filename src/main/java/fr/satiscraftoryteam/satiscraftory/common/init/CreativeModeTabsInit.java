package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreativeModeTabsInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SatisCraftory.MODID);

    public static final Supplier<CreativeModeTab> SATISCRAFTORY_TAB = CREATIVE_MODE_TABS.register(
            "satiscraftory_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BlockInit.LOGO.getBlock().asItem()))
                    .title(Component.translatable("creativetab.satiscraftory.global"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BlockInit.MANU.asItem());
                        output.accept(BlockInit.ELIOCUBE.asItem());
                        output.accept(BlockInit.LOGO.asItem());
                        output.accept(BlockInit.PALE_BERRY_BUSH.asItem());
                        output.accept(BlockInit.CONVEYOR_MERGER.asItem());
                        output.accept(BlockInit.CONVEYOR_INPUT_PART.asItem());
                        output.accept(BlockInit.CONVEYOR_OUTPUT_PART.asItem());
                        output.accept(BlockInit.MINER_MK1.asItem());
                        output.accept(BlockInit.SMELTER.asItem());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
