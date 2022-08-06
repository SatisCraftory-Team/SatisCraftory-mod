package fr.vgtom4.satiscraftory.client.models.items;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.item.MinerMk1Item;
import fr.vgtom4.satiscraftory.common.item.SmelterItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SmelterItemModel extends AnimatedGeoModel<SmelterItem> {
    @Override
    public ResourceLocation getModelResource(SmelterItem object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/smelter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmelterItem object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/machines/smelter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SmelterItem animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/miner_mk1.animation.json");
    }
}
