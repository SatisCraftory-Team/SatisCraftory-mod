package fr.vgtom4.satiscraftory.common.interfaces;

import fr.vgtom4.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.vgtom4.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.vgtom4.satiscraftory.common.tileentity.base.TickableTileEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface IHasTickableTileEntity extends EntityBlock {
    TileEntityRegistryObject<? extends TickableTileEntity> getTileType();

    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType){
        TileEntityRegistryObject<? extends TickableTileEntity> type = getTileType();
        return blockEntityType == type.get() ? (BlockEntityTicker<T>) type.getTicker(level.isClientSide) : null;
    }
}
