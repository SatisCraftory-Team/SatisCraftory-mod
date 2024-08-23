package fr.satiscraftoryteam.satiscraftory.client.models;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class ModelProvider<T extends GeoAnimatable & IAutoModelProvider> extends GeoModel<T> {

    @Override
    public ResourceLocation getModelResource(T object) {
        return new ResourceLocation(SatisCraftory.MODID, String.format("geo/%s%s.geo.json", object.getModelPath(), object.getModelName()));
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return new ResourceLocation(SatisCraftory.MODID, String.format("textures/%s%s.png", object.getModelTexturePath(),object.getModelName()));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return new ResourceLocation(SatisCraftory.MODID, String.format("animations/%s%s.animation.json", animatable.getModelAnimationPath(), animatable.getModelName()));
    }
}
