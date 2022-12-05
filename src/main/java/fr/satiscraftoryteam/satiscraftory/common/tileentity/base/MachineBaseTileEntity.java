package fr.satiscraftoryteam.satiscraftory.common.tileentity.base;

import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;

//TODO: implement here capabilities
public abstract class MachineBaseTileEntity extends TickableTileEntity {

    public boolean isActive = false;
    public int overclockPercentage = 100;

    public final ArrayList<Vec3i> BOUNDING_BLOCKS_POS = Lists.newArrayList();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_INPUT_POS_ORIENTATION = Lists.newArrayList();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_OUTPUT_POS_ORIENTATION = Lists.newArrayList();

    public void onAdded() {
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("overclockPercentage", overclockPercentage);
        compoundTag.putBoolean("isActive", isActive);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        isActive = compoundTag.getBoolean("isActive");
        overclockPercentage = compoundTag.getInt("overclockPercentage");
    }

    @Override
    public CompoundTag getReducedUpdateTag() {
        CompoundTag updateTag = super.getReducedUpdateTag();
        updateTag.putInt("overclockPercentage", overclockPercentage);
        updateTag.putBoolean("isActive", isActive);
        return updateTag;
    }


    public MachineBaseTileEntity(TileEntityRegistryObject<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract IItemHandler getOutputInventory();
    public abstract IItemHandler getInputInventory();
}
