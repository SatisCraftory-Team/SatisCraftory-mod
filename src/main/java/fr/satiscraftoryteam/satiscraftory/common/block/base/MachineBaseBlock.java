package fr.satiscraftoryteam.satiscraftory.common.block.base;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorStreamPartBlock;
import fr.satiscraftoryteam.satiscraftory.utils.MultiBlockUtil;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IHasTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorOutputPartBlockEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TileEntityBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class MachineBaseBlock extends BaseEntityBlock {

    public static final BooleanProperty HAS_BOUNDING_BLOCKS = BooleanProperty.create("hasboundingblocks");
    public static final BooleanProperty HAS_CONVEYOR_INPUT = BooleanProperty.create("hasconveyorinput");
    public static final BooleanProperty HAS_CONVEYOR_OUTPUT = BooleanProperty.create("hasconveyoroutput");

    public MachineBaseBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    @Deprecated
    public PushReaction getPistonPushReaction(@NotNull BlockState state) {
        if (state.hasBlockEntity()) {
            //Protect against mods like Quark that allow blocks with TEs to be moved
            //TODO: Eventually it would be nice to go through this and maybe even allow some TEs to be moved if they don't strongly
            // care about the world, but for now it is safer to just block them from being moved
            return PushReaction.BLOCK;
        }
        return super.getPistonPushReaction(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_BOUNDING_BLOCKS);
        builder.add(HAS_CONVEYOR_OUTPUT);
        builder.add(HAS_CONVEYOR_INPUT);
    }

    @Override
    @Deprecated
    public boolean triggerEvent(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, int id, int param) {
        boolean triggered = super.triggerEvent(state, level, pos, id, param);
        if (this instanceof IHasTileEntity<?> hasTileEntity) {
            return hasTileEntity.triggerBlockEntityEvent(state, level, pos, id, param);
        }
        return triggered;
    }


    @Override
    @Deprecated
    public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, world, pos);

        if (!state.is(newState.getBlock())) {
            removeBoundingBlocks(world, pos, state);
        }

        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            //TileEntityUpdateable tile = WorldUtils.getTileEntity(TileEntityUpdateable.class, world, pos);
            if (tile != null) {
                tile.blockRemoved();
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (state.getValue(this.HAS_BOUNDING_BLOCKS)) {
            placeBoundingBlocks(world, pos, state);
        }
        if(state.getValue(this.HAS_CONVEYOR_OUTPUT)) {
            placeConveyorOutput(world, pos, state);
        }
        if(state.getValue(this.HAS_CONVEYOR_INPUT)) {
            placeConveyorInput(world, pos, state);
        }


//
//
//        TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
//        if (tile == null) {
//            return;
//        }
//        if (tile.supportsRedstone()) {
//            tile.redstone = world.hasNeighborSignal(pos);
//        }
//        // Check if the stack has a custom name, and if the tile supports naming, name it
//        if (tile.isNameable() && stack.hasCustomHoverName()) {
//            tile.setCustomName(stack.getHoverName());
//        }
//
//        //Handle item
//        Item item = stack.getItem();
//        CompoundTag dataMap = ItemDataUtils.getDataMapIfPresent(stack);
//        if (dataMap == null) {
//            //Don't bother modifying the stack even though it doesn't matter as it is going away but return an empty compound
//            // the same as we would normally do if we had to add the data map
//            dataMap = new CompoundTag();
//        }
//        setTileData(world, pos, state, placer, stack, tile);
//
//        //TODO - 1.18: Re-evaluate the entirety of this method and see what parts potentially should not be getting called at all when on the client side.
//        // We previously had issues in readSustainedData regarding frequencies when on the client side so that is why the frequency data has this check
//        // but there is a good chance a lot of this stuff has no real reason to need to be set on the client side at all
//        if (!world.isClientSide && tile.getFrequencyComponent().hasCustomFrequencies()) {
//            tile.getFrequencyComponent().read(dataMap);
//        }
//        if (tile.hasSecurity()) {
//            stack.getCapability(Capabilities.SECURITY_OBJECT).ifPresent(security -> tile.setSecurityMode(security.getSecurityMode()));
//            UUID ownerUUID = MekanismAPI.getSecurityUtils().getOwnerUUID(stack);
//            if (ownerUUID != null) {
//                tile.setOwnerUUID(ownerUUID);
//            } else if (placer != null) {
//                tile.setOwnerUUID(placer.getUUID());
//                if (!world.isClientSide) {
//                    //If the machine doesn't already have an owner, make sure we portray this
//                    Mekanism.packetHandler().sendToAll(new PacketSecurityUpdate(placer.getUUID()));
//                }
//            }
//        }
//        if (tile.supportsUpgrades()) {
//            //The read method validates that data is stored
//            tile.getComponent().read(dataMap);
//        }
//        if (tile instanceof ISideConfiguration config) {
//            //The read methods validate that data is stored
//            config.getConfig().read(dataMap);
//            config.getEjector().read(dataMap);
//        }
//        for (SubstanceType type : EnumUtils.SUBSTANCES) {
//            if (type.canHandle(tile)) {
//                DataHandlerUtils.readContainers(type.getContainers(tile), dataMap.getList(type.getContainerTag(), Tag.TAG_COMPOUND));
//            }
//        }
//        if (tile instanceof ISustainedData sustainedData && stack.hasTag()) {
//            //TODO - 1.18: do we want to be checking it has a tag or not so that we can set things to stuff
//            sustainedData.readSustainedData(dataMap);
//        }
//        if (tile.supportsRedstone()) {
//            NBTUtils.setEnumIfPresent(dataMap, NBTConstants.CONTROL_TYPE, RedstoneControl::byIndexStatic, tile::setControlType);
//        }
//        if (item instanceof ISustainedInventory sustainedInventory && tile.persistInventory()) {
//            tile.setInventory(sustainedInventory.getInventory(stack));
//        }
    }

    //Method to override for setting some simple tile specific stuff
    public void setTileData(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack, MachineBaseTileEntity tile) {
    }



