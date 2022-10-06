package fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.StateAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorStreamPartBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorInputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorOutputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
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

public record IOAttribute(IOType type, TriConsumer<BlockPos, BlockState, Stream.Builder<BlockPos>> boundingPositions) implements StateAttribute {

    public static final BooleanProperty HAS_CONVEYOR_INPUT = BooleanProperty.create("hasconveyorinput");
    public static final BooleanProperty HAS_CONVEYOR_OUTPUT = BooleanProperty.create("hasconveyoroutput");

    @Override
    public void fillBlockStateContainer(Block block, List<Property<?>> properties) {
        switch (type) {
            case INPUT_ONLY -> properties.add(HAS_CONVEYOR_INPUT);
            case OUTPUT_ONLY -> properties.add(HAS_CONVEYOR_OUTPUT);
            case INPUT_OUTPUT -> {
                properties.add(HAS_CONVEYOR_INPUT);
                properties.add(HAS_CONVEYOR_OUTPUT);
            }
        }
    }

    public void placeInput(Level level, BlockPos orig, BlockState state) {
        MachineBaseTileEntity machine = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, orig);
        getPositions(orig, state).forEach(boundingLocation -> {
            ConveyorStreamPartBlock boundingBlock = (ConveyorStreamPartBlock) BlockInit.CONVEYOR_INPUT_PART.getBlock();
            BlockState newState = boundingBlock.defaultBlockState();
            level.setBlock(boundingLocation, newState, Block.UPDATE_ALL);
            if (!level.isClientSide()) {
                ConveyorInputPartBlockEntity tile = WorldUtils.getTileEntity(ConveyorInputPartBlockEntity.class, level, boundingLocation);
                if (tile != null) {
                    tile.setMaster(machine);
                } else {
                    // Mekanism.logger.warn("Unable to find Bounding Block Tile at: {}", boundingLocation);
                }
            }
        });
    }

    public void placeOutput(Level level, BlockPos orig, BlockState state) {
        MachineBaseTileEntity machine = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, orig);
        getPositions(orig, state).forEach(boundingLocation -> {
            ConveyorStreamPartBlock boundingBlock = (ConveyorStreamPartBlock) BlockInit.CONVEYOR_OUTPUT_PART.getBlock();
            BlockState newState = boundingBlock.defaultBlockState();
            level.setBlock(boundingLocation, newState, Block.UPDATE_ALL);
            if (!level.isClientSide()) {
                ConveyorOutputPartBlockEntity tile = WorldUtils.getTileEntity(ConveyorOutputPartBlockEntity.class, level, boundingLocation);
                if (tile != null) {
                    tile.setMachine(machine);
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

    public enum IOType {
        OUTPUT_ONLY,
        INPUT_ONLY,
        INPUT_OUTPUT
    }
}
