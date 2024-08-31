package fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.miners;

import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.BoudingAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.FacingAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.RestrictedPlacementAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.ShapeAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.resources.DepositBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registration.TileEntityDeferredHolder;
import fr.satiscraftoryteam.satiscraftory.common.shapes.ShapesList;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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

    private static final Vec3i P2OFFSET = new Vec3i(0, 0, 3);

    public MinerMk1Block() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    public MinerMk1Block(Properties properties) {
        super(properties);
    }

    @Override
    protected void initProperties() {
        this.getProps().addProperties(new RestrictedPlacementAttribute(BlockInit.IRON_DEPOSIT.getBlock(), BlockInit.COPPER_DEPOSIT.getBlock()));
        this.getProps().addProperties(new ShapeAttribute(ShapesList.MINER_MK1));
        this.getProps().addProperties(new FacingAttribute(BlockStateProperties.HORIZONTAL_FACING, FacingAttribute.FacePlacementType.PLAYER_LOCATION));
        this.getProps().addProperties(new BoudingAttribute((pos, state, builder) -> {


            //FIXME: PLEASE FIX THIS SHIT (it's working but we need to find a better way to do that)
            Direction direction = Attribute.get(this, FacingAttribute.class).getDirection(state);
            if (direction == Direction.SOUTH) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 5; y++) {
                        for (int z = -1; z <= 3; z++) {
                            if (x != 0 || y != 0 || z != 0) {
                                builder.add(pos.offset(x, y, z));
                            }
                        }
                    }
                }
            } else if (direction == Direction.NORTH) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 5; y++) {
                        for (int z = 1; z >= -3; z--) {
                            if (x != 0 || y != 0 || z != 0) {
                                builder.add(pos.offset(x, y, z));
                            }
                        }
                    }
                }
            } else if (direction == Direction.EAST) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 5; y++) {
                        for (int z = -1; z <= 3; z++) {
                            if (x != 0 || y != 0 || z != 0) {
                                builder.add(pos.offset(z, y, x));
                            }
                        }
                    }
                }
            } else if (direction == Direction.WEST) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 5; y++) {
                        for (int z = 1; z >= -3; z--) {
                            if (x != 0 || y != 0 || z != 0) {
                                builder.add(pos.offset(z, y, x));
                            }
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
