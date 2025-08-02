package fr.satiscraftoryteam.satiscraftory.client.models.items;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class CustomItemGeoModel<T extends GeoAnimatable> extends DefaultedItemGeoModel<T> {
    private final ResourceLocation modelPath;
    private final ResourceLocation texturePath;
    private final ResourceLocation animationsPath;

    public CustomItemGeoModel(String itemName, String assetSubpath) {
        super(ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, assetSubpath + "/" + itemName));
        this.modelPath = buildFormattedModelPath(itemName, assetSubpath);
        this.texturePath = buildFormattedTexturePath(itemName, "item/" + assetSubpath);
        this.animationsPath = buildFormattedAnimationPath(itemName, assetSubpath);
    }

    public CustomItemGeoModel(String itemName, String modelAssetSubpath, String textureAssetSubpath, String animationAssetSubpath) {
        super(ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, textureAssetSubpath + "/" + itemName));
        this.modelPath = buildFormattedModelPath(itemName, modelAssetSubpath);
        this.texturePath = buildFormattedTexturePath(itemName, textureAssetSubpath);
        this.animationsPath = buildFormattedAnimationPath(itemName, animationAssetSubpath);
    }

    private ResourceLocation buildFormattedModelPath(String itemName, String assetSubpath) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "geo/" + assetSubpath + "/" + itemName + ".geo.json");
    }

    private ResourceLocation buildFormattedTexturePath(String itemName, String assetSubpath) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/" + assetSubpath + "/" + itemName + ".png");
    }

    private ResourceLocation buildFormattedAnimationPath(String itemName, String assetSubpath) {
        return ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "animations/" + assetSubpath + "/" + itemName + ".animation.json");
    }

    @Override
    public ResourceLocation getModelResource(T animatable, GeoRenderer<T> renderer) {
        return this.modelPath;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable, GeoRenderer<T> renderer) {
        return this.texturePath;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return this.animationsPath;
    }
}
