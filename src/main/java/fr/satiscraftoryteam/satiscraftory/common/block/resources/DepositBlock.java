package fr.satiscraftoryteam.satiscraftory.common.block.resources;

import fr.satiscraftoryteam.satiscraftory.common.resources.DepositPurityManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.registries.DeferredItem;

public class DepositBlock extends Block {
    public static final IntegerProperty PURITY_LEVEL = IntegerProperty.create("purity", 1, 3);

    private final DeferredItem<Item> residue;
    private float purityModifier = 1;

    public DepositBlock(DeferredItem<Item> residue) {
        super(BlockBehaviour.Properties.of());
        this.residue = residue;
        this.registerDefaultState(this.stateDefinition.any().setValue(PURITY_LEVEL, 1));
    }

    public Item getResidueExtracted() {
        return ((Holder<Item>) residue).value();
    }

    public float getPurityModifier() {
        return purityModifier;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PURITY_LEVEL);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.isClientSide || oldState.is(this)) {
            return;
        }

        DepositPurityManager.getRandomPurity(BuiltInRegistries.BLOCK.getKey(this), level.getRandom())
                .ifPresent(purity -> {
                    BlockState randomizedState = state.setValue(PURITY_LEVEL, purity);
                    if (!randomizedState.equals(state)) {
                        level.setBlock(pos, randomizedState, Block.UPDATE_ALL);
                    }
                });
    }
}
