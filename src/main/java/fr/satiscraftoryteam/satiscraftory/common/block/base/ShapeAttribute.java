package fr.satiscraftoryteam.satiscraftory.common.block.base;

import net.minecraft.world.phys.shapes.VoxelShape;

public record ShapeAttribute(VoxelShape[] bounds) implements Attribute {
}
