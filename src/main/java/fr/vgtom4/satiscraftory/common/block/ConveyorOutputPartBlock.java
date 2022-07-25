package fr.vgtom4.satiscraftory.common.block;

import fr.vgtom4.satiscraftory.common.blockentity.ConveyorOutputPartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ConveyorOutputPartBlock extends ConveyorStreamPartBlock {

    public ConveyorOutputPartBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ConveyorOutputPartBlockEntity(blockPos, blockState);
    }
}
