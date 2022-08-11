package fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.miners;

import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.BlockEntityutils;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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

public class MinerMK1Block extends MachineBaseBlock implements IHasTickableTileEntity {

    private static final Vec3i P2OFFSET = new Vec3i(0, 0, 3);

    public MinerMK1Block(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HAS_BOUNDING_BLOCKS, Boolean.TRUE));
        runCalculation(SHAPE.orElse(Shapes.block()));
    }


    //----------------------------------------Hitbox/collision--------------------------------------------------------//

    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

    private static final Optional<VoxelShape> SHAPE =  Stream.of(
            Block.box(-7, 39, -31, 23, 42, -7),
            Block.box(0, 41, 0, 17, 51, 17),
            Block.box(1, 0, 1, 16, 82, 16),
            Block.box(0, 82, 0, 17, 86, 16),
            Block.box(-2, 83, 5, 2, 108, 9),
            Block.box(-7, 82, 16, 23, 86, 23),
            Block.box(-6, 42, 19, -2, 84, 21),
            Block.box(18, 42, 19, 22, 84, 21),
            Block.box(-6, 0, -2, -1, 4.5, 18),
            Block.box(17, 0, -2, 22, 4.5, 18),
            Block.box(-2, 4.5, -43, 18, 24.5, -15),
            Block.box(-6, 0, -9, 22, 6, -6),
            Block.box(-6, 0, -45, 22, 2, -28),
            Block.box(0, 6.5, -47, 16, 22.5, -43),
            Block.box(-6, 0, -28, -1, 4.5, -9),
            Block.box(17, 0, -28, 22, 4.5, -9),
            Block.box(-7, 35, -7, 23, 42, 23),
            Block.box(18, 2, 18, 22, 42, 22),
            Block.box(-6, 2, 18, -2, 42, 22),
            Block.box(-6, 2, -6, -2, 42, -2),
            Block.box(18, 2, -6, 22, 42, -2),
            Block.box(-6, 0, 17, 22, 2, 22),
            Block.box(-6, 0, -6, 22, 2, -1)
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
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState pNewState, boolean pIsMoving) {
        if (blockState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof MinerMk1BlockEntity) {
                ((MinerMk1BlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, pNewState, pIsMoving);
    }


    @Override
    public TileEntityRegistryObject<? extends MinerMk1BlockEntity> getTileType() {
        return TileEntityInit.MINER_MK1_BLOCK_ENTITY;
    }

    @Override
    public MinerMk1BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MinerMk1BlockEntity(blockPos, blockState);
    }
    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------------particle--------------------------------------------------------//
    public static boolean particle = true;

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        float chance = 0.35f;
        if (chance < randomSource.nextFloat() & particle) {
            level.addParticle(ParticleTypes.FLAME, blockPos.getX() + randomSource.nextFloat(), blockPos.getY() + 1D, blockPos.getZ() + randomSource.nextFloat(), 0d, 0.05d, 0d);
            level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, blockPos.getX() + randomSource.nextFloat(), blockPos.getY() + 5D, blockPos.getZ() + randomSource.nextFloat(), 0d, 0.05d, 0d);
        }

        super.animateTick(blockState, level, blockPos, randomSource);
    }
    //----------------------------------------------------------------------------------------------------------------//

    /*
    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        Vec3i p2Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P2OFFSET, blockState.getValue(FACING));

        level.setBlockAndUpdate(new BlockPos(blockPos.getX() + p2Pos.getX(), blockPos.getY() + p2Pos.getY(), blockPos.getZ() + p2Pos.getZ()), BlockInit.CONVEYOR_OUTPUT_PART.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING)));
    }


    @Override
    public boolean onDestroyedByPlayer(BlockState blockState, Level level, BlockPos blockPos, Player player, boolean willHarvest, FluidState fluid) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        Vec3i p2Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(P2OFFSET, blockState.getValue(FACING));

        if (this == BlockInit.MINER_MK1.getBlock()) {
            level.destroyBlock(new BlockPos(blockPos.getX() + p2Pos.getX(), blockPos.getY() + p2Pos.getY(), blockPos.getZ() + p2Pos.getZ()), false);
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
        }

        return super.onDestroyedByPlayer(blockState, level, blockPos, player, willHarvest, fluid);
    }*/
}
