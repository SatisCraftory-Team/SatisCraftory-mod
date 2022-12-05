package fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.miners;

import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.*;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.shapes.ShapesList;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class NewMinerMk1Block extends MachineBaseBlock implements IHasTickableTileEntity {

    private static final Vec3i P2OFFSET = new Vec3i(0, 0, 3);

    public NewMinerMk1Block() {
        super(BlockBehaviour.Properties.of(Material.METAL));
        //registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MinerMk1BlockEntity(blockPos, blockState);
    }
    //----------------------------------------------------------------------------------------------------------------//

    //---------------------------------------------DirectionFace------------------------------------------------------//



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
