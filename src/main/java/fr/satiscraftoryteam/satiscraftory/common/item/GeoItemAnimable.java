package fr.satiscraftoryteam.satiscraftory.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Consumer;

public class GeoItemAnimable<BlockItemRenderer extends GeoItemRenderer> extends BlockItem implements GeoItem {

    private final BlockItemRenderer blockItemRenderer;
    private static RawAnimation DEFAULT_ANIMATION = null;

    public GeoItemAnimable(Block block, Properties properties, BlockItemRenderer blockItemRenderer) {
        super(block, properties);
        this.blockItemRenderer = blockItemRenderer;
    }

    public GeoItemAnimable(Block block, Properties properties, BlockItemRenderer blockItemRenderer, String defaultAnimationName) {
        super(block, properties);
        this.blockItemRenderer = blockItemRenderer;
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        DEFAULT_ANIMATION = RawAnimation.begin().thenPlay(defaultAnimationName);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return blockItemRenderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        if (DEFAULT_ANIMATION != null) {
            controllers.add(new AnimationController<>(this, state -> {
                return state.setAndContinue(DEFAULT_ANIMATION);
            }));
        }
    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
