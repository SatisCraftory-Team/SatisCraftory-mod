package fr.vgtom4.satiscraftory.block.custom;

import fr.vgtom4.satiscraftory.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;



public class Eliocube extends BaseEntityBlock {

    public Eliocube(Properties properties) {
        super(properties);
        //registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }


    //----------------------------------------Hitbox/collision--------------------------------------------------------//

    private static final VoxelShape SHAPE =  Block.box(1, 0, 1, 15, 32, 15);
    /*
    private static final Optional<VoxelShape> SHAPE = Stream.of(Block.box(-2.90625, 0, 0.3125, 3.90625, 0.0625, 0.6875),
            (Block.box(0.0625, 0, 0.0625, 0.9375, 1, 0.9375)),
            (Block.box(0.90625, 1.2433373926376117, 0.46875, 0.96875, 1.6316626073623883, 0.53125)),
            (Block.box(0.90625, 1.2433373926376117, 0.46875, 0.96875, 1.6316626073623883, 0.53125)),
            (Block.box(-0.03125, 1.1808373926376117, 0.40625, 0.09375, 1.6316626073623883, 0.59375)),
            (Block.box(0.03125, 1.2433373926376117, 0.46875, 0.09375, 1.6316626073623883, 0.53125)),
            (Block.box(0.24333739263761167, 0.90625, 0.40625, 0.6941626073623883, 1.03125, 0.59375)),
            (Block.box(0.24333739263761167, 0.90625, 0.40625, 0.6941626073623883, 1.03125, 0.59375)),
            (Block.box(0.30583739263761167, 1.84375, 0.46875, 0.6941626073623883, 1.90625, 0.53125)),
            (Block.box(0.30583739263761167, 1.84375, 0.46875, 0.6941626073623883, 1.90625, 0.53125))
    ).reduce((voxelShape, voxelShape2) -> Shapes.join(voxelShape,voxelShape2,BooleanOp.OR));*/

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
        /*.orElse(Shapes.block());*/
    }

    //----------------------------------------------------------------------------------------------------------------//


    //---------------------------------------------DirectionFace------------------------------------------------------//

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
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


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityInit.ELIOCUBE_ENTITY.get().create(pPos, pState);
    }


    //------------------------------------------------particle--------------------------------------------------------//

    private boolean oui = true;

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        float chance = 0.35f;
        if (chance < randomSource.nextFloat() & oui) {
            level.addParticle(ParticleTypes.END_ROD, pos.getX() + randomSource.nextFloat(), pos.getY() + 1.5D, pos.getZ() + randomSource.nextFloat(), 0d, 0.05d, 0d);

        }

        super.animateTick(state, level, pos, randomSource);
    }
    //----------------------------------------------------------------------------------------------------------------//
}
