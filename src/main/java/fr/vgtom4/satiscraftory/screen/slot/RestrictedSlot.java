package fr.vgtom4.satiscraftory.screen.slot;

import fr.vgtom4.satiscraftory.init.ItemInit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RestrictedSlot extends SlotItemHandler {
    private final List<Item> allowedStacks = Lists.newArrayList();
    public RestrictedSlot(IItemHandler itemHandler, int index, int x, int y, List<Item> allowedStacks) {
        super(itemHandler, index, x, y);
    }

    public RestrictedSlot(IItemHandler itemHandler, int index, int x, int y, Item allowedStacks) {
        super(itemHandler, index, x, y);
        this.allowedStacks.add(allowedStacks);

    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return allowedStacks.contains(stack.getItem());
    }


}
