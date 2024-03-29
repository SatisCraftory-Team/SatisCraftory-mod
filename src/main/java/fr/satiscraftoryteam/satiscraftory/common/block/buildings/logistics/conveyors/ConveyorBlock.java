package fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors;

import fr.satiscraftoryteam.satiscraftory.common.block.BlockDelayedBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasMultipleTickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ConveyorBlock extends BlockDelayedBlockEntity<ConveyorTileEntity> implements IHasMultipleTickableTileEntity {

    public ConveyorBlock(Properties properties, BiFunction<BlockPos, BlockState, ConveyorTileEntity> blockEntityFactory) {
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
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof ConveyorTileEntity) {
            ((ConveyorTileEntity) be).onPlaced(level, blockPos, blockState);
        }
    }

    @Override
    public List<TileEntityRegistryObject<? extends TickableTileEntity>> getTilesTypes() {
        return new ArrayList<>(){
            {
                add(TileEntityInit.CONVEYOR);
                add(TileEntityInit.CONVEYOR_FULL);
            }
        };
    }
}
