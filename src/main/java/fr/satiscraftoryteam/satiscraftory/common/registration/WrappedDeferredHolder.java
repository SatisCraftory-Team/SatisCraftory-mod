package fr.satiscraftoryteam.satiscraftory.common.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class WrappedDeferredHolder<R, T extends R> extends DeferredHolder<R, T> implements INamedEntry {

    public WrappedDeferredHolder(ResourceKey<? extends Registry<R>> registryKey, ResourceLocation valueName) {
        this(ResourceKey.create(registryKey, valueName));
    }

    public WrappedDeferredHolder(ResourceKey<R> key) {
        super(key);
    }

    @Override
    public String getName() {
        return INamedEntry.super.getName();
    }
}
