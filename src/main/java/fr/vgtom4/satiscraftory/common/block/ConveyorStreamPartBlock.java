package fr.vgtom4.satiscraftory.common.block;

import fr.vgtom4.satiscraftory.common.blockentity.ConveyorOutputPartBlockEntity;
import fr.vgtom4.satiscraftory.common.blockentity.ConveyorStreamPartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class ConveyorStreamPartBlock extends BaseEntityBlock {

    public ConveyorStreamPartBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
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
