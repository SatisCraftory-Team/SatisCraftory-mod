package fr.vgtom4.satiscraftory.common.tileentity.base;

import fr.vgtom4.satiscraftory.common.interfaces.IHasTileEntity;
import fr.vgtom4.satiscraftory.common.registry.TileEntityRegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

//TODO: implement here capabilities
public abstract class TileEntityBase extends TileEntityUpdatable {

    public TileEntityBase(Block blockProvider, BlockPos pos, BlockState state) {
        super(((IHasTileEntity<? extends BlockEntity>) blockProvider).getTileType(), pos, state);
    }


    public TileEntityBase(TileEntityRegistryObject<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tickClient(Level level, BlockPos pos, BlockState state, TileEntityBase tile) {

    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, TileEntityBase tile) {

    }
}
