package fr.vgtom4.satiscraftory.common.block;

import fr.vgtom4.satiscraftory.common.blockentity.ConveyorInputPartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ConveyorInputPartBlock extends ConveyorStreamPartBlock {

    public ConveyorInputPartBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ConveyorInputPartBlockEntity(blockPos, blockState);
    }
}
