package fr.satiscraftoryteam.satiscraftory.common.registration;


import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DoubleWrappedDeferredHolder<PRIMARY_REGISTRY, PRIMARY extends PRIMARY_REGISTRY, SECONDARY_REGISTRY, SECONDARY extends SECONDARY_REGISTRY> implements INamedEntry {

    protected final DeferredHolder<PRIMARY_REGISTRY, PRIMARY> primaryRO;
    protected final DeferredHolder<SECONDARY_REGISTRY, SECONDARY> secondaryRO;

    public DoubleWrappedDeferredHolder(DeferredHolder<PRIMARY_REGISTRY, PRIMARY> primaryRO, DeferredHolder<SECONDARY_REGISTRY, SECONDARY> secondaryRO) {
        this.primaryRO = primaryRO;
        this.secondaryRO = secondaryRO;
    }

    public PRIMARY getPrimary() {
        return primaryRO.get();
    }

    public SECONDARY getSecondary() {
        return secondaryRO.get();
    }

    public ResourceLocation getId() {
        return primaryRO.getId();
    }

    public String getName() {
        return INamedEntry.super.getName();
    }
}