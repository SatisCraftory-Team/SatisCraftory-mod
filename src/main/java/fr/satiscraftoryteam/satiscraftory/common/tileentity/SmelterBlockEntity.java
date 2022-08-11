package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterMenu;
import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;

public class SmelterBlockEntity extends MachineBaseTileEntity implements MenuProvider, IAnimatable, IBoundingBlock {

    public SmelterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.SMELTER_BLOCK_ENTITY, blockPos, blockState);

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
        /*
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
        }*/

    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();


    @Override
    public Component getDisplayName() {
        return Component.literal("Smelter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new SmelterMenu(pContainerId, pInventory, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
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

    private boolean hasNotReachedStackLimit() {
        return itemHandler.getStackInSlot(0).getCount() < itemHandler.getStackInSlot(0).getMaxStackSize();
    }


    //-------------------------------------------------Animation------------------------------------------------------//

    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<SmelterBlockEntity>
                (this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("running", true));

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public ItemStackHandler getOutputInventory() {
        return itemHandler;
    }

    @Override
    public ItemStackHandler getInputInventory() {
        return null;
    }


    //----------------------------------------------------------------------------------------------------------------//
}
