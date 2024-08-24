package fr.satiscraftoryteam.satiscraftory.client.models.blocks;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MinerMk1Model extends GeoModel<MinerMk1BlockEntity> {
    @Override
    public ResourceLocation getModelResource(MinerMk1BlockEntity object) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "geo/miner_mk1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MinerMk1BlockEntity object) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/block/machines/miner_mk1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MinerMk1BlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "animations/miner_mk1.animation.json");
    }
}
