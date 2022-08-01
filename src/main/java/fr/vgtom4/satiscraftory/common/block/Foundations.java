package fr.vgtom4.satiscraftory.common.block;

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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
