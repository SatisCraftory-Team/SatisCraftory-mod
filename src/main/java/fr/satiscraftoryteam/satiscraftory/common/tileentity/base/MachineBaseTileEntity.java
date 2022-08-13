package fr.satiscraftoryteam.satiscraftory.common.tileentity.base;

import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;

//TODO: implement here capabilities
public abstract class MachineBaseTileEntity extends TickableTileEntity {

    public final ArrayList<Vec3i> BOUNDING_BLOCKS_POS = Lists.newArrayList();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_INPUT_POS_ORIENTATION = Lists.newArrayList();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_OUTPUT_POS_ORIENTATION = Lists.newArrayList();

    public void onAdded() {
    }


    public MachineBaseTileEntity(TileEntityRegistryObject<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract IItemHandler getOutputInventory();
    public abstract IItemHandler getInputInventory();
}
