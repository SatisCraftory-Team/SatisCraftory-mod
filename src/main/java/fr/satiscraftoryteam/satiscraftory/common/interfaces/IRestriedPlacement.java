package fr.satiscraftoryteam.satiscraftory.common.interfaces;

import net.minecraft.world.level.block.Block;
import org.apache.commons.compress.utils.Lists;

import java.util.Arrays;
import java.util.List;

public interface IRestriedPlacement {

    static List<Block> canOnlyBePlacedOn = Lists.newArrayList();

    default void registerPlacementBlock(Block... block) {
        canOnlyBePlacedOn.addAll(Arrays.asList(block));
    }

    default boolean canBeplaced(Block blockBellow) {
        return canOnlyBePlacedOn.contains(blockBellow);
    }
}
