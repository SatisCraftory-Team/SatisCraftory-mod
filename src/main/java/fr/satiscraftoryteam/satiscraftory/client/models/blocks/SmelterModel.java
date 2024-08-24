package fr.satiscraftoryteam.satiscraftory.client.models.blocks;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SmelterModel extends GeoModel<SmelterBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SmelterBlockEntity object) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "geo/smelter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmelterBlockEntity object) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/machines/smelter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SmelterBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "animations/miner_mk1.animation.json");
    }
}
