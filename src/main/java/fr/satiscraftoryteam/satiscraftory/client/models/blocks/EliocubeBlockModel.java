package fr.satiscraftoryteam.satiscraftory.client.models.blocks;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class EliocubeBlockModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {
    public EliocubeBlockModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    protected String subtype() {
        return "block";
    }
}
