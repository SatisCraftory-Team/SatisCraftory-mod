package fr.satiscraftoryteam.satiscraftory.client.renderer.items;

import fr.satiscraftoryteam.satiscraftory.client.models.items.CustomItemGeoModel;
import fr.satiscraftoryteam.satiscraftory.common.item.EliocubeItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class EliocubeItemRenderer extends GeoItemRenderer<EliocubeItem> {
    public EliocubeItemRenderer() {
        super(new CustomItemGeoModel<>("eliocube", "block/test", "block/test", "block/test"));
    }
}
