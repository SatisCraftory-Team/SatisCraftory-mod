package fr.vgtom4.satiscraftory.common.interfaces;

import fr.vgtom4.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.vgtom4.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface IHasMultipleTickableTileEntity extends EntityBlock {
    List<TileEntityRegistryObject<? extends MachineBaseTileEntity>> getTilesTypes();

    default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType){
        List<TileEntityRegistryObject<? extends MachineBaseTileEntity>> type = getTilesTypes();
        for(TileEntityRegistryObject<? extends MachineBaseTileEntity> tile : type){
            if(tile.get() == blockEntityType){
                return (BlockEntityTicker<T>) tile.getTicker(level.isClientSide);
            }
        }
        return null;
    }
}
