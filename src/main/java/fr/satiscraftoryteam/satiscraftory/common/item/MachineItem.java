package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.Attribute;
import fr.satiscraftoryteam.satiscraftory.common.block.base.properties.attributes.RestrictedPlacementAttribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public abstract class MachineItem<BlockItemRenderer extends GeoItemRenderer> extends GeoItemAnimable<BlockItemRenderer> {
    public MachineItem(Block block, Properties properties, BlockItemRenderer blockItemRenderer) {
        super(block, properties, blockItemRenderer);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext blockPlaceContext, BlockState blockState) {
        return Attribute.ifHas(blockState, RestrictedPlacementAttribute.class, (attribute) -> {
            return attribute.canBePlacedOn(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
        }, super.canPlace(blockPlaceContext, blockState));
    }
}
