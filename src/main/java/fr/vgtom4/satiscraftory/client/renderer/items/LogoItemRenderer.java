package fr.vgtom4.satiscraftory.client.renderer.items;

import fr.vgtom4.satiscraftory.client.models.items.EliocubeItemModel;
import fr.vgtom4.satiscraftory.client.models.items.LogoItemModel;
import fr.vgtom4.satiscraftory.common.item.EliocubeItem;
import fr.vgtom4.satiscraftory.common.item.LogoItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class LogoItemRenderer extends GeoItemRenderer<LogoItem> {
    public LogoItemRenderer() {
        super(new LogoItemModel());
    }
}
