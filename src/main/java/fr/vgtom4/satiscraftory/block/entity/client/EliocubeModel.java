package fr.vgtom4.satiscraftory.block.entity.client;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.block.entity.custom.EliocubeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EliocubeModel extends AnimatedGeoModel<EliocubeEntity> {
    @Override
    public ResourceLocation getModelResource(EliocubeEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/eliocube.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EliocubeEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/machines/eliocube.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EliocubeEntity animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/eliocube.animation.json");
    }
}
