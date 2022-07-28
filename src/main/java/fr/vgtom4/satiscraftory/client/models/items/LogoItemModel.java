package fr.vgtom4.satiscraftory.client.models.items;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.item.EliocubeItem;
import fr.vgtom4.satiscraftory.common.item.LogoItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LogoItemModel extends AnimatedGeoModel<LogoItem> {
    @Override
    public ResourceLocation getModelResource(LogoItem object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/logo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LogoItem object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/block/logo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LogoItem animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/logo.animation.json");
    }
}
