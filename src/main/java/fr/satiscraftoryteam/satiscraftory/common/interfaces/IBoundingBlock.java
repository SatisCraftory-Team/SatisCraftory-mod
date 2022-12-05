package fr.satiscraftoryteam.satiscraftory.common.interfaces;

import net.minecraft.core.Vec3i;

public interface IBoundingBlock {

    default boolean triggerBoundingEvent(Vec3i offset, int id, int param) {
        return false;
    }
}