//    @Override
//    public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation) {
//        return AttributeStateFacing.rotate(state, world, pos, rotation);
//    }
//
//    @NotNull
//    @Override
//    @Deprecated
//    public BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
//        return AttributeStateFacing.rotate(state, rotation);
//    }
//
//    @NotNull
//    @Override
//    @Deprecated
//    public BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
//        return AttributeStateFacing.mirror(state, mirror);
//    }

    @Override
    @Deprecated
    public void onPlace(BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (state.hasBlockEntity() && oldState.getBlock() != state.getBlock()) {
            MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, world, pos);
            if (tile != null) {
                tile.onAdded();
            }
        }
        super.onPlace(state, world, pos, oldState, isMoving);
    }

//    @Override
//    @Deprecated
//    public boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
//        return Attribute.has(this, AttributeComparator.class);
//    }

//    @Override
//    @Deprecated
//    public int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos pos) {
//        if (hasAnalogOutputSignal(blockState)) {
//            BlockEntity tile = WorldUtils.getTileEntity(world, pos);
//            //Double-check the tile actually has comparator support
//            if (tile instanceof IComparatorSupport comparatorTile && comparatorTile.supportsComparator()) {
//                return comparatorTile.getCurrentRedstoneLevel();
//            }
//        }
//        return 0;
//    }

