package fr.satiscraftoryteam.satiscraftory.client.models.items;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.SmelterItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SmelterItemModel extends GeoModel<SmelterItem> {
    @Override
    public ResourceLocation getModelResource(SmelterItem object) {
        return SatisCraftory.rl("geo/smelter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmelterItem object) {
        return SatisCraftory.rl("textures/block/machines/smelter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SmelterItem animatable) {
        return SatisCraftory.rl("animations/miner_mk1.animation.json");
    }
}
