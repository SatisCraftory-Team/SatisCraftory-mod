package fr.satiscraftoryteam.satiscraftory.client.models.items;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.LogoItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LogoItemModel extends GeoModel<LogoItem> {
    @Override
    public ResourceLocation getModelResource(LogoItem object) {
        return SatisCraftory.rl("geo/logo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LogoItem object) {
        return SatisCraftory.rl("textures/block/logo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LogoItem animatable) {
        return SatisCraftory.rl("animations/logo.animation.json");
    }
}
