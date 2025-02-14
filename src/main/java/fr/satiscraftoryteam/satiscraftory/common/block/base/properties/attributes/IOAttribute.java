package fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.StateAttribute;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorStreamPartBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorStreamPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.utils.MultiBlockUtil;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.stream.Stream;

public record IOAttribute(TriConsumer<BlockPos, BlockState, Stream.Builder<IOEntry>> ioEntries) implements StateAttribute {

    @Override
    public void fillBlockStateContainer(Block block, List<Property<?>> properties) {

    }

    public void placeIOEntries(Level level, BlockPos orig, BlockState state) {
        MachineBaseTileEntity machine = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, orig);
        getPositions(orig, state).forEach(ioEntry -> {
            ConveyorStreamPartBlock boundingBlock = ioEntry.type() == BlockIOType.INPUT ? BlockInit.CONVEYOR_INPUT_PART.getBlock() : BlockInit.CONVEYOR_OUTPUT_PART.getBlock();
            BlockState newState = boundingBlock.defaultBlockState();
            Vec3i p2Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(ioEntry.pos(), Attribute.get(state, FacingAttribute.class).getDirection(state));
            BlockPos finalPos = new BlockPos(orig.getX() + p2Pos.getX(), orig.getY() + p2Pos.getY(), orig.getZ() + p2Pos.getZ());
            level.setBlock(finalPos, newState, Block.UPDATE_ALL);

            if (!level.isClientSide()) {
                ConveyorStreamPartBlockEntity tile = WorldUtils.getTileEntity(ConveyorStreamPartBlockEntity.class, level, ioEntry.pos());
                if (tile != null) {
                    tile.setMaster(machine);
                } else {
                    // Mekanism.logger.warn("Unable to find Bounding Block Tile at: {}", boundingLocation);
                }
            }
        });
    }

    public void removeIOEntries(Level level, BlockPos orig, BlockState state) {
        getPositions(orig, state).forEach(ioEntry -> {
            Vec3i p2Pos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(ioEntry.pos(), Attribute.get(state, FacingAttribute.class).getDirection(state));
            BlockPos finalPos = new BlockPos(orig.getX() + p2Pos.getX(), orig.getY() + p2Pos.getY(), orig.getZ() + p2Pos.getZ());
            level.destroyBlock(finalPos, false);
        });
    }

    public Stream<IOEntry> getPositions(BlockPos pos, BlockState state) {
        Stream.Builder<IOEntry> builder = Stream.builder();
        ioEntries().accept(new BlockPos(0, 0, 0), state, builder);
        return builder.build();
    }

    public enum BlockIOType {
        OUTPUT,
        INPUT,
    }

    public static record IOEntry(BlockIOType type, BlockPos pos) {
        public static IOEntry of(BlockIOType type, BlockPos pos) {
            return new IOEntry(type, pos);
        }
    }
}
