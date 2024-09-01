package fr.satiscraftoryteam.satiscraftory.common.block.buildings.production.smelters;

import com.mojang.serialization.MapCodec;
import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.FacingAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.ShapeAttribute;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registration.TileEntityDeferredHolder;
import fr.satiscraftoryteam.satiscraftory.common.shapes.ShapesList;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;

public class SmelterBlock extends MachineBaseBlock implements IHasTickableTileEntity {

    public SmelterBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    public SmelterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    protected void initProperties() {
        this.getProps().addProperties(new ShapeAttribute(ShapesList.SMELTER));
        this.getProps().addProperties(new FacingAttribute(BlockStateProperties.HORIZONTAL_FACING, FacingAttribute.FacePlacementType.PLAYER_LOCATION));
//        this.getProps().addProperties(new IOAttribute(IOAttribute.IOType.INPUT_OUTPUT, (pos, state, builder) -> {
//            builder.add(pos.north(1));
//        }));
//        this.getProps().addProperties(new IOAttribute(IOAttribute.IOType.OUTPUT_ONLY, (pos, state, builder) -> {
//            builder.add(pos.north(-1));
//        }));
    }


    //------------------------------------------BLOCK_ENTITY----------------------------------------------------------//

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SmelterBlockEntity) {
            ((SmelterBlockEntity) blockEntity).giveInventoryToPlayer(player);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public TileEntityDeferredHolder<? extends SmelterBlockEntity> getTileType() {
        return TileEntityInit.SMELTER_BLOCK_ENTITY;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
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
