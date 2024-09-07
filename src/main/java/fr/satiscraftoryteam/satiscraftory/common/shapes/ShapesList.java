package fr.satiscraftoryteam.satiscraftory.common.shapes;

import fr.satiscraftoryteam.satiscraftory.utils.ShapesUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapesList {
    public static final VoxelShape[] MINER_MK1 = new VoxelShape[ShapesUtils.HORIZONTAL_DIRECTIONS.length];
    public static final VoxelShape[] SMELTER = new VoxelShape[ShapesUtils.HORIZONTAL_DIRECTIONS.length];


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

        setShape(ShapesUtils.combine(
                Block.box(0, 6.5, -15, 16, 22.5, -11),
                Block.box(1, 7.5, -11, 15, 21.5, 27),
                Block.box(-2, 4.5, -11, 18, 24.5, 27),
                Block.box(-6, 0, -1.5, -1, 4.5, 17.5),
                Block.box(-6, 0, -13, 22, 2, 29),
                Block.box(17, 0, -1.5, 22, 4.5, 17.5),
                Block.box(0, 21.5, -11, 16, 24.5, 27),
                Block.box(1, 0.7781749272294078, -11, 15, 6.5, 26.99999967056526),
                Block.box(-5, 0, 16, 20.999999854850767, 27.5, 26),
                Block.box(-4, 26.5, -6, 20, 34.5, 9),
                Block.box(-4, 34.5, -5, 20, 36.5, 8),
                Block.box(-4, 36.5, -4, 20, 37.5, 7),
                Block.box(-4, 37.5, -3, 20, 38.5, 6),
                Block.box(-4, 51.5, -2, 20, 62.5, 5),
                Block.box(-4, 52.5, -3, 20, 61.5, 6),
                Block.box(-4, 50.5, -1, 20, 63.5, 4),
                Block.box(1.9999999997741398, 67.5, -3.1694164276123047, 13.99999999977414, 68.5, 3.8305835723876953),
                Block.box(2.99999999977414, 67.5, -5.169416427612305, 12.99999999977414, 68.5, 5.830583572387695),
                Block.box(5.99999999977414, 67.5, -8.169416427612305, 9.99999999977414, 68.5, 8.830583572387695),
                Block.box(4.99999999977414, 67.5, -7.169416427612305, 10.99999999977414, 68.5, 7.830583572387695),
                Block.box(3.99999999977414, 67.5, -6.169416427612305, 11.99999999977414, 68.5, 6.830583572387695),
                Block.box(-3, 26.5, -15, -1, 65.5, 12),
                Block.box(1, 24.5, -9, 15, 67.5, 11),
                Block.box(-1, 26.5, -15, 17, 65.5, -13),
                Block.box(17, 26.5, -15, 19, 65.5, 12),
                Block.box(-1, 26.5, -13, 17, 67.5, 10),
                Block.box(-3, 14.5, -3, -2, 21.5, 6),
                Block.box(18, 14.5, -3, 19, 21.5, 6),
                Block.box(0, 6.5, 27, 16, 22.5, 31),
                Block.box(-4.5, 27, 20.5, -1.5, 32.5, 23.5),
                Block.box(-4.75, 28.5, 20.25, -1.25, 31, 23.75)
        ), SMELTER);
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
