package fr.satiscraftoryteam.satiscraftory.common.registration;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class DeferredBlockEntityCapabilityRegisterData<T, C, BE extends BlockEntity> {
    private final BlockCapability<T, C> capability;
    private final ICapabilityProvider<BE, C, T> capabilityProvider;

    public DeferredBlockEntityCapabilityRegisterData(BlockCapability<T, C> capability, ICapabilityProvider<BE, C, T> capabilityProvider) {
        this.capability = capability;
        this.capabilityProvider = capabilityProvider;
    }

    public BlockCapability<T, C> getCapability() {
        return capability;
    }

    public ICapabilityProvider<BE, C, T> getCapabilityProvider() {
        return capabilityProvider;
    }

    @Override
    public int hashCode() {
        return capability.hashCode();
    }

    public void registerCapabilityProviders(RegisterCapabilitiesEvent event, BlockEntityType<BE> blockEntityType) {
        event.registerBlockEntity(capability, blockEntityType, capabilityProvider);
    }
}