//    @Override
//    @Deprecated
//    public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter world, @NotNull BlockPos pos) {
//        return getDestroyProgress(state, player, world, pos, state.hasBlockEntity() ? WorldUtils.getTileEntity(world, pos) : null);
//    }




    public Stream<Vec3i> getBoundingPositions(Level level, BlockPos pos, BlockState state) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, pos);
        if (tile != null) {
            for (Vec3i bounding_blocks_pos : tile.BOUNDING_BLOCKS_POS) {
                //System.out.println(bounding_blocks_pos);
            }
            return tile.BOUNDING_BLOCKS_POS.stream();
        }
        return Stream.empty();
    }

    public Stream<BlockPos> getAbsoluteBoundingBlockPos(Level level, BlockPos pos, BlockState state) {
        ArrayList<BlockPos> absolutBoundingBlockPos = new ArrayList<>();
        getBoundingPositions(level, pos, state).forEach(bounding_blocks_pos -> {
            absolutBoundingBlockPos.add(pos.offset(MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(bounding_blocks_pos,state.getValue(BlockStateProperties.HORIZONTAL_FACING))));
        });
        return absolutBoundingBlockPos.stream();
    }

    public Stream<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> getInputConveyorPositionsOrientations(Level level, BlockPos pos, BlockState state) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, pos);
        if (tile != null) {
            return tile.CONVEYOR_INPUT_POS_ORIENTATION.stream();
        }
        return Stream.empty();
    }

    public Stream<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> getOutputConveyorPositionsOrientations(Level level, BlockPos pos, BlockState state) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, level, pos);
        if (tile != null) {
            return tile.CONVEYOR_OUTPUT_POS_ORIENTATION.stream();
        }
        return Stream.empty();
    }


    public void removeBoundingBlocks(Level level, BlockPos pos, BlockState state) {
        getAbsoluteBoundingBlockPos(level, pos, state).forEach(p -> {
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
        getAbsoluteBoundingBlockPos(level, orig, state).forEach(boundingLocation -> {
            BlockBounding boundingBlock = (BlockBounding) BlockInit.BOUNDING_BLOCK.getBlock();
            BlockState newState = boundingBlock.defaultBlockState();
            level.setBlock(boundingLocation, newState, Block.UPDATE_ALL);
            if (!level.isClientSide()) {
                TileEntityBoundingBlock tile = WorldUtils.getTileEntity(TileEntityBoundingBlock.class, level, boundingLocation);
                if (tile != null) {
                    //System.out.println("Setting main orig a t "  + orig);
                    tile.setMainLocation(orig);
                } else {
                  SatisCraftory.LOGGER.warn("Unable to find Bounding Block Tile at: {}", boundingLocation);
                }
            }
        });
    }

    private void placeConveyorOutput(Level world, BlockPos pos, BlockState state) {
        MachineBaseTileEntity tile = WorldUtils.getTileEntity(MachineBaseTileEntity.class, world, pos);
        getOutputConveyorPositionsOrientations(world, pos, state).forEach(tuple -> {
            BlockPos conveyorOutputPos;
            RelativeOrientationUtils.RelativeOrientation orientation = tuple.getB();
            BlockState conveyorState = BlockInit.CONVEYOR_OUTPUT_PART.getBlock().defaultBlockState();
            Direction blockDirection = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            conveyorState = conveyorState.setValue(ConveyorStreamPartBlock.FACING, RelativeOrientationUtils.getAbsoluteDirection(orientation, blockDirection));
            conveyorOutputPos = pos.offset(MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(tuple.getA(),blockDirection));
            world.setBlock(conveyorOutputPos, conveyorState, Block.UPDATE_ALL);
            ConveyorOutputPartBlockEntity te = (ConveyorOutputPartBlockEntity) WorldUtils.getTileEntity(world,conveyorOutputPos);
            te.setMachine(tile);
        });
    }

    private void placeConveyorInput(Level world, BlockPos pos, BlockState state) {
        getInputConveyorPositionsOrientations(world, pos, state).forEach(tuple -> {
            BlockPos conveyorInputPos;
            RelativeOrientationUtils.RelativeOrientation orientation = tuple.getB();
            BlockState conveyorState = BlockInit.CONVEYOR_INPUT_PART.getBlock().defaultBlockState();
            Direction blockDirection = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            conveyorState = conveyorState.setValue(ConveyorStreamPartBlock.FACING, RelativeOrientationUtils.getAbsoluteDirection(orientation, blockDirection));
            conveyorInputPos = pos.offset(MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(tuple.getA(),blockDirection));
            world.setBlock(conveyorInputPos, conveyorState, Block.UPDATE_ALL);
        });
    }
}