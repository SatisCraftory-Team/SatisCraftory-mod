package fr.vgtom4.satiscraftory.common.block;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

public class MultiBlockUtil {
    public static Vec3i getAbsoluteFromRelativeFacingSouth(Vec3i pos) {
        return pos;
    }

    public static Vec3i getAbsoluteFromRelativeFacingNorth(Vec3i pos) {
        return new Vec3i(-pos.getX(), pos.getY(), -pos.getZ());
    }

    public static Vec3i getAbsoluteFromRelativeFacingEast(Vec3i pos) {
        return new Vec3i(-pos.getZ(), pos.getY(), pos.getX());
    }

    public static Vec3i getAbsoluteFromRelativeFacingWest(Vec3i pos) {
        return new Vec3i(pos.getZ(), pos.getY(), -pos.getX());
    }

    public static Vec3i getAbsolutePosFromRelativeFacingSouth(Vec3i pos, Direction facing) {
        switch (facing) {
            case NORTH:
                return getAbsoluteFromRelativeFacingNorth(pos);
            case EAST:
                return getAbsoluteFromRelativeFacingWest(pos);
            case WEST:
                return getAbsoluteFromRelativeFacingEast(pos);
            case SOUTH:
                return getAbsoluteFromRelativeFacingSouth(pos);
            default:
                return pos;
        }
    }

}
