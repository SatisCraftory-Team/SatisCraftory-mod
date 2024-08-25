package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Menu;
import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities.InventoryHandler;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.capabilities.InventoryPartition;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.RenderUtil;

public class MinerMk1BlockEntity extends MachineBaseTileEntity<MinerMk1BlockEntity> implements MenuProvider, GeoBlockEntity, IBoundingBlock {

    public final InventoryHandler inventoryHandler;
    public final InventoryPartition overclockPartition = new InventoryPartition("overclock", 3);
    public final InventoryPartition outputPartition = new InventoryPartition("output", 1);

    private float default_energy_use = 5;

    //level miner_mk1 (default = 60)
    private float default_mining_speed = 60;

    //info gisement
    private float purity_modifier = 1;
    double getPowerUsage() {
        return (double) Math.round((default_energy_use * Math.pow( (double) overclockPercentage / 100, 1.6)) * 100.0) / 100.0;
    }

    private int progress = 0;
    private int maxProgress = (int) (60 * 20 / (int)(Math.round((purity_modifier * (double) overclockPercentage / 100 * default_mining_speed) * 100.0) / 100.0));
    // get et set pour la variable maxProgress
    public int getMaxProgress() {
        return maxProgress;
    }
    public void updateMaxProgress() {
        this.maxProgress = (int) (60 * 20 / (int)(Math.round((purity_modifier * (double) overclockPercentage / 100 * default_mining_speed) * 100.0) / 100.0));
    }

    public MinerMk1BlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.MINER_MK1_BLOCK_ENTITY.get(), blockPos, blockState);

        inventoryHandler = new InventoryHandler.Builder()
                .addPartition(overclockPartition)
                .addPartition(outputPartition)
                .build(this);
        this.CONVEYOR_OUTPUT_POS_ORIENTATION.add(new Tuple<>(new Vec3i(0,0,3), RelativeOrientationUtils.RelativeOrientation.FRONT));

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
    }


    @Override
    public Component getDisplayName() {
        return Component.literal("Miner Mk1");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new MinerMk1Menu(pContainerId, pInventory, this);
    }

    private BlockCapabilityCache<IItemHandler, @Nullable Direction> itemHandlerCache;

    public IItemHandler getItemHandler(@Nullable Direction context) {
        return (IItemHandler) inventoryHandler;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if(level != null && !level.isClientSide)
            itemHandlerCache = BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, (ServerLevel) getLevel(), this.worldPosition, null);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        tag.put("inventory", inventoryHandler.serializeNBT(provider));
        outputPartition.serializeNBT(tag);
        overclockPartition.serializeNBT(tag);
        super.saveAdditional(tag, provider);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        inventoryHandler.deserializeNBT(provider, nbt.getCompound("inventory"));
        outputPartition.deserializeNBT(nbt);
        overclockPartition.deserializeNBT(nbt);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(inventoryHandler.inventory.getSlots());
        for (int i = 0; i < inventoryHandler.inventory.getSlots(); i++) {
            inventory.setItem(i, inventoryHandler.inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if (hasNotReachedStackLimit()) {
            if(hasPower() && this.isActive) {
                System.out.println("progress: " + progress + " maxProgress: " + maxProgress + " overclockPercentage: " + overclockPercentage);
                if (progress >= maxProgress) {
                    progress = 0;
                    updateMaxProgress();
                    craftItem();
                }
                progress++;
            }
        } else {
            progress=0;
        }
    }

    private void craftItem() {/*
        itemHandler.extractItem(1, 1, false);
        itemHandler.extractItem(2, 1, false);
        itemHandler.extractItem(3, 1, false);*/

        outputPartition.setStackInSlot(0, new ItemStack(ItemInit.IRON_RESIDUE.get(),
                outputPartition.getStackInSlot(0).getCount() + 1));

    }

    private boolean hasPower() {
//        boolean hasItemInFirstSlot = overclockPartition.getStackInSlot(0).getItem() == ItemInit.POWER_SHARD.get();
//        boolean hasItemInSecondSlot = overclockPartition.getStackInSlot(1).getItem() == ItemInit.POWER_SHARD.get();
//        boolean hasItemInThirdSlot = overclockPartition.getStackInSlot(2).getItem() == ItemInit.POWER_SHARD.get();

//        return hasItemInFirstSlot && hasItemInSecondSlot && hasItemInThirdSlot;
        // TODO: implement here power system
        return true;
    }

    private boolean hasNotReachedStackLimit() {
        return outputPartition.getStackInSlot(0).getCount() < outputPartition.getStackInSlot(0).getMaxStackSize();
    }


    //-------------------------------------------------Animation------------------------------------------------------//

    private static final RawAnimation DEFAULT_ANIMATION = RawAnimation.begin().thenPlay("running");

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            return state.setAndContinue(DEFAULT_ANIMATION);
        }));
    }

    //----------------------------------------------------------------------------------------------------------------//

    // --------------------------------------PartitionManagement---------------------------------------------------------//

    @Override
    public IItemHandler getOutputInventory() {
        return outputPartition;
    }

    @Override
    public IItemHandler getInputInventory() {
        return null;
    }

    @Override
    public int getNumberOfOverclocks() {
        int numberOfOverclocks = 0;
        if (overclockPartition.getStackInSlot(0).getItem() == ItemInit.POWER_SHARD.get()) {
            numberOfOverclocks++;
        }
        if (overclockPartition.getStackInSlot(1).getItem() == ItemInit.POWER_SHARD.get()) {
            numberOfOverclocks++;
        }
        if (overclockPartition.getStackInSlot(2).getItem() == ItemInit.POWER_SHARD.get()) {
            numberOfOverclocks++;
        }
        return numberOfOverclocks;
    }

    //----------------------------------------------------------------------------------------------------------------//
}
