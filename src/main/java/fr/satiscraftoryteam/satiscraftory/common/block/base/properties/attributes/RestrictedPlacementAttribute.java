package fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;

public record RestrictedPlacementAttribute(Block... blocks) implements Attribute {

    public boolean canBePlacedOn(Level level, BlockPos pos) {
        Block blockBellow = level.getBlockState(pos.below()).getBlock();
        return Arrays.asList(blocks).contains(blockBellow);
    }
}
