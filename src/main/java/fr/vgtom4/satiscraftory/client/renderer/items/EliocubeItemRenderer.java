package fr.vgtom4.satiscraftory.client.renderer.items;

import fr.vgtom4.satiscraftory.client.models.items.EliocubeItemModel;
import fr.vgtom4.satiscraftory.common.item.EliocubeItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class EliocubeItemRenderer extends GeoItemRenderer<EliocubeItem> {
    public EliocubeItemRenderer() {
        super(new EliocubeItemModel());
    }
}
