package fr.vgtom4.satiscraftory.common.block;

import fr.vgtom4.satiscraftory.common.blockentity.MinerMk1BlockEntity;
import fr.vgtom4.satiscraftory.common.blockentity.BlockEntityutils;
import fr.vgtom4.satiscraftory.common.init.BlockEntityInit;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

public class MinerMK1Block extends BaseEntityBlock {

    public MinerMK1Block(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
        runCalculation(SHAPE.orElse(Shapes.block()));
    }


    //----------------------------------------Hitbox/collision--------------------------------------------------------//

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE =  Stream.of(
            Block.box(-6, 2, -6, -2, 32, -2),
            Block.box(18, 2, -6, 22, 32, -2),
            Block.box(18, 2, 18, 22, 32, 22),
            Block.box(-6, 2, 18, -2, 32, 22),
            Block.box(-6, 0, -6, 22, 2, -1),
            Block.box(17, 2.5, -2, 22, 4.5, 18),
            Block.box(17, 2.5, -16, 22, 4.5, -9),
            Block.box(-6, 2.5, -2, -1, 4.5, 18),
            Block.box(-6, 2.5, -16, -1, 4.5, -9),
            Block.box(-6, 0, 17, 22, 2, 22),
            Block.box(-6, 0, -9, 22, 6, -6),
            Block.box(0, 0, 0, 16, 32, 16),
            Block.box(-2, 4.5, -16, 18, 24.5, -11.75)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR));


    protected void runCalculation(VoxelShape shape) {
        for (Direction direction : Direction.values())
            SHAPES.put(direction, BlockEntityutils.calculateShapes(direction, shape));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPES.get(pState.getValue(FACING));
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE.orElse(Shapes.block());
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
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------OPEN_INTERFACE--------------------------------------------------------//
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof MinerMk1BlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)player), (MinerMk1BlockEntity)entity, blockPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());

    }
    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------BLOCK_ENTITY----------------------------------------------------------//

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof MinerMk1BlockEntity) {
                ((MinerMk1BlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MinerMk1BlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> tBlockEntityType) {
        return createTickerHelper(tBlockEntityType, BlockEntityInit.MINER_MK1_BLOCK_ENTITY.get(),
                MinerMk1BlockEntity::tick);
    }
    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------------particle--------------------------------------------------------//
    public static boolean particle = true;

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        float chance = 0.35f;
        if (chance < randomSource.nextFloat() & particle) {
            level.addParticle(ParticleTypes.FLAME, pos.getX() + randomSource.nextFloat(), pos.getY() + 1D, pos.getZ() + randomSource.nextFloat(), 0d, 0.05d, 0d);
            level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + randomSource.nextFloat(), pos.getY() + 2D, pos.getZ() + randomSource.nextFloat(), 0d, 0.05d, 0d);

        }

        super.animateTick(state, level, pos, randomSource);
    }
    //----------------------------------------------------------------------------------------------------------------//
}
