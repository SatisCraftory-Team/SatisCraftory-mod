package fr.satiscraftoryteam.satiscraftory.common.registration;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BlockRegistryObject<BLOCK extends Block, ITEM extends Item> extends DoubleWrappedDeferredHolder<Block, BLOCK, Item, ITEM> {

    public BlockRegistryObject(DeferredHolder<Block, BLOCK> blockRegistryObject, DeferredHolder<Item, ITEM> itemRegistryObject) {
        super(blockRegistryObject, itemRegistryObject);
    }

    @NotNull
    public BLOCK getBlock() {
        return getPrimary();
    }

    @NotNull
    public ITEM asItem() {
        return getSecondary();
    }

//    public BlockRegistryObject<BLOCK, ITEM> forItemHolder(Consumer<ItemRegistryObject<ITEM>> consumer) {
//        if (secondaryRO instanceof UI<ITEM> itemHolder) {
//            consumer.accept(itemHolder);
//            return this;
//        }
//        throw new IllegalStateException("Called method requires an ItemRegistryObject");
//    }
}