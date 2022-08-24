package fr.satiscraftoryteam.satiscraftory.common.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FacingAttribute implements Attribute {

    private final DirectionProperty facingProperty;
    private final FacePlacementType placementType;
    private final boolean canRotate;

    public FacingAttribute() {
        this(true);
    }

    public FacingAttribute(boolean canRotate) {
        this(BlockStateProperties.HORIZONTAL_FACING, canRotate);
    }

    public FacingAttribute(DirectionProperty facingProperty) {
        this(facingProperty, true);
    }

    public FacingAttribute(DirectionProperty facingProperty, boolean canRotate) {
        this(facingProperty, FacePlacementType.PLAYER_LOCATION, canRotate);
    }

    public FacingAttribute(DirectionProperty facingProperty, FacePlacementType placementType) {
        this(facingProperty, placementType, true);
    }

    public FacingAttribute(DirectionProperty facingProperty, FacePlacementType placementType, boolean canRotate) {
        this.facingProperty = facingProperty;
        this.placementType = placementType;
        this.canRotate = canRotate;
    }

    public boolean canRotate() {
        return canRotate;
    }

    public Direction getDirection(BlockState state) {
        return state.getValue(getFacingProperty());
    }

    @NotNull
    public DirectionProperty getFacingProperty() {
        return facingProperty;
    }

    @NotNull
    public FacePlacementType getPlacementType() {
        return placementType;
    }

    public boolean supportsDirection(Direction direction) {
        return getSupportedDirections().contains(direction);
    }

    public Collection<Direction> getSupportedDirections() {
        return getFacingProperty().getPossibleValues();
    }

    public void fillBlockStateContainer(Block block, List<Property<?>> properties) {
        properties.add(getFacingProperty());
    }

    public BlockState setDirection(@NotNull BlockState state, Direction newDirection) {
        return supportsDirection(newDirection) ? state.setValue(getFacingProperty(), newDirection) : state;
    }

    @Contract("_, null, _, _, _, _ -> null")
    public BlockState getStateForPlacement(Block block, @Nullable BlockState state, @NotNull LevelAccessor world, @NotNull BlockPos pos, @Nullable Player player,
                                           @NotNull Direction face) {
        if (state == null) {
            return null;
        }
        FacingAttribute blockFacing = Attribute.get(block, FacingAttribute.class);
        Direction newDirection = Direction.SOUTH;
        if (blockFacing.getPlacementType() == FacePlacementType.PLAYER_LOCATION) {
            //TODO: Somehow weight this stuff towards context.getFace(), so that it has a higher likelihood of going with the face that was clicked on
            if (blockFacing.supportsDirection(Direction.DOWN) && blockFacing.supportsDirection(Direction.UP)) {
                float rotationPitch = player == null ? 0 : player.getXRot();
                int height = Math.round(rotationPitch);
                if (height >= 65) {
                    newDirection = Direction.UP;
                } else if (height <= -65) {
                    newDirection = Direction.DOWN;
                }
            }
            if (newDirection != Direction.DOWN && newDirection != Direction.UP) {
                //TODO: Can this just use newDirection = context.getPlacementHorizontalFacing().getOpposite(); or is that not accurate
                float placementYaw = player == null ? 0 : player.getYRot();
                int side = Mth.floor((placementYaw * 4.0F / 360.0F) + 0.5D) & 3;
                newDirection = switch (side) {
                    case 0 -> Direction.NORTH;
                    case 1 -> Direction.EAST;
                    case 2 -> Direction.SOUTH;
                    case 3 -> Direction.WEST;
                    default -> newDirection;
                };
            }

        } else {
            newDirection = blockFacing.supportsDirection(face) ? face : Direction.NORTH;
        }

        state = blockFacing.setDirection(state, newDirection);
        return state;
    }

    public static BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation) {
        return rotate(state, rotation);
    }

    public static BlockState rotate(BlockState state, Rotation rotation) {
        Block block = state.getBlock();
        if (Attribute.has(block, FacingAttribute.class)) {
            FacingAttribute blockFacing = Attribute.get(block, FacingAttribute.class);
            if (blockFacing.canRotate()) {
                return rotate(blockFacing, blockFacing.getFacingProperty(), state, rotation);
            }
        }
        return state;
    }

    private static BlockState rotate(FacingAttribute blockFacing, DirectionProperty property, BlockState state, Rotation rotation) {
        return blockFacing.setDirection(state, rotation.rotate(state.getValue(property)));
    }

    public static BlockState mirror(BlockState state, Mirror mirror) {
        Block block = state.getBlock();
        if (Attribute.has(block, FacingAttribute.class)) {
            FacingAttribute blockFacing = Attribute.get(block, FacingAttribute.class);
            if (blockFacing.canRotate()) {
                DirectionProperty property = blockFacing.getFacingProperty();
                return rotate(blockFacing, property, state, mirror.getRotation(state.getValue(property)));
            }
        }
        return state;
    }

    public enum FacePlacementType {
        /** Set the face based on the player's relative location to the placement location. */
        PLAYER_LOCATION,
        /** Set the face based on the direction of the block face selected. */
        SELECTED_FACE
    }
}
