package fr.satiscraftoryteam.satiscraftory.common.registration;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

public class TileEntityDeferredHolder<BE extends BlockEntity> extends WrappedDeferredHolder<BlockEntityType<BE>, BlockEntityType<BE>> {

    @Nullable
    private BlockEntityTicker<BE> clientTicker;
    @Nullable
    private BlockEntityTicker<BE> serverTicker;

    public TileEntityDeferredHolder(DeferredHolder<BlockEntityType<BE>, BlockEntityType<BE>> deferredHolder) {
        super(deferredHolder);
    }

    //Internal use only, overwrite the registry object
    public TileEntityDeferredHolder<BE> setDeferredHolder(DeferredHolder<BlockEntityType<BE>, BlockEntityType<BE>> deferredHolder) {
        this.deferredHolder = deferredHolder;
        return this;
    }

    //Internal use only
    public TileEntityDeferredHolder<BE> clientTicker(BlockEntityTicker<BE> ticker) {
        clientTicker = ticker;
        return this;
    }

    //Internal use only
    public TileEntityDeferredHolder<BE> serverTicker(BlockEntityTicker<BE> ticker) {
        serverTicker = ticker;
        return this;
    }

    @Nullable
    public BlockEntityTicker<BE> getTicker(boolean isClient) {
        return isClient ? clientTicker : serverTicker;
    }

}
