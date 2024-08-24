package fr.satiscraftoryteam.satiscraftory.common.item;

import net.minecraft.world.level.block.Block;

public class MinerMk1Item extends MachineItem   {
    //public AnimationFactory factory = new AnimationFactory(this);

    public MinerMk1Item(Block block, Properties settings) {
        super(block, settings);
    }
//
//    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
//        return PlayState.CONTINUE;
//    }
//
//    @Override
//    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
//        super.initializeClient(consumer);
//        consumer.accept(new IClientItemExtensions() {
//            private final BlockEntityWithoutLevelRenderer renderer = new MinerMk1ItemRenderer();
//
//            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
//                return renderer;
//            }
//        });
//    }
//
//    @Override
//    public void registerControllers(AnimationData data) {
//        data.addAnimationController(new AnimationController(this, "controller",
//                0, this::predicate));
//    }
//
//    @Override
//    public AnimationFactory getFactory() {
//        return this.factory;
//    }
}
