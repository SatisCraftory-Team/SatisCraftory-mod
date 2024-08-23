package fr.satiscraftoryteam.satiscraftory.common.builder;

import fr.satiscraftoryteam.satiscraftory.common.registration.WrappedDeferredHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class WrappedDeferredRegister<T> extends DeferredRegister<T> {


    private final Function<ResourceKey<T>, ? extends WrappedDeferredHolder<T, ?>> holderCreator;

    public WrappedDeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        this(registryKey, namespace, WrappedDeferredHolder::new);
    }

    public WrappedDeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace,
                                   Function<ResourceKey<T>, ? extends WrappedDeferredHolder<T, ? extends T>> holderCreator) {
        super(registryKey, namespace);
        this.holderCreator = holderCreator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> WrappedDeferredHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> func) {
        return (WrappedDeferredHolder<T, I>) super.register(name, func);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> WrappedDeferredHolder<T, I> register(String name, Supplier<? extends I> sup) {
        return (WrappedDeferredHolder<T, I>) super.register(name, sup);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <I extends T> WrappedDeferredHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation key) {
        return (WrappedDeferredHolder<T, I>) holderCreator.apply(ResourceKey.create(registryKey, key));
    }
}
