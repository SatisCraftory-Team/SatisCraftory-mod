package fr.satiscraftoryteam.satiscraftory.common.registration;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class TileEntityDeferredHolder<BE extends BlockEntity> extends WrappedDeferredHolder<BlockEntityType<?>, BlockEntityType<BE>> {

    @Nullable
    private BlockEntityTicker<BE> clientTicker;
    @Nullable
    private BlockEntityTicker<BE> serverTicker;

    public TileEntityDeferredHolder(ResourceKey<BlockEntityType<?>> deferredHolder) {
        super(deferredHolder);
    }

    public void tickers(@Nullable BlockEntityTicker<BE> clientTicker, @Nullable BlockEntityTicker<BE> serverTicker) {
        this.clientTicker = clientTicker;
        this.serverTicker = serverTicker;
    }

    @Nullable
    public BlockEntityTicker<BE> getTicker(boolean isClient) {
        return isClient ? clientTicker : serverTicker;
    }

}
