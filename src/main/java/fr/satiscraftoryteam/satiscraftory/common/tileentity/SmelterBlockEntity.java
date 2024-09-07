package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterMenu;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData.ProductionBuildingMachine;
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

public class SmelterBlockEntity extends ProductionBuildingMachine<SmelterBlockEntity> implements MenuProvider, IBoundingBlock {

    public SmelterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.SMELTER_BLOCK_ENTITY.get(), blockPos, blockState, 1, 1, 4, 30, true);

        this.CONVEYOR_OUTPUT_POS_ORIENTATION.add(new Tuple<>(new Vec3i(0, 0, 1), RelativeOrientationUtils.RelativeOrientation.FRONT));

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -1; z <= 3; z++) {
                    Vec3i pos = new Vec3i(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    boolean shouldNotAdd = false;
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_OUTPUT_POS_ORIENTATION) {
                        if (tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_INPUT_POS_ORIENTATION) {
                        if (tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    if (shouldNotAdd) {
                        continue;
                    }
                    this.BOUNDING_BLOCKS_POS.add(pos);
                }
            }
        }

        this.CONVEYOR_INPUT_POS_ORIENTATION.add(new Tuple<>(new Vec3i(0, 0, -1), RelativeOrientationUtils.RelativeOrientation.BACK));
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -1; z <= 3; z++) {
                    Vec3i pos = new Vec3i(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    boolean shouldNotAdd = false;
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_INPUT_POS_ORIENTATION) {
                        if (tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_OUTPUT_POS_ORIENTATION) {
                        if (tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    if (shouldNotAdd) {
                        continue;
                    }
                    this.BOUNDING_BLOCKS_POS.add(pos);
                }
            }
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new SmelterMenu(pContainerId, pInventory, this);
    }
}