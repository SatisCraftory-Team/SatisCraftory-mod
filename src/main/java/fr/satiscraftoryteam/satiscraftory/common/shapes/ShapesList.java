package fr.satiscraftoryteam.satiscraftory.common.shapes;

import fr.satiscraftoryteam.satiscraftory.utils.ShapesUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapesList {
    public static final VoxelShape[] MINER_MK1 = new VoxelShape[ShapesUtils.HORIZONTAL_DIRECTIONS.length];

    static {

        setShape(ShapesUtils.combine(
                Block.box(-7, 39, -31, 23, 42, -7),
                Block.box(0, 41, 0, 17, 51, 17),
                Block.box(1, 0, 1, 16, 82, 16),
                Block.box(0, 82, 0, 17, 86, 16),
                Block.box(-2, 83, 5, 2, 108, 9),
                Block.box(-7, 82, 16, 23, 86, 23),
                Block.box(-6, 42, 19, -2, 84, 21),
                Block.box(18, 42, 19, 22, 84, 21),
                Block.box(-6, 0, -2, -1, 4.5, 18),
                Block.box(17, 0, -2, 22, 4.5, 18),
                Block.box(-2, 4.5, -43, 18, 24.5, -15),
                Block.box(-6, 0, -9, 22, 6, -6),
                Block.box(-6, 0, -45, 22, 2, -28),
                Block.box(0, 6.5, -47, 16, 22.5, -43),
                Block.box(-6, 0, -28, -1, 4.5, -9),
                Block.box(17, 0, -28, 22, 4.5, -9),
                Block.box(-7, 35, -7, 23, 42, 23),
                Block.box(18, 2, 18, 22, 42, 22),
                Block.box(-6, 2, 18, -2, 42, 22),
                Block.box(-6, 2, -6, -2, 42, -2),
                Block.box(18, 2, -6, 22, 42, -2),
                Block.box(-6, 0, 17, 22, 2, 22),
                Block.box(-6, 0, -6, 22, 2, -1)
        ), MINER_MK1);

    }

    public static void setShape(VoxelShape shape, VoxelShape[] dest, boolean verticalAxis, boolean invert) {
        Direction[] dirs = verticalAxis ? ShapesUtils.DIRECTIONS : ShapesUtils.HORIZONTAL_DIRECTIONS;
        for (Direction side : dirs) {
            dest[verticalAxis ? side.ordinal() : side.ordinal() - 2] = verticalAxis ? ShapesUtils.rotate(shape, invert ? side.getOpposite() : side) : ShapesUtils.rotateHorizontal(shape, side);
        }
    }

    public static void setShape(VoxelShape shape, VoxelShape[] dest) {
        setShape(shape, dest, false, false);
    }
}
