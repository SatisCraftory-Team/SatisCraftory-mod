package fr.vgtom4.satiscraftory.client.renderer.items;

import fr.vgtom4.satiscraftory.client.models.items.MinerMk1ItemModel;
import fr.vgtom4.satiscraftory.common.item.MinerMk1Item;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MinerMk1ItemRenderer extends GeoItemRenderer<MinerMk1Item> {
    public MinerMk1ItemRenderer() {
        super(new MinerMk1ItemModel());
    }
}
