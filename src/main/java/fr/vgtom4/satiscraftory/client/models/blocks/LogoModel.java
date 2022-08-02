package fr.vgtom4.satiscraftory.client.models.blocks;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.tileentity.LogoBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LogoModel extends AnimatedGeoModel<LogoBlockEntity> {
    @Override
    public ResourceLocation getModelResource(LogoBlockEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/logo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LogoBlockEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/block/logo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LogoBlockEntity animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/logo.animation.json");
    }
}
