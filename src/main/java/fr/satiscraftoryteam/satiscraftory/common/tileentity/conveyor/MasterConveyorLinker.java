package fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateMasterConveyorLinker;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

public class MasterConveyorLinker implements IItemStreamable{

    public final int itemPerMin; //thing todo
    public ArrayList<ConveyorTileEntity> conveyorChain;
    public ArrayList<ItemStack> itemsChain;
    public IItemInputable outputTile;
    public float progress;
    public int itemStuckIndex; //item where all items after are not moving todo: implement this

    public int tickCounter;

    private int[] loadPoses; //buffer to connect conveyor when world is loaded
    private int[] loadOutputPose;
    private boolean activated;

    public MasterConveyorLinker(int itemPerMin, ConveyorTileEntity linkerCreator) {
        activated = true;
        this.itemPerMin = itemPerMin;
        conveyorChain = new ArrayList<>();
        itemsChain = new ArrayList<>();
        linkerCreator.isMaster = true;
        conveyorChain.add(linkerCreator);
    }

    public MasterConveyorLinker merge(MasterConveyorLinker toMerge){
        toMerge.conveyorChain.get(0).isMaster = false;
        itemsChain.addAll(toMerge.itemsChain);
        conveyorChain.addAll(toMerge.conveyorChain);
        toMerge.updateAllMaster(this);
        outputTile = toMerge.outputTile;
        return this;
    }

    public void masterClientTick(){
        tickCounter++;
        if(tickCounter >= 20f * (itemPerMin/60)){
            tickCounter = 0;
            for(int i = itemsChain.size() - 2; i >= 0; i--){
                if(itemsChain.get(i+1) == null){
                    itemsChain.set(i+1,itemsChain.get(i));
                    itemsChain.set(i,null);
                }
            }
        }
        progress = tickCounter / 20f * (itemPerMin/60);
    }

    public void masterServerTick(){
        tickCounter++;
        if(tickCounter >= 20f * (itemPerMin/60)){
            tickCounter = 0;
            if(itemsChain.get(itemsChain.size() - 1) != null){
                tryOutputItem(itemsChain.get(itemsChain.size() - 1));
            }
            for(int i = itemsChain.size() - 2; i >= 0; i--){
                if(itemsChain.get(i+1) == null){
                    itemsChain.set(i+1,itemsChain.get(i));
                    itemsChain.set(i,null);
                }
            }
        }
    }

    public boolean tryOutputItem(ItemStack item) {
        if(outputTile != null && outputTile.canInputItem(item)){
            outputTile.inputItem(item);
            itemsChain.set(itemsChain.size() - 1, null);
            sendUpdateMasterConveyorLinkerPacket();
            return true;
        }
        return false;
    }

    private void sendUpdateMasterConveyorLinkerPacket() {
        ConveyorTileEntity masterConveyor = conveyorChain.get(0);
        PacketUpdateMasterConveyorLinker updatePacket = new PacketUpdateMasterConveyorLinker(tickCounter, itemsChain, conveyorChain, outputTile, masterConveyor);
        masterConveyor.sendUpdateMasterConveyorLinkerPacket(updatePacket);
    }

    public void handleUpdateMasterConveyorLinkerPacket(PacketUpdateMasterConveyorLinker updatePacket){
        tickCounter = updatePacket.getTickCounter();
        itemsChain = updatePacket.getItemsChain();
        conveyorChain = updatePacket.getConveyorChain();
        outputTile = updatePacket.getOutputTile();
    }

    @Override
    public boolean canInputItem(ItemStack itemStack) {
        return itemsChain.get(0) == null;
    }

    @Override
    public void inputItem(ItemStack itemStack) {
        SatisCraftory.LOGGER.info("inputItem");
        itemsChain.set(0,itemStack);
        sendUpdateMasterConveyorLinkerPacket();
    }

