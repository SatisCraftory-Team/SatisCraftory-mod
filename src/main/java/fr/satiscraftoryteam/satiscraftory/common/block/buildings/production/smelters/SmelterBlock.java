package fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.smelters;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.FacingAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.IOAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.ShapeAttribute;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.shapes.ShapesList;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class SmelterBlock extends MachineBaseBlock implements IHasTileEntity<SmelterBlockEntity> {

    public SmelterBlock(Properties properties) {
        super(properties);
        //registerDefaultState(this.defaultBlockState().setValue(HAS_BOUNDING_BLOCKS, Boolean.TRUE));

        //runCalculation(SHAPE.orElse(Shapes.block()));

    }

    @Override
    protected void initProperties() {
        this.getProps().addProperties(new ShapeAttribute(ShapesList.SMELTER));
        this.getProps().addProperties(new FacingAttribute(BlockStateProperties.HORIZONTAL_FACING, FacingAttribute.FacePlacementType.PLAYER_LOCATION));
        this.getProps().addProperties(new IOAttribute(IOAttribute.IOType.INPUT_OUTPUT, (pos, state, builder) -> {

        }));
    }


    //------------------------------------------OPEN_INTERFACE--------------------------------------------------------//
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if(entity instanceof SmelterBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)player), (SmelterBlockEntity)entity, blockPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------BLOCK_ENTITY----------------------------------------------------------//
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState pNewState, boolean pIsMoving) {
        if (blockState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof SmelterBlockEntity) {
                ((SmelterBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, pNewState, pIsMoving);
    }


    @Override
    public TileEntityRegistryObject<? extends SmelterBlockEntity> getTileType() {
        return TileEntityInit.SMELTER_BLOCK_ENTITY;
    }

    @Override
    public SmelterBlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SmelterBlockEntity(blockPos, blockState);
    }
    //----------------------------------------------------------------------------------------------------------------//


    //------------------------------------------------particle--------------------------------------------------------//
    public static boolean particle = true;

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        float chance = 0.35f;
        if (chance < randomSource.nextFloat() & particle) {
            level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, blockPos.getX() + randomSource.nextFloat(), blockPos.getY() + 5D, blockPos.getZ() + randomSource.nextFloat(), 0d, 0.05d, 0d);
        }

        super.animateTick(blockState, level, blockPos, randomSource);
    }
    //----------------------------------------------------------------------------------------------------------------//

}
