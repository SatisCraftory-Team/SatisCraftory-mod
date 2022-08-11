package fr.satiscraftoryteam.satiscraftory.client.renderer.items;

import fr.satiscraftoryteam.satiscraftory.client.models.items.SmelterItemModel;
import fr.satiscraftoryteam.satiscraftory.common.item.SmelterItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SmelterItemRenderer extends GeoItemRenderer<SmelterItem> {
    public SmelterItemRenderer() {
        super(new SmelterItemModel());
    }
}
