package fr.satiscraftoryteam.satiscraftory.common.block.base;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.BlockProps;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.BoudingAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.FacingAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.ShapeAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorStreamPartBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IPropsGetter;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorOutputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.BlockstateUtils;
import fr.satiscraftoryteam.satiscraftory.utils.MultiBlockUtil;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class MachineBaseBlock extends Block implements IPropsGetter {

    protected BlockProps blockProps;

    public MachineBaseBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any());
    }

    protected void initProperties() {}

    @Override
    public BlockProps getProps() {
        return blockProps == null ? blockProps = new BlockProps() : blockProps;
    }

    @NotNull
    @Override
    @Deprecated
    public PushReaction getPistonPushReaction(@NotNull BlockState state) {
        if (state.hasBlockEntity()) {
            //Protect against mods like Quark that allow blocks with TEs to be moved
            //TODO: Eventually it would be nice to go through this and maybe even allow some TEs to be moved if they don't strongly
            // care about the world, but for now it is safer to just block them from being moved
            return PushReaction.BLOCK;
        }
        return super.getPistonPushReaction(state);
    }

    @NotNull
    @Override
    @Deprecated
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (this.getProps().has(ShapeAttribute.class)) {
           FacingAttribute attr = this.getProps().get(FacingAttribute.class);
           int index = attr == null ? 0 : (attr.getDirection(state).ordinal() - (attr.getFacingProperty() == BlockStateProperties.FACING ? 0 : 2));
            return this.getProps().get(ShapeAttribute.class).bounds()[index];
        }
        return super.getShape(state, world, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        initProperties();
        BlockstateUtils.fillBlockStateContainer(this, builder);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation) {
        return FacingAttribute.rotate(state, world, pos, rotation);
    }

    @NotNull
    @Override
    @Deprecated
    public BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return FacingAttribute.rotate(state, rotation);
    }

    @NotNull
    @Override
    @Deprecated
    public BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return FacingAttribute.mirror(state, mirror);
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return BlockstateUtils.getStateForPlacement(this, super.getStateForPlacement(context), context);
    }


    @Override
    @Deprecated
    public boolean triggerEvent(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, int id, int param) {
        boolean triggered = super.triggerEvent(state, level, pos, id, param);
        if (this instanceof IHasTileEntity<?> hasTileEntity) {
            return hasTileEntity.triggerBlockEntityEvent(state, level, pos, id, param);
        }
        return triggered;
    }


    @Override
    @Deprecated
    public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {

        if (!state.is(newState.getBlock())) {
            BoudingAttribute hasBounding = Attribute.get(state, BoudingAttribute.class);
            if (hasBounding != null) {
                hasBounding.removeBoundingBlocks(world, pos, state);
            }
        }

        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, world, pos);
            if (tile != null) {
                tile.blockRemoved();
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        Attribute.ifHas(state, BoudingAttribute.class, (attribute) -> {
            attribute.placeBoundingBlocks(world, pos, state);
        });
    }

    //Method to override for setting some simple tile specific stuff
    public void setTileData(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack, MachineBaseTileEntity tile) {
    }

    @Override
    @Deprecated
    public void onPlace(BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (state.hasBlockEntity() && oldState.getBlock() != state.getBlock()) {
            MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, world, pos);
            if (tile != null) {
                tile.onAdded();
            }
        }
        super.onPlace(state, world, pos, oldState, isMoving);
    }

//    @Override
//    @Deprecated
//    public boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
//        return Attribute.has(this, AttributeComparator.class);
//    }

//    @Override
//    @Deprecated
//    public int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos pos) {
//        if (hasAnalogOutputSignal(blockState)) {
//            BlockEntity tile = WorldUtils.getTileEntity(world, pos);
//            //Double-check the tile actually has comparator support
//            if (tile instanceof IComparatorSupport comparatorTile && comparatorTile.supportsComparator()) {
//                return comparatorTile.getCurrentRedstoneLevel();
//            }
//        }
//        return 0;
//    }

//    @Override
//    @Deprecated
//    public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter world, @NotNull BlockPos pos) {
//        return getDestroyProgress(state, player, world, pos, state.hasBlockEntity() ? WorldUtils.getTileEntity(world, pos) : null);
//    }




    public Stream<Vec3i> getBoundingPositions(Level level, BlockPos pos, BlockState state) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, pos);
        if (tile != null) {
            return tile.BOUNDING_BLOCKS_POS.stream();
        }
        return Stream.empty();
    }

    public Stream<BlockPos> getAbsoluteBoundingBlockPos(Level level, BlockPos pos, BlockState state) {
        ArrayList<BlockPos> absolutBoundingBlockPos = new ArrayList<>();
        getBoundingPositions(level, pos, state).forEach(bounding_blocks_pos -> {
            absolutBoundingBlockPos.add(pos.offset(MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(bounding_blocks_pos,state.getValue(BlockStateProperties.HORIZONTAL_FACING))));
        });
        return absolutBoundingBlockPos.stream();
    }

    public Stream<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> getInputConveyorPositionsOrientations(Level level, BlockPos pos, BlockState state) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, pos);
        if (tile != null) {
            return tile.CONVEYOR_INPUT_POS_ORIENTATION.stream();
        }
        return Stream.empty();
    }

    public Stream<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> getOutputConveyorPositionsOrientations(Level level, BlockPos pos, BlockState state) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, pos);
        if (tile != null) {
            return tile.CONVEYOR_OUTPUT_POS_ORIENTATION.stream();
        }
        return Stream.empty();
    }


    private void placeConveyorOutput(Level world, BlockPos pos, BlockState state) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, world, pos);
        getOutputConveyorPositionsOrientations(world, pos, state).forEach(tuple -> {
            BlockPos conveyorOutputPos;
            RelativeOrientationUtils.RelativeOrientation orientation = tuple.getB();
            BlockState conveyorState = BlockInit.CONVEYOR_OUTPUT_PART.getBlock().defaultBlockState();
            Direction blockDirection = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            conveyorState = conveyorState.setValue(ConveyorStreamPartBlock.FACING, RelativeOrientationUtils.getAbsoluteDirection(orientation, blockDirection));
            conveyorOutputPos = pos.offset(MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(tuple.getA(),blockDirection));
            world.setBlock(conveyorOutputPos, conveyorState, Block.UPDATE_ALL);
            ConveyorOutputPartBlockEntity te = (ConveyorOutputPartBlockEntity) WorldUtils.getTileEntity(world,conveyorOutputPos);
            te.setMachine(tile);
        });
    }

    private void placeConveyorInput(Level world, BlockPos pos, BlockState state) {
        getInputConveyorPositionsOrientations(world, pos, state).forEach(tuple -> {
            BlockPos conveyorInputPos;
            RelativeOrientationUtils.RelativeOrientation orientation = tuple.getB();
            BlockState conveyorState = BlockInit.CONVEYOR_INPUT_PART.getBlock().defaultBlockState();
            Direction blockDirection = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            conveyorState = conveyorState.setValue(ConveyorStreamPartBlock.FACING, RelativeOrientationUtils.getAbsoluteDirection(orientation, blockDirection));
            conveyorInputPos = pos.offset(MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(tuple.getA(),blockDirection));
            world.setBlock(conveyorInputPos, conveyorState, Block.UPDATE_ALL);
        });
    }
}
