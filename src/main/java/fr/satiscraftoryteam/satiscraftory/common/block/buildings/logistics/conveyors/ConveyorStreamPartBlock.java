package fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors;

import fr.satiscraftoryteam.satiscraftory.common.block.BlockDelayedBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasMultipleTickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorStreamPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ConveyorStreamPartBlock<T extends BlockEntity> extends BlockDelayedBlockEntity<T> implements IHasMultipleTickableTileEntity {

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


    @Override
    public List<TileEntityRegistryObject<? extends TickableTileEntity>> getTilesTypes() {
        return new ArrayList<>(){
            {
                add(TileEntityInit.CONVEYOR_INPUT_PART_ENTITY);
                add(TileEntityInit.CONVEYOR_OUTPUT_PART_ENTITY);
            }
        };
    }
}
