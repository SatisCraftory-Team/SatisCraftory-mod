package fr.vgtom4.satiscraftory.common.block;

import fr.vgtom4.satiscraftory.common.tileentity.ConveyorStreamPartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.BiFunction;

public class ConveyorStreamPartBlock<T extends BlockEntity> extends BlockDelayedBlockEntity<T> {

    public ConveyorStreamPartBlock(Properties properties, BiFunction<BlockPos,BlockState,T> blockEntityFactory) {
        super(properties, blockEntityFactory);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(player.isCrouching()){
            return InteractionResult.PASS;
        }

        if(blockHitResult.getDirection() == blockState.getValue(FACING)){
            //todo: check if player is holding conveyor
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity instanceof ConveyorStreamPartBlockEntity){
            ((ConveyorStreamPartBlockEntity)blockEntity).onUse(blockState, level, blockPos, player, interactionHand, blockHitResult);
        }

        return InteractionResult.SUCCESS;
    }


}
