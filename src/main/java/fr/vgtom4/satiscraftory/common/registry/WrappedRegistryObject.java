package fr.vgtom4.satiscraftory.common.registry;

import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class WrappedRegistryObject<T> implements Supplier<T> {
    protected RegistryObject<T> registryObject;

    protected WrappedRegistryObject(RegistryObject<T> registryObject) {
        this.registryObject = registryObject;
    }

    @Override
    public T get() {
        return registryObject.get();
    }


    public String getInternalRegistryName() {
        return registryObject.getId().getPath();
    }
}
