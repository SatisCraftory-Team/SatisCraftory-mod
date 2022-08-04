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

import java.util.List;

public interface IHasMultipleTickableTileEntity extends EntityBlock {
    List<TileEntityRegistryObject<? extends TickableTileEntity>> getTilesTypes();

    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType){
        List<TileEntityRegistryObject<? extends TickableTileEntity>> type = getTilesTypes();
        for(TileEntityRegistryObject<? extends TickableTileEntity> tile : type){
            if(tile.get() == blockEntityType){
                return (BlockEntityTicker<T>) tile.getTicker(level.isClientSide);
            }
        }
        return null;
    }
}