    @Override
    public void setOutput(IItemInputable stream) {
        outputTile = stream;
    }

    public void trySetOutput(IItemInputable stream, ConveyorTileEntity connectionHandlerSender){
        if(connectionHandlerSender != conveyorChain.get(conveyorChain.size() - 1)){
            SatisCraftory.LOGGER.error("trying to link output without being last conveyor in chain");
            return;
        }

        setOutput(stream);
    }

    public ItemStack[] getItemsForConveyor(ConveyorTileEntity conveyorTile){
        int index = conveyorChain.indexOf(conveyorTile);
        if(index == -1){
            SatisCraftory.LOGGER.error("trying to get items for conveyor not in conveyor chain");
            return null;
        }
        return getItemsForConveyor(index);
    }

    public void updateAllMaster(MasterConveyorLinker masterConveyorLinker){
        for(ConveyorTileEntity conveyorTileEntity : masterConveyorLinker.conveyorChain){
            conveyorTileEntity.master = masterConveyorLinker;
        }
    }

    public ItemStack[] getItemsForConveyor(int conveyorIndex){
        return new ItemStack[]{itemsChain.get(conveyorIndex * 2), itemsChain.get(conveyorIndex * 2 + 1)};
    }

    public void save(CompoundTag compoundTag) {
        compoundTag.putIntArray("itemsChain", itemsChain.stream().mapToInt((itemStack -> Item.getId(Objects.requireNonNullElse(itemStack, ItemStack.EMPTY).getItem()))).toArray());
        int[] poses = new int[conveyorChain.size() * 3];
        for (int i = 0; i < conveyorChain.size(); i++) {
            BlockPos pos = conveyorChain.get(i).getBlockPos();
            poses[i * 3] = pos.getX();
            poses[i * 3 + 1] = pos.getY();
            poses[i * 3 + 2] = pos.getZ();
        }
        compoundTag.putIntArray("conveyorsChainPoses",poses);
        int[] outputPose =  {0,0,0};
        if(outputTile instanceof BlockEntity tile){
            BlockPos pos = tile.getBlockPos();
            outputPose = new int[]{pos.getX(), pos.getY(), pos.getZ()};
        }
        compoundTag.putIntArray("outputPose", outputPose);
    }

    public void load(CompoundTag compoundTag) {
        activated = false;
        int[] itemsIDs = compoundTag.getIntArray("itemsChain");
        itemsChain = new ArrayList<>();
        for (int i = 0; i < itemsIDs.length; i++) {
            ItemStack item = Registry.ITEM.byId(itemsIDs[i]).getDefaultInstance();
            SatisCraftory.LOGGER.warn("item saved : "+item);
            if(item.getItem() == Items.AIR){
                itemsChain.add(null);
            }
            else {
                itemsChain.add(item);
            }
            SatisCraftory.LOGGER.warn("item added : "+itemsChain.get(i));
        }

        loadPoses = compoundTag.getIntArray("conveyorsChainPoses");
        loadOutputPose = compoundTag.getIntArray("outputPose");
    }

    public void activate(BlockGetter world){
        activated = true;
        conveyorChain = new ArrayList<>();
        for (int i = 0; i < loadPoses.length; i+=3) {
            BlockPos conveyorPos = new BlockPos(loadPoses[i], loadPoses[i + 1], loadPoses[i + 2]);
            ConveyorTileEntity conveyorTile = WorldUtils.getTileEntity(ConveyorTileEntity.class, world, conveyorPos);
            conveyorTile.master = this;
            conveyorChain.add(conveyorTile);
        }

        BlockPos outputTilePos = new BlockPos(loadOutputPose[0], loadOutputPose[1], loadOutputPose[2]);
        outputTile = (IItemInputable) WorldUtils.getTileEntity(BlockEntity.class, world, outputTilePos);
        sendUpdateMasterConveyorLinkerPacket();
    }

    public boolean isActivated() {
        return activated;
    }
}
