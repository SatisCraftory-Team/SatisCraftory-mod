package fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.smelters;

import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.BlockEntityutils;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class SmelterBlock extends MachineBaseBlock implements IHasTileEntity<SmelterBlockEntity> {

    public SmelterBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HAS_BOUNDING_BLOCKS, Boolean.TRUE));
        runCalculation(SHAPE.orElse(Shapes.block()));
    }


    //----------------------------------------Hitbox/collision--------------------------------------------------------//

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE =  Stream.of(
            Block.box(0, 6.5, -15, 16, 22.5, -11),
            Block.box(-4.113961030678929, 52.5, 5.363961030678929, -3.1139610306789294, 61.5, 6.363961030678929),
            Block.box(1, 7.5, -11, 15, 21.5, 27),
            Block.box(15, 4.5, -11, 18, 24.5, 27),
            Block.box(-6, 0, -1.5, -1, 4.5, 17.5),
            Block.box(-1, 0, 24, 17, 2, 29),
            Block.box(-6, 0, -13, -1, 2, 29),
            Block.box(17, 0, -13, 22, 2, 29),
            Block.box(-1, 0, -13, 17, 2, -8),
            Block.box(17, 0, -1.5, 22, 4.5, 17.5),
            Block.box(0, 21.5, -11, 16, 24.5, 27),
            Block.box(-2, 4.5, -11, 1, 24.5, 27),
            Block.box(1, 0.7781749272294078, -11, 15, 6.5, 26.99999967056526),
            Block.box(-4.113961030678928, 37.5, -3.6360389693210706, -3.1139610306789276, 38.5, 7.363961030678929),
            Block.box(-4.113961030678928, 26.5, -6.636038969321071, -3.1139610306789276, 34.5, 10.36396103067893),
            Block.box(-4.900252721164232, 1.9779223481829042, 16, 20.849747133686535, 27.25, 26),
            Block.box(-4.113961030678928, 34.5, -5.636038969321071, -3.1139610306789276, 36.5, 9.36396103067893),
            Block.box(-4.113961030678928, 36.5, -4.636038969321071, -3.1139610306789276, 37.5, 8.36396103067893),
            Block.box(19.11396103067893, 37.5, -3.6360389693210706, 20.11396103067893, 38.5, 7.363961030678929),
            Block.box(19.11396103067893, 26.5, -6.636038969321071, 20.11396103067893, 34.5, 10.36396103067893),
            Block.box(19.11396103067893, 34.5, -5.636038969321071, 20.11396103067893, 36.5, 9.36396103067893),
            Block.box(19.11396103067893, 36.5, -4.636038969321071, 20.11396103067893, 37.5, 8.36396103067893),
            Block.box(19.11396103067893, 51.5, 4.363961030678929, 20.11396103067893, 62.5, 5.363961030678929),
            Block.box(19.11396103067893, 51.5, -1.6360389693210706, 20.11396103067893, 62.5, -0.6360389693210706),
            Block.box(19.11396103067893, 52.5, -2.6360389693210706, 20.11396103067893, 61.5, -1.6360389693210706),
            Block.box(19.11396103067893, 50.5, -0.6360389693210706, 20.11396103067893, 63.5, 4.363961030678929),
            Block.box(19.11396103067893, 52.5, 5.363961030678929, 20.11396103067893, 61.5, 6.363961030678929),
            Block.box(-4.113961030678929, 50.5, -0.6360389693210706, -3.1139610306789294, 63.5, 4.363961030678929),
            Block.box(-4.113961030678929, 52.5, -2.6360389693210706, -3.1139610306789294, 61.5, -1.6360389693210706),
            Block.box(-4.113961030678929, 51.5, -1.6360389693210706, -3.1139610306789294, 62.5, -0.6360389693210706),
            Block.box(-4.113961030678929, 51.5, 4.363961030678929, -3.1139610306789294, 62.5, 5.363961030678929),
            Block.box(11.99999999977414, 67.5, -3.1694164276123047, 13.99999999977414, 68.5, 3.8305835723876953),
            Block.box(1.9999999997741398, 67.5, -3.1694164276123047, 3.99999999977414, 68.5, 3.8305835723876953),
            Block.box(2.99999999977414, 67.5, 3.8305835723876953, 4.99999999977414, 68.5, 5.830583572387695),
            Block.box(5.99999999977414, 67.5, 6.830583572387695, 9.99999999977414, 68.5, 8.830583572387695),
            Block.box(5.99999999977414, 67.5, -8.169416427612305, 9.99999999977414, 68.5, -6.169416427612305),
            Block.box(10.99999999977414, 67.5, 3.8305835723876953, 12.99999999977414, 68.5, 5.830583572387695),
            Block.box(10.99999999977414, 67.5, -5.169416427612305, 12.99999999977414, 68.5, -3.1694164276123047),
            Block.box(9.99999999977414, 67.5, -7.169416427612305, 10.99999999977414, 68.5, -5.169416427612305),
            Block.box(4.99999999977414, 67.5, -7.169416427612305, 5.99999999977414, 68.5, -5.169416427612305),
            Block.box(4.99999999977414, 67.5, 5.830583572387695, 5.99999999977414, 68.5, 7.830583572387695),
            Block.box(9.99999999977414, 67.5, 5.830583572387695, 10.99999999977414, 68.5, 7.830583572387695),
            Block.box(10.99999999977414, 67.5, 5.830583572387695, 11.99999999977414, 68.5, 6.830583572387695),
            Block.box(3.99999999977414, 67.5, 5.830583572387695, 4.99999999977414, 68.5, 6.830583572387695),
            Block.box(3.99999999977414, 67.5, -6.169416427612305, 4.99999999977414, 68.5, -5.169416427612305),
            Block.box(10.99999999977414, 67.5, -6.169416427612305, 11.99999999977414, 68.5, -5.169416427612305),
            Block.box(2.99999999977414, 67.5, -5.169416427612305, 4.99999999977414, 68.5, -3.1694164276123047),
            Block.box(-3.1139610306789276, 26.5, -8.63603896932107, -1.1139610306789276, 65.5, 12.36396103067893),
            Block.box(3.25, 26.5, -15, 6.25, 65.5, -13),
            Block.box(1, 24.5, -9.169416428741606, 15, 67.5, 10.830583571258394),
            Block.box(9.75, 26.5, -15, 12.75, 65.5, -13),
            Block.box(12.75, 26.5, -15, 19.1, 65.5, -8),
            Block.box(-3.0999999999999996, 26.5, -15, 3.25, 65.5, -8),
            Block.box(17.11396103067893, 26.5, -8.63603896932107, 19.11396103067893, 65.5, 12.36396103067893),
            Block.box(-1.1000000000000014, 27.5, -13, 17.055456351736993, 67.5, 10),
            Block.box(6.25, 26.5, -15, 9.75, 35, -13),
            Block.box(-3, 14.5, -3, -2, 15.5, 6),
            Block.box(18, 16.5, -3, 19, 17.5, 6),
            Block.box(18, 18.5, -3, 19, 19.5, 6),
            Block.box(18, 20.5, -3, 19, 21.5, 6),
            Block.box(18, 14.5, -3, 19, 15.5, 6),
            Block.box(-3, 20.5, -3, -2, 21.5, 6),
            Block.box(-3, 18.5, -3, -2, 19.5, 6),
            Block.box(-3, 16.5, -3, -2, 17.5, 6),
            Block.box(0, 6.5, 27, 16, 22.5, 31),
            Block.box(-4.5, 31.5, 20.5, -1.5, 32.5, 23.5),
            Block.box(-4, 22, 21, -2, 32, 23),
            Block.box(-4.5, 27, 20.5, -1.5, 28, 23.5),
            Block.box(-4.75, 28.5, 20.25, -1.25, 29.5, 23.75),
            Block.box(-4.75, 30, 20.25, -1.25, 31, 23.75)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));


    protected void runCalculation(VoxelShape shape) {
        for (Direction direction : Direction.values())
            SHAPES.put(direction, BlockEntityutils.calculateShapes(direction, shape));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter pLevel, BlockPos blockPos, CollisionContext context) {
        return SHAPES.get(blockState.getValue(FACING));
    }


    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter worldIn, BlockPos blockPos, CollisionContext context) {
        return SHAPES.get(blockState.getValue(FACING));
    }


    //----------------------------------------------------------------------------------------------------------------//


    //---------------------------------------------DirectionFace------------------------------------------------------//

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------OPEN_INTERFACE--------------------------------------------------------//
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof SmelterBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)player), (SmelterBlockEntity)entity, blockPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------BLOCK_ENTITY----------------------------------------------------------//

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState pNewState, boolean pIsMoving) {
        if (blockState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof SmelterBlockEntity) {
                ((SmelterBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, pNewState, pIsMoving);
    }


    @Override
    public TileEntityRegistryObject<? extends SmelterBlockEntity> getTileType() {
        return TileEntityInit.SMELTER_BLOCK_ENTITY;
    }

    @Override
    public SmelterBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SmelterBlockEntity(blockPos, blockState);
    }
    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------------particle--------------------------------------------------------//
    public static boolean particle = true;

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        float chance = 0.35f;
        if (chance < randomSource.nextFloat() & particle) {
            level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, blockPos.getX() + randomSource.nextFloat(), blockPos.getY() + 5D, blockPos.getZ() + randomSource.nextFloat(), 0d, 0.05d, 0d);
        }

        super.animateTick(blockState, level, blockPos, randomSource);
    }
    //----------------------------------------------------------------------------------------------------------------//

}
