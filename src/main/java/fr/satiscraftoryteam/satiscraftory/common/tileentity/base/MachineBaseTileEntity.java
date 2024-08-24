package fr.satiscraftoryteam.satiscraftory.common.tileentity.base;

import fr.satiscraftoryteam.satiscraftory.common.registration.TileEntityDeferredHolder;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities.IBlockCapabilityProvider;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.items.IItemHandler;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

//TODO: implement here capabilities
public abstract class MachineBaseTileEntity extends TickableTileEntity implements IBlockCapabilityProvider {

    public boolean isActive = false;
    public int overclockPercentage = 100;

    public final ArrayList<Vec3i> BOUNDING_BLOCKS_POS = Lists.newArrayList();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_INPUT_POS_ORIENTATION = Lists.newArrayList();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_OUTPUT_POS_ORIENTATION = Lists.newArrayList();

    public void onAdded() {
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider lookupProvider) {
        super.saveAdditional(compoundTag, lookupProvider);
        compoundTag.putInt("overclockPercentage", overclockPercentage);
        compoundTag.putBoolean("isActive", isActive);
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider lookupProvider) {
        super.loadAdditional(compoundTag, lookupProvider);
        isActive = compoundTag.getBoolean("isActive");
        overclockPercentage = compoundTag.getInt("overclockPercentage");
    }

    @Override
    public CompoundTag getReducedUpdateTag(HolderLookup.Provider lookupProvider) {
        CompoundTag updateTag = super.getReducedUpdateTag(lookupProvider);
        updateTag.putInt("overclockPercentage", overclockPercentage);
        updateTag.putBoolean("isActive", isActive);
        return updateTag;
    }


    public MachineBaseTileEntity(TileEntityDeferredHolder<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract IItemHandler getOutputInventory();
    public abstract IItemHandler getInputInventory();

    public abstract int getNumberOfOverclocks();

    public <Q, C extends @Nullable Object, T extends BlockCapability<Q, C>> Optional<Q> getCapability(T cap, C context) {
        if(level == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(level.getCapability(cap, getBlockPos(), getBlockState(), this, context));
    }
}
