package fr.satiscraftoryteam.satiscraftory.common.tileentity.base;

import fr.satiscraftoryteam.satiscraftory.common.registration.TileEntityDeferredHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TickableTileEntity<BE extends BlockEntity> extends TileEntityUpdatable<BE>{
    public TickableTileEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tickClient(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        tile.onClientTick(level, pos, state, tile);
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        tile.onServerTick(level, pos, state, tile);
    }

    public void onClientTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile){

    }

    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile){

    }
}
