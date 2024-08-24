package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.function.Supplier;

public class CreativeModeTabsInit {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SatisCraftory.MODID);

    public static final Supplier<CreativeModeTab> SATISCRAFTORY_TAB = CREATIVE_MODE_TABS.register(
            "satiscraftory_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BlockInit.MANU.getBlock().asItem()))
                    .title(Component.translatable("creativetab.satiscraftory.global"))
                    .displayItems((itemDisplayParameters, output) -> {
                        // Add all blocks to the creative tab
                        BlockInit.BLOCKS.getSecondaryEntries().stream()
                                // Blocks that should not be displayed in the creative tab
                                .filter(item ->
                                    !Arrays.asList(
                                            BlockInit.LOGO.asItem(),
                                            BlockInit.BOUNDING_BLOCK.asItem(),
                                            BlockInit.MINER_MK1.asItem(),
                                            BlockInit.SMELTER.asItem(),
                                            BlockInit.ELIOCUBE.asItem()
                                    ).contains(item.get())
                                )
                                .forEach(item -> output.accept(item.get()));

                        // Add all items to the creative tab
                        ItemInit.ITEMS.getEntries().forEach(item -> {
                            output.accept(item.get());
                        });
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
