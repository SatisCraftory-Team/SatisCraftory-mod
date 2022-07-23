package fr.vgtom4.satiscraftory.item.client;

import fr.vgtom4.satiscraftory.item.custom.EliocubeItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class EliocubeItemRenderer extends GeoItemRenderer<EliocubeItem> {
    public EliocubeItemRenderer() {
        super(new EliocubeItemModel());
    }
}
