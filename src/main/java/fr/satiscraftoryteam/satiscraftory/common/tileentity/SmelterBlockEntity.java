package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterMenu;
import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData.ProductionBuildingMachine;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmelterBlockEntity extends ProductionBuildingMachine<SmelterBlockEntity> implements MenuProvider, IBoundingBlock {

    public SmelterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.SMELTER_BLOCK_ENTITY.get(), blockPos, blockState, 1, 1, 4, 30, true);

        this.CONVEYOR_OUTPUT_POS_ORIENTATION.add(new Tuple<>(new Vec3i(0,0,1), RelativeOrientationUtils.RelativeOrientation.FRONT));

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -1; z <= 3; z++) {
                    Vec3i pos = new Vec3i(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    boolean shouldNotAdd = false;
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_OUTPUT_POS_ORIENTATION) {
                        if(tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_INPUT_POS_ORIENTATION) {
                        if(tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    if(shouldNotAdd) {
                        continue;
                    }
                    this.BOUNDING_BLOCKS_POS.add(pos);
                }
            }
        }

        this.CONVEYOR_INPUT_POS_ORIENTATION.add(new Tuple<>(new Vec3i(0,0,-1), RelativeOrientationUtils.RelativeOrientation.BACK));
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -1; z <= 3; z++) {
                    Vec3i pos = new Vec3i(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    boolean shouldNotAdd = false;
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_INPUT_POS_ORIENTATION) {
                        if(tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_OUTPUT_POS_ORIENTATION) {
                        if(tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    if(shouldNotAdd) {
                        continue;
                    }
                    this.BOUNDING_BLOCKS_POS.add(pos);
                }
            }
        }

    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private Lazy<IItemHandler> lazyItemHandler = Lazy.of(() -> itemHandler);


    @Override
    public Component getDisplayName() {
        return Component.literal("Smelter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new SmelterMenu(pContainerId, pInventory, this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = Lazy.of(() -> itemHandler);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        tag.put("inventory", itemHandler.serializeNBT(lookupProvider));
        super.saveAdditional(tag, lookupProvider);
    }


    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
        super.loadAdditional(nbt, lookupProvider);
        itemHandler.deserializeNBT(lookupProvider, nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if(hasRecipe() && hasNotReachedStackLimit()) {
            craftItem();
        }
    }

    private void craftItem() {/*
        itemHandler.extractItem(1, 1, false);
        itemHandler.extractItem(2, 1, false);
        itemHandler.extractItem(3, 1, false);*/

        itemHandler.setStackInSlot(0, new ItemStack(ItemInit.PALEBERRY.get(),
                itemHandler.getStackInSlot(0).getCount() + 1));
                
    }

    private boolean hasRecipe() {
        boolean hasItemInFirstSlot = itemHandler.getStackInSlot(1).getItem() == ItemInit.POWER_SHARD.get();
        boolean hasItemInSecondSlot = itemHandler.getStackInSlot(2).getItem() == ItemInit.POWER_SHARD.get();
        boolean hasItemInThirdSlot = itemHandler.getStackInSlot(3).getItem() == ItemInit.POWER_SHARD.get();

        return hasItemInFirstSlot && hasItemInSecondSlot && hasItemInThirdSlot;
    }

    //----------------------------------------------------------------------------------------------------------------//
}
