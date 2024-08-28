package fr.satiscraftoryteam.satiscraftory.common.tileentity.base;

import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities.IBlockCapabilityProvider;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities.InventoryHandler;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities.InventoryPartition;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

//TODO: implement here capabilities
public abstract class MachineBaseTileEntity<BE extends BlockEntity> extends TickableTileEntity<BE> implements IBlockCapabilityProvider {

    public InventoryHandler inventoryHandler;
    public InventoryPartition inputPartition;
    public InventoryPartition outputPartition;
    public InventoryPartition overclockPartition;

    public boolean isActive = true;
    public int overclockPercentage = 100;
    public boolean hasOverclockPartition = false;

    public final ArrayList<Vec3i> BOUNDING_BLOCKS_POS = new ArrayList<>();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_INPUT_POS_ORIENTATION = new ArrayList<>();
    public final ArrayList<Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation>> CONVEYOR_OUTPUT_POS_ORIENTATION = new ArrayList<>();

    public MachineBaseTileEntity(BlockEntityType<BE> type, BlockPos pos, BlockState state, int numberOfInput, int numberOfOutput, boolean hasOverclockPartition) {
        super(type, pos, state);
        this.hasOverclockPartition = hasOverclockPartition;

        InventoryHandler.Builder builder = new InventoryHandler.Builder();
        if (numberOfInput > 0) {
            this.inputPartition = new InventoryPartition("input", numberOfInput);
            builder.addPartition(inputPartition);
        }
        if (numberOfOutput > 0) {
            this.outputPartition = new InventoryPartition("output", numberOfOutput);
            builder.addPartition(outputPartition);
        }
        if (this.hasOverclockPartition) {
            this.overclockPartition = new InventoryPartition("overclock", 3);
            builder.addPartition(overclockPartition);
        }

        this.inventoryHandler = builder.build(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        updateMachineInfos(overclockPercentage);

        BlockCapabilityCache<IItemHandler, @Nullable Direction> itemHandlerCache;
        if(level != null && !level.isClientSide)
            itemHandlerCache = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, (ServerLevel) getLevel(), this.worldPosition, null);
    }

    public abstract void updateMachineInfos(int overclockPercentage);

    @Override
    protected void saveAdditional(CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("overclockPercentage", overclockPercentage);
        tag.putBoolean("isActive", isActive);
        tag.put("inventory", inventoryHandler.serializeNBT(registries));
        if (overclockPartition != null) overclockPartition.serializeNBT(tag);
        if (outputPartition != null) outputPartition.serializeNBT(tag);
        if (inputPartition != null) inputPartition.serializeNBT(tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
        overclockPercentage = tag.getInt("overclockPercentage");
        inventoryHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        if (overclockPartition != null) overclockPartition.deserializeNBT(tag);
        if (outputPartition != null) outputPartition.deserializeNBT(tag);
        if (inputPartition != null) inputPartition.deserializeNBT(tag);
    }

    @Override
    public CompoundTag getReducedUpdateTag(HolderLookup.Provider lookupProvider) {
        CompoundTag updateTag = super.getReducedUpdateTag(lookupProvider);
        updateTag.putInt("overclockPercentage", overclockPercentage);
        updateTag.putBoolean("isActive", isActive);
        return updateTag;
    }

    // --------------------------------------PartitionManagement---------------------------------------------------------//

    public IItemHandler getItemHandler(@Nullable Direction context) {
        return (IItemHandler) inventoryHandler;
    }

    public IItemHandler getOutputInventory() {
        return outputPartition;
    }
    public IItemHandler getInputInventory() {
        return inputPartition;
    }

    public int getNumberOfOverclocks() {
        if (overclockPartition == null) return 0;
        int numberOfOverclocks = 0;
        if (overclockPartition.getStackInSlot(0).getItem() == ItemInit.POWER_SHARD.get()) {
            numberOfOverclocks++;
        }
        if (overclockPartition.getStackInSlot(1).getItem() == ItemInit.POWER_SHARD.get()) {
            numberOfOverclocks++;
        }
        if (overclockPartition.getStackInSlot(2).getItem() == ItemInit.POWER_SHARD.get()) {
            numberOfOverclocks++;
        }
        return numberOfOverclocks;
    }

    public int getMaxOverclockPercentage() {
        return 100 + getNumberOfOverclocks() * 50;
    }

    public <Q, C extends @Nullable Object, T extends BlockCapability<Q, C>> Optional<Q> getCapability(T cap, C context) {
        if(level == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(level.getCapability(cap, getBlockPos(), getBlockState(), this, context));
    }

    public void giveInventoryToPlayer(Player player) {
        SimpleContainer inventory = new SimpleContainer(inventoryHandler.inventory.getSlots());
        for (int i = 0; i < inventoryHandler.inventory.getSlots(); i++) {
            player.getInventory().placeItemBackInInventory(inventoryHandler.inventory.getStackInSlot(i));
        }
    }

    protected boolean hasNotReachedStackLimit() {
        return outputPartition.getStackInSlot(0).getCount() < outputPartition.getStackInSlot(0).getMaxStackSize();
    }

    public void onAdded() {
    }
}
