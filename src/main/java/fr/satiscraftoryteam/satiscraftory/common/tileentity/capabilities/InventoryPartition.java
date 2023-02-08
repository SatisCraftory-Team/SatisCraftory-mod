package fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class InventoryPartition implements IItemHandler, IItemHandlerModifiable {
    public final String name;
    public ItemStackHandler inventory;
    public final int partitionSize;
    private int partitionIndex;

    public InventoryPartition(String name, int partitionSize) {
        this.name = name;
        this.partitionSize = partitionSize;
        this.partitionIndex = 0;
    }

    public InventoryPartition(String name, int partitionSize, ItemStackHandler inventory) {
        this.name = name;
        this.partitionSize = partitionSize;
        this.inventory = inventory;
        this.partitionIndex = 0;
    }

    public void setPartitionIndex(int partitionIndex) {
        this.partitionIndex = partitionIndex;
    }

    @Override
    public int getSlots() {
        return partitionSize;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(partitionIndex + slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return inventory.insertItem(partitionIndex + slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return inventory.extractItem(partitionIndex + slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(partitionIndex + slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return inventory.isItemValid(partitionIndex + slot, stack);
    }

    public void serializeNBT(CompoundTag nbt) {
        nbt.putInt(name + "_index", partitionIndex);
    }

    public void deserializeNBT(CompoundTag nbt) {
        partitionIndex = nbt.getInt(name + "_index");
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        inventory.setStackInSlot(partitionIndex + slot, stack);
    }
}
