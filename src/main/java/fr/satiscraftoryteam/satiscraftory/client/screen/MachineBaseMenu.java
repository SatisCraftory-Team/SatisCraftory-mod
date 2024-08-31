package fr.satiscraftoryteam.satiscraftory.client.screen;

import fr.satiscraftoryteam.satiscraftory.client.screen.element.slot.RestrictedSlot;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities.InventoryPartition;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static fr.satiscraftoryteam.satiscraftory.common.init.ItemInit.POWER_SHARD;

public abstract class MachineBaseMenu<T extends MachineBaseTileEntity> extends AbstractContainerMenu {
    public T machineEntity;
    private Level level;

    protected MachineBaseMenu(MenuType<?> menuType, int containerId, Inventory inv, FriendlyByteBuf extraData) {
        super(menuType, containerId);
        machineEntity = (T) inv.player.level().getBlockEntity(extraData.readBlockPos());
        BE_INVENTORY_SLOT_COUNT =
                (machineEntity.inputPartition != null ? machineEntity.inputPartition.getSlots() : 0 ) +
                (machineEntity.outputPartition != null ? machineEntity.outputPartition.getSlots() : 0 ) +
                (machineEntity.hasOverclockPartition ? machineEntity.overclockPartition.getSlots() : 0);
        init(inv, machineEntity);
    }

    protected MachineBaseMenu(MenuType<?> menuType, int containerId, Inventory inv, T entity) {
        super(menuType, containerId);
        machineEntity = entity;
        init(inv, entity);
    }

    protected void init(Inventory inv, T entity) {
        this.level = inv.player.level();
        checkContainerSize(inv, BE_INVENTORY_SLOT_COUNT);

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        if (entity.hasOverclockPartition) {
            InventoryPartition overclockPartition = entity.overclockPartition;
            this.addSlot(new RestrictedSlot(overclockPartition, 0, 184, 44, POWER_SHARD.get()));
            this.addSlot(new RestrictedSlot(overclockPartition, 1, 202, 44, POWER_SHARD.get()));
            this.addSlot(new RestrictedSlot(overclockPartition, 2, 220, 44, POWER_SHARD.get()));
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, machineEntity.getBlockPos()),
                pPlayer, machineEntity.getBlockState().getBlock());
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // Number of slots in the BlockEntity inventory
    private static int BE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + BE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + BE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    private int ySlot = 32;

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18 + ySlot));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144 + ySlot));
        }
    }

}
