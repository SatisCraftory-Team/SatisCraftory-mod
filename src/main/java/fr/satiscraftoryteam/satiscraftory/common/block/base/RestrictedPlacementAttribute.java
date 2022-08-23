package fr.satiscraftoryteam.satiscraftory.common.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;

public record  RestrictedPlacementAttribute(Block... blocks) implements Attribute {

    public boolean canBePlacedOn(Level level, BlockPos pos) {
        Block blockBellow = level.getBlockState(pos.below()).getBlock();
        return asList().contains(blockBellow);
    }

    private List<Block> asList() {
        return Arrays.asList(blocks);
    }
}
