package fr.satiscraftoryteam.satiscraftory.client.models.blocks;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SmelterModel extends AnimatedGeoModel<SmelterBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SmelterBlockEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/smelter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmelterBlockEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/machines/smelter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SmelterBlockEntity animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/miner_mk1.animation.json");
    }
}
