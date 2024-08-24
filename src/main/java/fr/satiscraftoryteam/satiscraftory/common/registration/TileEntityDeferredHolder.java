package fr.satiscraftoryteam.satiscraftory.common.registration;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class TileEntityDeferredHolder<BE extends BlockEntity> extends WrappedDeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> {

    @Nullable
    private BlockEntityTicker<BE> clientTicker;
    @Nullable
    private BlockEntityTicker<BE> serverTicker;

    private Set<DeferredBlockEntityCapabilityRegisterData<?, ?, BE>> capabilityProviders;

    public TileEntityDeferredHolder(ResourceKey<BlockEntityType<?>> deferredHolder) {
        super(deferredHolder);
    }

    public void tickers(@Nullable BlockEntityTicker<BE> clientTicker, @Nullable BlockEntityTicker<BE> serverTicker) {
        this.clientTicker = clientTicker;
        this.serverTicker = serverTicker;
    }

    public void capabilityProviders(Set<DeferredBlockEntityCapabilityRegisterData<?, ?, BE>> capabilityProviders) {
        this.capabilityProviders = capabilityProviders;
    }

    public void registerCapabilityProviders(RegisterCapabilitiesEvent event) {
        for (DeferredBlockEntityCapabilityRegisterData<?, ?, BE> capabilityProvider : capabilityProviders) {
            capabilityProvider.registerCapabilityProviders(event, this.get());
        }
    }

    @Nullable
    public BlockEntityTicker<BE> getTicker(boolean isClient) {
        return isClient ? clientTicker : serverTicker;
    }

}
