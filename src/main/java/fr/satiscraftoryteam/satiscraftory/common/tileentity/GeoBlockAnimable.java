package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

public class GeoBlockAnimable<BE extends BlockEntity> extends BlockEntity implements GeoBlockEntity {
    private static RawAnimation DEFAULT_ANIMATION = null;

    public GeoBlockAnimable(BlockEntityType<BE> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public GeoBlockAnimable(BlockEntityType<BE> type, BlockPos pos, BlockState blockState, String defaultAnimationName) {
        super(type, pos, blockState);
        DEFAULT_ANIMATION = RawAnimation.begin().thenPlay(defaultAnimationName);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        if (DEFAULT_ANIMATION != null) {
            controllers.add(new AnimationController<>(this, state -> {
                return state.setAndContinue(DEFAULT_ANIMATION);
            }));
        }
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return RenderUtil.getCurrentTick();
    }

}
