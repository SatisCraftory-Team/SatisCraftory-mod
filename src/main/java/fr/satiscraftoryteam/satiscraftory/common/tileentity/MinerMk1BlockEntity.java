package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Menu;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData.MinerExtractorMachine;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MinerMk1BlockEntity extends MinerExtractorMachine<MinerMk1BlockEntity> implements IBoundingBlock, MenuProvider {
    public static final Vec3i OUTPUT_OFFSET = new Vec3i(0, 0, 3);
    private static final int MIN_BOUNDING_X = -1;
    private static final int MAX_BOUNDING_X = 1;
    private static final int MIN_BOUNDING_Y = 0;
    private static final int MAX_BOUNDING_Y = 6;
    private static final int MIN_BOUNDING_Z = -1;
    private static final int MAX_BOUNDING_Z = 3;

    public MinerMk1BlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.MINER_MK1_BLOCK_ENTITY.get(), blockPos, blockState, 1, 1, 5, 60, true);

        this.CONVEYOR_OUTPUT_POS_ORIENTATION.add(new Tuple<>(OUTPUT_OFFSET, RelativeOrientationUtils.RelativeOrientation.FRONT));
        getBoundingOffsets().forEach(this.BOUNDING_BLOCKS_POS::add);
    }

    public static Stream<Vec3i> getBoundingOffsets() {
        return IntStream.rangeClosed(MIN_BOUNDING_X, MAX_BOUNDING_X).boxed().flatMap(x ->
                        IntStream.rangeClosed(MIN_BOUNDING_Y, MAX_BOUNDING_Y).boxed().flatMap(y ->
                                IntStream.rangeClosed(MIN_BOUNDING_Z, MAX_BOUNDING_Z)
                                        .mapToObj(z -> new Vec3i(x, y, z))))
                .filter(offset -> !offset.equals(Vec3i.ZERO))
                .filter(offset -> !offset.equals(OUTPUT_OFFSET));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new MinerMk1Menu(pContainerId, pInventory, this);
    }


    //-------------------------------------------------Animation------------------------------------------------------//

    private static final RawAnimation DEFAULT_ANIMATION = RawAnimation.begin().thenPlay("running");
    private static final RawAnimation START_ANIMATION = RawAnimation.begin().thenPlay("start").thenLoop("running");
    private static final RawAnimation STOP_ANIMATION = RawAnimation.begin().thenPlay("stop");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            if (this.isActive && this.hasNotReachedStackLimit()) {
                return state.setAndContinue(START_ANIMATION);
            } else {
                return state.setAndContinue(STOP_ANIMATION);
            }
        }));
    }
}
