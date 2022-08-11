package fr.satiscraftoryteam.satiscraftory.client.models.items;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.MinerMk1Item;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MinerMk1ItemModel extends AnimatedGeoModel<MinerMk1Item> {
    @Override
    public ResourceLocation getModelResource(MinerMk1Item object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/miner_mk1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MinerMk1Item object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/machines/miner_mk1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MinerMk1Item animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/miner_mk1.animation.json");
    }
}
