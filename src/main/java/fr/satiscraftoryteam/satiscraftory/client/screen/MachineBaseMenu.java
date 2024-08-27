package fr.satiscraftoryteam.satiscraftory.client.screen;

import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBaseMenu<T extends MachineBaseTileEntity> extends AbstractContainerMenu {
    public T machineEntity;

    protected MachineBaseMenu(MenuType<?> menuType, int containerId, Inventory inv, FriendlyByteBuf extraData) {
        super(menuType, containerId);
        machineEntity = (T) inv.player.level().getBlockEntity(extraData.readBlockPos());
        init(inv, machineEntity);
    }

    protected MachineBaseMenu(MenuType<?> menuType, int containerId, Inventory inv, T entity) {
        super(menuType, containerId);
        machineEntity = entity;
        init(inv, entity);
    }

    protected abstract void init(Inventory inv, T entity);
}
