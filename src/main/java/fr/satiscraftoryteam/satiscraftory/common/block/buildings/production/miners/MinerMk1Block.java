package fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.miners;

import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.*;
import fr.satiscraftoryteam.satiscraftory.common.block.resources.DepositBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registration.TileEntityDeferredHolder;
import fr.satiscraftoryteam.satiscraftory.common.shapes.ShapesList;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MinerMk1Block extends MachineBaseBlock implements IHasTickableTileEntity {

    public MinerMk1Block() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    public MinerMk1Block(Properties properties) {
        super(properties);
    }

    @Override
    protected void initProperties() {
        this.getProps().addProperties(new RestrictedPlacementAttribute(BlockInit.IRON_RESOURCE_NODE.getBlock(), BlockInit.COPPER_DEPOSIT.getBlock()));
        this.getProps().addProperties(new ShapeAttribute(ShapesList.MINER_MK1));
        this.getProps().addProperties(new FacingAttribute(BlockStateProperties.HORIZONTAL_FACING, FacingAttribute.FacePlacementType.PLAYER_LOCATION));
        this.getProps().addProperties(new IOAttribute((pos, state, builder) -> {
            builder.add(IOAttribute.IOEntry.of(IOAttribute.BlockIOType.OUTPUT, pos.south(3)));
        }));

        this.getProps().addProperties(new BoudingAttribute((pos, state, builder) -> {


            Direction direction = Attribute.get(this, FacingAttribute.class).getDirection(state);
            int[][] offsets = new int[][]{{-1, 1}, {0, 5}, {-3, 3}};
            boolean reverseZ = direction == Direction.NORTH || direction == Direction.WEST;

            for (int x = offsets[0][0]; x <= offsets[0][1]; x++) {
                for (int y = offsets[1][0]; y <= offsets[1][1]; y++) {
                    for (int z = offsets[2][0]; z <= offsets[2][1]; z++) {
                        if (x != 0 || y != 0 || z != 0) {
                            int offsetX = direction == Direction.EAST || direction == Direction.WEST ? z : x;
                            int offsetZ = reverseZ ? -z : z;
                            builder.add(pos.offset(offsetX, y, offsetZ));
                        }
                    }
                }
            }

        }));
    }

    //------------------------------------------BLOCK_ENTITY----------------------------------------------------------//

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MinerMk1BlockEntity) {
            ((MinerMk1BlockEntity) blockEntity).giveInventoryToPlayer(player);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public TileEntityDeferredHolder<? extends MinerMk1BlockEntity> getTileType() {
        return TileEntityInit.MINER_MK1_BLOCK_ENTITY;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MinerMk1BlockEntity(blockPos, blockState);
    }

    @Override
    public void onPlace(BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        Objects.requireNonNull(this.getTileType().get().getBlockEntity(world, pos)).setBlockResource((DepositBlock) world.getBlockState(pos.below()).getBlock());
    }



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
}
