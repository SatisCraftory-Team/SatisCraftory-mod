package fr.satiscraftoryteam.satiscraftory.client.models.blocks;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.LogoBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LogoModel extends GeoModel<LogoBlockEntity> {
    @Override
    public ResourceLocation getModelResource(LogoBlockEntity object) {
        return SatisCraftory.rl("geo/logo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LogoBlockEntity object) {
        return SatisCraftory.rl("textures/block/miscellaneous/logo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LogoBlockEntity animatable) {
        return SatisCraftory.rl("animations/logo.animation.json");
    }
}
