package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.client.renderer.items.EliocubeItemRenderer;
import net.minecraft.world.level.block.Block;

public class EliocubeItem extends GeoItemAnimable<EliocubeItemRenderer> {

    public EliocubeItem(Block block, Properties properties) {
        super(block, properties, new EliocubeItemRenderer());
    }
}
