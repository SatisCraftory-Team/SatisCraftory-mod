package fr.vgtom4.satiscraftory.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class BlockDelayedBlockEntity<T extends BlockEntity> extends BaseEntityBlock {

    protected BiFunction<BlockPos,BlockState,T> blockEntityFactory;

    protected BlockDelayedBlockEntity(Properties properties, BiFunction<BlockPos,BlockState,T> blockEntityFactory) {
        super(properties);
        this.blockEntityFactory = blockEntityFactory;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return blockEntityFactory.apply(blockPos,blockState);
    }
}
