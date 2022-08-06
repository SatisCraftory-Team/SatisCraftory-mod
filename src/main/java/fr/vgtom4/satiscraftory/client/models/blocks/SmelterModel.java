package fr.vgtom4.satiscraftory.client.models.blocks;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import fr.vgtom4.satiscraftory.common.tileentity.SmelterBlockEntity;
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
