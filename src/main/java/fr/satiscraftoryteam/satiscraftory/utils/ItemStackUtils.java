package fr.satiscraftoryteam.satiscraftory.utils;

import net.minecraft.world.item.ItemStack;

public class ItemStackUtils {
    public static ItemStack stackItemStack(ItemStack stacked, ItemStack stacker) {
        int maxSize = stacked.getMaxStackSize();
        int remaining = maxSize - stacked.getCount();
        if(remaining > 0){
            int stackerSize = stacker.getCount();
            if(stackerSize > remaining){
                stacker.setCount(stackerSize - remaining);
                stacked.setCount(maxSize);
            } else {
                stacked.setCount(stacked.getCount() + stacker.getCount());
                stacker.setCount(0);
                return ItemStack.EMPTY;
            }
        }

        return stacker;
    }

    public static boolean canStackItemStack(ItemStack stacked, ItemStack stacker) {
        return stacked.getItem() == stacker.getItem() && stacked.getCount() < stacked.getMaxStackSize();
    }

    public static ItemStack copyItemStackWithCount(ItemStack stack, int count) {
        ItemStack copy = stack.copy();
        copy.setCount(count);
        return copy;
    }
}
