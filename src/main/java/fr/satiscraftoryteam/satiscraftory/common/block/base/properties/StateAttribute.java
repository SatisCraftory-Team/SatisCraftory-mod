package fr.satiscraftoryteam.satiscraftory.common.block.base.properties;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

public interface StateAttribute extends Attribute {

    void fillBlockStateContainer(Block block, List<Property<?>> properties);
}
