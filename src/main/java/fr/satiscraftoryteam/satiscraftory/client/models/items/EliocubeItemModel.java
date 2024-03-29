package fr.satiscraftoryteam.satiscraftory.client.models.items;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.EliocubeItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EliocubeItemModel extends AnimatedGeoModel<EliocubeItem> {
    @Override
    public ResourceLocation getModelResource(EliocubeItem object) {
        return new ResourceLocation(SatisCraftory.MODID, "geo/eliocube.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EliocubeItem object) {
        return new ResourceLocation(SatisCraftory.MODID, "textures/machines/eliocube.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EliocubeItem animatable) {
        return new ResourceLocation(SatisCraftory.MODID, "animations/eliocube.animation.json");
    }
}
