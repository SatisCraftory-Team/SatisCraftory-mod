package fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.List;

public class InventoryHandler implements INBTSerializable<CompoundTag> {
    public final BlockEntity tile;
    public final ItemStackHandler inventory;

    public InventoryHandler(BlockEntity tile, int size) {
        this.tile = tile;
        this.inventory = new ItemStackHandler(size){
            @Override
            public void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                InventoryHandler.this.onContentsChanged(slot);
            }
        };
    }

    private void onContentsChanged(int slot) {
        tile.setChanged();
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return inventory.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        inventory.deserializeNBT(provider, nbt);
    }


    public static class Builder {
        private final List<InventoryPartition> partitions;

        public Builder() {
            this.partitions = new ArrayList<>();
        }


        public Builder addPartition(@NotNull InventoryPartition partition) {
            partitions.add(partition);
            return this;
        }

        public InventoryHandler build(BlockEntity tile) {
            int totalSize = 0;
            for (InventoryPartition partition : partitions) {
                totalSize += partition.partitionSize;
            }
            InventoryHandler handler = new InventoryHandler(tile,totalSize);
            int index = 0;
            for (InventoryPartition partition : partitions) {
                partition.inventory = handler.inventory;
                partition.setPartitionIndex(index);
                index += partition.partitionSize;
            }
            return handler;
        }
    }
}
