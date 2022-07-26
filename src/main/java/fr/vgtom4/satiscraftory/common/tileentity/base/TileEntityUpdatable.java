package fr.vgtom4.satiscraftory.common.tileentity.base;

import fr.vgtom4.satiscraftory.common.interfaces.ITileWrapper;
import fr.vgtom4.satiscraftory.common.registry.TileEntityRegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileEntityUpdatable extends BlockEntity implements ITileWrapper {


    public TileEntityUpdatable(TileEntityRegistryObject<?> type, BlockPos pos, BlockState state) {
        super(type.get(), pos, state);
    }

    public boolean isRemote() {
        return getLevel().isClientSide();
    }
}
