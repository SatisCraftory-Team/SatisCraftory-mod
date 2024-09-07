package fr.satiscraftoryteam.satiscraftory.client.screen;

import fr.satiscraftoryteam.satiscraftory.client.screen.element.slot.RestrictedSlot;
import fr.satiscraftoryteam.satiscraftory.client.screen.element.slot.ResultSlotInit;
import fr.satiscraftoryteam.satiscraftory.common.init.MenuTypesInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import java.util.Arrays;

import static fr.satiscraftoryteam.satiscraftory.common.init.ItemInit.COPPER_RESIDUE;
import static fr.satiscraftoryteam.satiscraftory.common.init.ItemInit.IRON_RESIDUE;

public class SmelterMenu extends MachineBaseMenu<SmelterBlockEntity> {
    public SmelterBlockEntity blockEntity;

    public SmelterMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        super(MenuTypesInit.SMELTER_MENU.get() ,pContainerId, inv, extraData);
    }

    public SmelterMenu(int pContainerId, Inventory inv, SmelterBlockEntity entity) {
        super(MenuTypesInit.SMELTER_MENU.get(), pContainerId, inv, entity);
    }

    @Override
    protected void init(Inventory inv, SmelterBlockEntity entity) {
        super.init(inv, entity);
        this.addSlot(new RestrictedSlot(entity.inputPartition, 0, 1, 45, Arrays.asList(IRON_RESIDUE.get(), COPPER_RESIDUE.get())));
        this.addSlot(new ResultSlotInit(entity.outputPartition, 0, 17, 45));
    }

}
