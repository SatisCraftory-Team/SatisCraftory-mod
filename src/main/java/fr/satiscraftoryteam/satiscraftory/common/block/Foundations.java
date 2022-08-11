package fr.satiscraftoryteam.satiscraftory.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Foundations extends Block {
    public Foundations(Properties properties) {
        super(properties);
    }

    //----------------------------------------Hitbox/collision--------------------------------------------------------//

    private static final VoxelShape SHAPE =  Block.box(-16, 0, -16, 32, 16, 32);

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }
}
