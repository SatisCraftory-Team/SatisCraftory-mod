package fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes;

import fr.satiscraftoryteam.satiscraftory.common.block.base.BlockBounding;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.StateAttribute;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TileEntityBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.stream.Stream;

public class BoudingAttribute implements StateAttribute {

    public static final BooleanProperty HAS_BOUNDING_BLOCKS = BooleanProperty.create("hasboundingblocks");

    private final TriConsumer<BlockPos, BlockState, Stream.Builder<BlockPos>> boundingPositions;

    public BoudingAttribute(TriConsumer<BlockPos, BlockState, Stream.Builder<BlockPos>> boundingPositions) {
        this.boundingPositions = boundingPositions;
    }

    @Override
    public void fillBlockStateContainer(Block block, List<Property<?>> properties) {
        properties.add(HAS_BOUNDING_BLOCKS);
    }

    public void removeBoundingBlocks(Level level, BlockPos pos, BlockState state) {
        getPositions(pos, state).forEach(p -> {
            BlockState boundingState = level.getBlockState(p);
            if (!boundingState.isAir()) {
                //The state might be air if we broke a bounding block first
                if (boundingState.is(BlockInit.BOUNDING_BLOCK.getBlock())) {
                    level.removeBlock(p, false);
                } else {
//                    Mekanism.logger.warn("Skipping removing block, expected bounding block but the block at {} in {} was {}", p, level.dimension().location(),
//                            RegistryUtils.getName(boundingState.getBlock()));
                }
            }
        });
    }

    public void placeBoundingBlocks(Level level, BlockPos orig, BlockState state) {
        getPositions(orig, state).forEach(boundingLocation -> {
            BlockBounding boundingBlock = (BlockBounding) BlockInit.BOUNDING_BLOCK.getBlock();
            BlockState newState = boundingBlock.defaultBlockState();
            level.setBlock(boundingLocation, newState, Block.UPDATE_ALL);
            if (!level.isClientSide()) {
                TileEntityBoundingBlock tile = WorldUtils.getTileEntity(TileEntityBoundingBlock.class, level, boundingLocation);
                if (tile != null) {
                    tile.setMainLocation(orig);
                } else {
                   // Mekanism.logger.warn("Unable to find Bounding Block Tile at: {}", boundingLocation);
                }
            }
        });
    }

    public Stream<BlockPos> getPositions(BlockPos pos, BlockState state) {
        Stream.Builder<BlockPos> builder = Stream.builder();
        boundingPositions.accept(pos, state, builder);
        return builder.build();
    }
}
