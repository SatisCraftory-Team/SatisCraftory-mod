package fr.satiscraftoryteam.satiscraftory.common.block.flora;

import com.mojang.serialization.MapCodec;
import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PaleBerryBushBlock extends BushBlock implements BonemealableBlock {

    public static final MapCodec<PaleBerryBushBlock> CODEC = simpleCodec(PaleBerryBushBlock::new);
    public static final int MAX_AGE = 1;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    private static final VoxelShape SAPLING_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    public PaleBerryBushBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 1));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return null;
    }

    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(ItemInit.PALEBERRY.get());
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(AGE) == 0) {
            return SAPLING_SHAPE;
        } else {
            return super.getShape(blockState, blockGetter, blockPos, collisionContext);
        }
    }

    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(AGE) < 1;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = state.getValue(AGE);
        if (i < 1 && level.getRawBrightness(pos.above(), 0) >= 9 && net.neoforged.neoforge.common.CommonHooks.canCropGrow(level, pos, state, random.nextInt(5) == 0)) {
            BlockState blockstate = state.setValue(AGE, Integer.valueOf(i + 1));
            level.setBlock(pos, blockstate, 2);
            net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(level, pos, state);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult
    ) {
        int i = state.getValue(AGE);
        boolean flag = i == 1;
        return !flag && stack.is(Items.BONE_MEAL)
                ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
                : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == 1;
        if (i > 0) {
            int j = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(ItemInit.PALEBERRY.asItem(), j + (flag ? 1 : 0)));
            level.playSound(
                    null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F
            );
            BlockState blockstate = state.setValue(AGE, Integer.valueOf(0));
            level.setBlock(pos, blockstate, 0);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_57282_) {
        p_57282_.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < 1;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = Math.min(1, state.getValue(AGE) + 1);
        level.setBlock(pos, state.setValue(AGE, Integer.valueOf(i)), 2);
    }
}
