package fr.satiscraftoryteam.satiscraftory.client.screen;

import fr.satiscraftoryteam.satiscraftory.client.screen.element.slot.ResultSlotInit;
import fr.satiscraftoryteam.satiscraftory.common.init.MenuTypesInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MinerMk1Menu extends MachineBaseMenu<MinerMk1BlockEntity> {
    public MinerMk1BlockEntity blockEntity;

    //Client side
    public MinerMk1Menu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        super(MenuTypesInit.MINER_MK1_MENU.get(), pContainerId, inv, extraData);
    }

    //Server side
    public MinerMk1Menu(int pContainerId, Inventory inv, MinerMk1BlockEntity entity) {
        super(MenuTypesInit.MINER_MK1_MENU.get(), pContainerId, inv, entity);
    }

    @Override
    protected void init(Inventory inv, MinerMk1BlockEntity entity) {
        super.init(inv, entity);
        this.addSlot(new ResultSlotInit(entity.outputPartition, 0, 50, 29));
    }
}
