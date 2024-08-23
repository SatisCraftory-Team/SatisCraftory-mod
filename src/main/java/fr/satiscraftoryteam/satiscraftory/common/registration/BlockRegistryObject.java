package fr.satiscraftoryteam.satiscraftory.common.registration;

import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BlockRegistryObject<PRIMARY_REGISTRY, PRIMARY extends PRIMARY_REGISTRY, SECONDARY_REGISTRY, SECONDARY extends SECONDARY_REGISTRY> extends DoubleWrappedDeferredHolder<PRIMARY_REGISTRY, PRIMARY, SECONDARY_REGISTRY, SECONDARY> {

    public BlockRegistryObject(DeferredHolder<PRIMARY_REGISTRY, PRIMARY> blockRegistryObject, DeferredHolder<SECONDARY_REGISTRY, SECONDARY> itemRegistryObject) {
        super(blockRegistryObject, itemRegistryObject);
    }

    @NotNull
    public PRIMARY getBlock() {
        return getPrimary();
    }

    @NotNull
    public SECONDARY asItem() {
        return getSecondary();
    }
}