package fr.vgtom4.satiscraftory.item.client;

import fr.vgtom4.satiscraftory.item.custom.MinerMk1Item;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MinerMk1ItemRenderer extends GeoItemRenderer<MinerMk1Item> {
    public MinerMk1ItemRenderer() {
        super(new MinerMk1ItemModel());
    }
}
