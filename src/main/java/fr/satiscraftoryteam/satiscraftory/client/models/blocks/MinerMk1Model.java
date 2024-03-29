package fr.satiscraftoryteam.satiscraftory.client.models.blocks;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MinerMk1Model extends AnimatedGeoModel<MinerMk1BlockEntity> {
    @Override
    public ResourceLocation getModelResource(MinerMk1BlockEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/miner_mk1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MinerMk1BlockEntity object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/machines/miner_mk1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MinerMk1BlockEntity animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/miner_mk1.animation.json");
    }
}
