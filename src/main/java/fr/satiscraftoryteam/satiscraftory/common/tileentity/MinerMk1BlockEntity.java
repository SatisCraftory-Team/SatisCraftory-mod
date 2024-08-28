package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Menu;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData.MinerExtractorMachine;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
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

public class MinerMk1BlockEntity extends MinerExtractorMachine<MinerMk1BlockEntity> implements IBoundingBlock, MenuProvider {

    public MinerMk1BlockEntity(BlockPos blockPos, BlockState blockState) {
        //TODO: get the purity modifier from the block below the miner --> send the blockResource with the purityModifier and resourceType
        super(TileEntityInit.MINER_MK1_BLOCK_ENTITY.get(), blockPos, blockState, 1, 5, 60, 1, true);

        this.CONVEYOR_OUTPUT_POS_ORIENTATION.add(new Tuple<>(new Vec3i(0,0,3), RelativeOrientationUtils.RelativeOrientation.FRONT));

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -1; z <= 3; z++) {
                    Vec3i pos = new Vec3i(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    boolean shouldNotAdd = false;
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_OUTPUT_POS_ORIENTATION) {
                        if(tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_INPUT_POS_ORIENTATION) {
                        if(tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    if(shouldNotAdd) {
                        continue;
                    }
                    this.BOUNDING_BLOCKS_POS.add(pos);
                }
            }
        }
    }


    @Override
    public Component getDisplayName() {
        return Component.literal("Miner Mk1");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new MinerMk1Menu(pContainerId, pInventory, this);
    }


    //-------------------------------------------------Animation------------------------------------------------------//

    private static final RawAnimation DEFAULT_ANIMATION = RawAnimation.begin().thenPlay("running");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            return state.setAndContinue(DEFAULT_ANIMATION);
        }));
    }
}
