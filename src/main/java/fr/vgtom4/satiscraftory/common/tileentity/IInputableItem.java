package fr.vgtom4.satiscraftory.common.tileentity;

import net.minecraft.world.item.Item;

public interface IInputableItem {

    void setConnectedStream(IInputableItem stream);
    boolean canInputItem(Item itemStack);
    void InputItem(Item itemStack);
}
