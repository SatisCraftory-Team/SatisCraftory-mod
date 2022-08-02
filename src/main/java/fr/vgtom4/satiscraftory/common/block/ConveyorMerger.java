package fr.vgtom4.satiscraftory.common.block;

import fr.vgtom4.satiscraftory.common.init.BlockInit;
import fr.vgtom4.satiscraftory.common.tileentity.BlockEntityutils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class ConveyorMerger extends HorizontalDirectionalBlock {

    private static final Vec3i P2OFFSET = new Vec3i(0, 0, 1);
    private static final Vec3i P3OFFSET = new Vec3i(0, 0, -1);
    private static final Vec3i P4OFFSET = new Vec3i(1, 0, 0);
    private static final Vec3i P5OFFSET = new Vec3i(-1, 0, 0);

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE = Optional.of(Block.box(0, 0, 0, 16, 24, 16));

    public ConveyorMerger(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        runCalculation(SHAPE.orElse(Shapes.block()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void runCalculation(VoxelShape shape) {
        for (Direction direction : Direction.values())
            SHAPES.put(direction, BlockEntityutils.calculateShapes(direction, shape));
    }


    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        Vec3i p2Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P2OFFSET, blockState.getValue(FACING));
        Vec3i p3Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P3OFFSET, blockState.getValue(FACING));
        Vec3i p4Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P4OFFSET, blockState.getValue(FACING));
        Vec3i p5Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P5OFFSET, blockState.getValue(FACING));

        level.setBlockAndUpdate(new BlockPos(blockPos.getX() + p2Pos.getX(), blockPos.getY() + p2Pos.getY(), blockPos.getZ() + p2Pos.getZ()), BlockInit.CONVEYOR_OUTPUT_PART.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING)));
        level.setBlockAndUpdate(new BlockPos(blockPos.getX() + p3Pos.getX(), blockPos.getY() + p3Pos.getY(), blockPos.getZ() + p3Pos.getZ()), BlockInit.CONVEYOR_INPUT_PART.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING).getOpposite()));
        level.setBlockAndUpdate(new BlockPos(blockPos.getX() + p4Pos.getX(), blockPos.getY() + p4Pos.getY(), blockPos.getZ() + p4Pos.getZ()), BlockInit.CONVEYOR_INPUT_PART.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING).getCounterClockWise()));
        level.setBlockAndUpdate(new BlockPos(blockPos.getX() + p5Pos.getX(), blockPos.getY() + p5Pos.getY(), blockPos.getZ() + p5Pos.getZ()), BlockInit.CONVEYOR_INPUT_PART.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING).getClockWise()));
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState blockState, Level level, BlockPos blockPos, Player player, boolean willHarvest, FluidState fluid) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        Vec3i p2Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P2OFFSET, blockState.getValue(FACING));
        Vec3i p3Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P3OFFSET, blockState.getValue(FACING));
        Vec3i p4Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P4OFFSET, blockState.getValue(FACING));
        Vec3i p5Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P5OFFSET, blockState.getValue(FACING));

        if (this == BlockInit.CONVEYOR_MERGER.get()) {
            level.destroyBlock(new BlockPos(blockPos.getX() + p2Pos.getX(), blockPos.getY() + p2Pos.getY(), blockPos.getZ() + p2Pos.getZ()), false);
            level.destroyBlock(new BlockPos(blockPos.getX() + p3Pos.getX(), blockPos.getY() + p3Pos.getY(), blockPos.getZ() + p3Pos.getZ()), false);
            level.destroyBlock(new BlockPos(blockPos.getX() + p4Pos.getX(), blockPos.getY() + p4Pos.getY(), blockPos.getZ() + p4Pos.getZ()), false);
            level.destroyBlock(new BlockPos(blockPos.getX() + p5Pos.getX(), blockPos.getY() + p5Pos.getY(), blockPos.getZ() + p5Pos.getZ()), false);
        }
        //destroy others blocks : don't work
        /*
        if (this == BlockInit.MINER_MK1_P2.get()) {
            level.destroyBlock(new BlockPos(x, y , z), false);
            level.destroyBlock(new BlockPos(blockPos.getX() + p3Pos.getX(), blockPos.getY() + p3Pos.getY(), blockPos.getZ() + p3Pos.getZ()), false);
        }
        if (this == BlockInit.MINER_MK1_P3.get()) {
            level.destroyBlock(new BlockPos(x, y , z), false);
            level.destroyBlock(new BlockPos(blockPos.getX() + p2Pos.getX(), blockPos.getY() + p2Pos.getY(), blockPos.getZ() + p2Pos.getZ()), false);
        }*/

        return super.onDestroyedByPlayer(blockState, level, blockPos, player, willHarvest, fluid);
    }
}
