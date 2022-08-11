package fr.satiscraftoryteam.satiscraftory.common.registry;

import net.minecraftforge.registries.RegistryObject;

public class DoubleWrappedRegistryObject<PRIMARY, SECONDARY> {

    protected final RegistryObject<PRIMARY> primaryRO;
    protected final RegistryObject<SECONDARY> secondaryRO;

    public DoubleWrappedRegistryObject(RegistryObject<PRIMARY> primaryRO, RegistryObject<SECONDARY> secondaryRO) {
        this.primaryRO = primaryRO;
        this.secondaryRO = secondaryRO;
    }

    public PRIMARY getPrimary() {
        return primaryRO.get();
    }

    public SECONDARY getSecondary() {
        return secondaryRO.get();
    }

    public String getInternalRegistryName() {
        return primaryRO.getId().getPath();
    }
}