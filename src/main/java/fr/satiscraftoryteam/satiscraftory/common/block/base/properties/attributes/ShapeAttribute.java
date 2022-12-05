package fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import net.minecraft.world.phys.shapes.VoxelShape;

public record ShapeAttribute(VoxelShape[] bounds) implements Attribute { }
