package fr.satiscraftoryteam.satiscraftory.client.models.items;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.item.EliocubeItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EliocubeItemModel extends GeoModel<EliocubeItem> {
    @Override
    public ResourceLocation getModelResource(EliocubeItem object) {
        return SatisCraftory.rl("geo/eliocube.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EliocubeItem object) {
        return SatisCraftory.rl("textures/machines/eliocube.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EliocubeItem animatable) {
        return SatisCraftory.rl("animations/eliocube.animation.json");
    }
}
