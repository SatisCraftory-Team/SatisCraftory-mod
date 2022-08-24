package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.RestrictedPlacementAttribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineItem extends BlockItem {

    public MachineItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext blockPlaceContext, BlockState blockState) {
        return Attribute.ifHas(blockState, RestrictedPlacementAttribute.class, (attribute) -> {
            return attribute.canBePlacedOn(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
        }, super.canPlace(blockPlaceContext, blockState));
    }
}
