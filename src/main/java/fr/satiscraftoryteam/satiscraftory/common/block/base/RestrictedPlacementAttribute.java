package fr.satiscraftoryteam.satiscraftory.common.block.base;

import net.minecraft.world.level.block.Block;

public record  RestrictedPlacementAttribute(Block... blocks) implements Attribute{
}
