package fr.satiscraftoryteam.satiscraftory.client.models.blocks;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.EliocubeBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EliocubeModel extends GeoModel<EliocubeBlockEntity> {
    @Override
    public ResourceLocation getModelResource(EliocubeBlockEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/eliocube.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EliocubeBlockEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/machines/eliocube.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EliocubeBlockEntity animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/eliocube.animation.json");
    }
}
