package fr.satiscraftoryteam.satiscraftory.client.renderer.items;

import fr.satiscraftoryteam.satiscraftory.client.models.items.LogoItemModel;
import fr.satiscraftoryteam.satiscraftory.common.item.LogoItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LogoItemRenderer extends GeoItemRenderer<LogoItem> {
    public LogoItemRenderer() {
        super(new LogoItemModel());
    }
}
