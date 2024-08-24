package fr.satiscraftoryteam.satiscraftory.common.block.examplesTest;

import com.mojang.serialization.MapCodec;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
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

public class EliocubeBlock extends BaseEntityBlock {

    public EliocubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }


    //----------------------------------------Hitbox/collision--------------------------------------------------------//

    private static final VoxelShape SHAPE =  Block.box(1, 0, 1, 15, 32, 15);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
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
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return TileEntityInit.ELIOCUBE_ENTITY.get().create(pPos, pState);
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
