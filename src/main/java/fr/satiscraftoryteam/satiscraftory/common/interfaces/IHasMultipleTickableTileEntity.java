package fr.satiscraftoryteam.satiscraftory.common.interfaces;

import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IHasMultipleTickableTileEntity extends EntityBlock {
    List<TileEntityDeferredHolder<? extends TickableTileEntity>> getTilesTypes();

    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType){
        List<TileEntityDeferredHolder<? extends TickableTileEntity>> type = getTilesTypes();
        for(TileEntityDeferredHolder<? extends TickableTileEntity> tile : type){
            if(tile.get() == blockEntityType){
                return (BlockEntityTicker<T>) tile.getTicker(level.isClientSide);
            }
        }
        return null;
    }
}
