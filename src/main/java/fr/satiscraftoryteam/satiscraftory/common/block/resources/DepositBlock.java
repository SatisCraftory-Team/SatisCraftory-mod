package fr.satiscraftoryteam.satiscraftory.common.block.resources;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class DepositBlock extends Block {
    private final Item residue;
    private float purityModifier = 1;
    public DepositBlock(Item residue) {
        super(BlockBehaviour.Properties.of());
        this.residue = residue;
    }

    public Item getResidueExtracted() {
        return residue;
    }

    public float getPurityModifier() {
        return purityModifier;
    }

//    Random random = new Random();
//    purityModifier = random.nextFloat() < 0.5 ? 0.5f : random.nextFloat() < 0.5 ? 1 : 1.5f;

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        purityModifier = level.random.nextFloat() < 0.5 ? 0.5f : level.random.nextFloat() < 0.5 ? 1 : 1.5f;
    }


}
