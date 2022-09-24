package fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IItemInputable {
    boolean canInputItem(ItemStack itemStack);
    void inputItem(ItemStack itemStack);
}
