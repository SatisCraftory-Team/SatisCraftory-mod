package fr.vgtom4.satiscraftory.client.renderer.items;

import fr.vgtom4.satiscraftory.client.models.items.MinerMk1ItemModel;
import fr.vgtom4.satiscraftory.client.models.items.SmelterItemModel;
import fr.vgtom4.satiscraftory.common.item.MinerMk1Item;
import fr.vgtom4.satiscraftory.common.item.SmelterItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SmelterItemRenderer extends GeoItemRenderer<SmelterItem> {
    public SmelterItemRenderer() {
        super(new SmelterItemModel());
    }
}
