package fr.vgtom4.satiscraftory.common.tileentity;

import net.minecraft.world.item.Item;

public interface IItemInputable {
    boolean canInputItem(Item itemStack);
    void inputItem(Item itemStack);
}
