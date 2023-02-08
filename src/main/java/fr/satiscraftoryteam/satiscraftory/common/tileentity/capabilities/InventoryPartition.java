package fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class InventoryPartition extends ItemStackHandler {
    public final String name;
    public final int partitionSize;
    private int partitionIndex;

    public InventoryPartition(String name, int partitionSize) {
        this.name = name;
        this.partitionSize = partitionSize;
        this.partitionIndex = 0;
    }

//    public InventoryPartition(String name, int partitionSize, ItemStackHandler inventory) {
//        this.name = name;
//        this.partitionSize = partitionSize;
//        this.inventory = inventory;
//        this.partitionIndex = 0;
//    }

    public void setPartitionIndex(int partitionIndex) {
        this.partitionIndex = partitionIndex;
    }

    @Override
    public int getSlots() {
        return partitionSize;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return super.getStackInSlot(partitionIndex + slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return super.insertItem(partitionIndex + slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(partitionIndex + slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return super.getSlotLimit(partitionIndex + slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return super.isItemValid(partitionIndex + slot, stack);
    }

    public void serializeNBT(CompoundTag nbt) {
        nbt.putInt(name + "_index", partitionIndex);
    }

    public void deserializeNBT(CompoundTag nbt) {
        partitionIndex = nbt.getInt(name + "_index");
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        super.setStackInSlot(partitionIndex + slot, stack);
    }
}
