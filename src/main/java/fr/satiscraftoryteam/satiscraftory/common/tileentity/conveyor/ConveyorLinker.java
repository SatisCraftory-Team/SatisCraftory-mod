package fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketNewConveyorLinker;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateConveyorLinker;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConveyorLinker implements IItemStreamable{

    public final int itemPerMin; //thing todo
    public List<ConveyorTileEntity> conveyorChain;
    public List<ItemStack> itemsChain;
    public IItemInputable outputTile;
    public float progress;
    public int itemStuckIndex; //item where all items after are not moving todo: implement this

    public int tickCounter;

    private int[] loadPoses; //buffer to connect conveyor when world is loaded
    private int[] loadOutputPose;
    private boolean activated;

    public ConveyorLinker(int itemPerMin) {
        activated = true;
        this.itemPerMin = itemPerMin;
        conveyorChain = new ArrayList<>();
        itemsChain = new ArrayList<>();
    }

    public ConveyorLinker merge(ConveyorLinker toMerge){
        toMerge.conveyorChain.get(0).setIsMaster(false);
        itemsChain.addAll(toMerge.itemsChain);
        conveyorChain.addAll(toMerge.conveyorChain);
        updateAllLinkers();
        outputTile = toMerge.outputTile;
        return this;
    }

    public static void removeConveyor(ConveyorTileEntity conveyorTile){
        ConveyorLinker firstLinker = conveyorTile.getLinker();
        int index = firstLinker.conveyorChain.indexOf(conveyorTile);
        int initialSize = firstLinker.conveyorChain.size();
        if(index == 0){
            firstLinker.removeFirstConveyor();
        }
        else if (index == initialSize - 1) {
            firstLinker.removeLastConveyor();
        }
        else {
            ConveyorLinker lastLinker = new ConveyorLinker(firstLinker.itemPerMin);
            SatisCraftory.LOGGER.info("old linker conveyor positions :");
            firstLinker.conveyorChain.forEach(conveyorTileEntity -> SatisCraftory.LOGGER.info(conveyorTileEntity.getBlockPos()));
            firstLinker.transferConveyors(index + 1, initialSize - index - 1, lastLinker);
            firstLinker.removeLastConveyor();
            firstLinker.sendSplitLinkerUpdate(lastLinker);
            SatisCraftory.LOGGER.info("new linker conveyor positions :");
            lastLinker.conveyorChain.forEach(conveyorTileEntity -> SatisCraftory.LOGGER.info(conveyorTileEntity.getBlockPos()));
        }
    }

    private void removeStart(int length){
        for (int i = 0; i < length; i++) {
            itemsChain.remove(0);
            itemsChain.remove(0);
            conveyorChain.remove(0);
        }

        if(!conveyorChain.isEmpty())
            conveyorChain.get(0).setIsMaster(true);
    }

    public void transferConveyors(int startIndex, int length, ConveyorLinker target){
        for (int i = 0; i < length; i++) {
            target.itemsChain.add(itemsChain.remove(startIndex * 2));
            target.itemsChain.add(itemsChain.remove(startIndex * 2));
            target.conveyorChain.add(conveyorChain.remove(startIndex));
        }
        target.outputTile = outputTile;
        outputTile = null;
        target.localUpdate();
    }

    private void removeEnd(int length){
        int initialSize = conveyorChain.size();
        for (int i = initialSize - 1; i >= initialSize - length; i--) {
            itemsChain.remove(i * 2 + 1);
            itemsChain.remove(i * 2);
            conveyorChain.remove(i);
        }

        outputTile = null;
    }

    public void removeLastConveyor(){
        removeEnd(1);
    }

    public void removeFirstConveyor(){
        removeStart(1);
    }

    public void masterClientTick(){
        tickCounter++;
        if(tickCounter >= 20f * (itemPerMin/60)){
            tickCounter = 0;
            for(int i = itemsChain.size() - 2; i >= 0; i--){
                if(itemsChain.get(i+1).isEmpty()){
                    itemsChain.set(i+1,itemsChain.get(i));
                    itemsChain.set(i,ItemStack.EMPTY);
                }
            }
        }
        progress = tickCounter / 20f * (itemPerMin/60);
    }

    public void masterServerTick(){
        tickCounter++;
        if(tickCounter >= 20f * (itemPerMin/60)){
            tickCounter = 0;
            if(itemsChain.get(itemsChain.size() - 1) != ItemStack.EMPTY){
                tryOutputItem(itemsChain.get(itemsChain.size() - 1));
            }
            for(int i = itemsChain.size() - 2; i >= 0; i--){
                if(itemsChain.get(i+1).isEmpty()){
                    itemsChain.set(i+1,itemsChain.get(i));
                    itemsChain.set(i,ItemStack.EMPTY);
                }
            }
            conveyorChain.get(0).setChanged(); // conveyor linker are saved on master conveyor
            sendLinkerUpdate();
        }
    }

    public boolean tryOutputItem(ItemStack item) {
        if(outputTile != null && outputTile.canInputItem(item)){
            outputTile.inputItem(item);
            itemsChain.set(itemsChain.size() - 1, ItemStack.EMPTY);
            sendLinkerUpdate();
            return true;
        }
        return false;
    }

    private void localUpdate(){
        conveyorChain.forEach(conveyorTile -> {
            conveyorTile.setLinker(this);
            conveyorTile.setIsMaster(false);
        });
        conveyorChain.get(0).setIsMaster(true);
    }

    public void sendLinkerUpdate() {
        SatisCraftory.LOGGER.info("sendUpdateMasterConveyorLinkerPacket");
        ConveyorTileEntity masterConveyor = conveyorChain.get(0);
        PacketUpdateConveyorLinker updatePacket = new PacketUpdateConveyorLinker(tickCounter, itemsChain, conveyorChain, outputTile);
        SatisCraftory.packetHandler.sendToAllTracking(updatePacket, masterConveyor);
    }

    private void sendSplitLinkerUpdate(ConveyorLinker newLinker){
        SatisCraftory.LOGGER.info("sendNewLinkerUpdate");
        ConveyorTileEntity previousMaster = conveyorChain.get(0);
        PacketUpdateConveyorLinker updatePacket = new PacketNewConveyorLinker(tickCounter, newLinker.itemsChain, newLinker.conveyorChain, newLinker.outputTile, previousMaster);
        SatisCraftory.packetHandler.sendToAllTracking(updatePacket, previousMaster);
    }

    public void handleLinkerUpdate(PacketUpdateConveyorLinker updatePacket){
        SatisCraftory.LOGGER.info("handleUpdateLinkerPacket");
        tickCounter = updatePacket.getTickCounter();
        itemsChain = updatePacket.getItemsChain();
        conveyorChain = updatePacket.getConveyorChainDeducedFromPosition();
        outputTile = updatePacket.getOutputTile();
        localUpdate();
    }

    public void handleSplitLinkerUpdate(PacketNewConveyorLinker updatePacket){
        SatisCraftory.LOGGER.info("handleSplitLinkerUpdate");
        ConveyorLinker newLinker = new ConveyorLinker(itemPerMin);
        newLinker.handleLinkerUpdate(updatePacket);
    }



    @Override
    public boolean canInputItem(ItemStack itemStack) {
        return itemsChain.get(0) == ItemStack.EMPTY;
    }

    @Override
    public void inputItem(ItemStack itemStack) {
        SatisCraftory.LOGGER.info("inputItem");
        itemsChain.set(0,itemStack);
        sendLinkerUpdate();
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

    public void updateAllLinkers(){
        for(ConveyorTileEntity conveyorTileEntity : conveyorChain){
            conveyorTileEntity.setLinker(this);
        }
    }

    public ItemStack[] getItemsForConveyor(int conveyorIndex){
        if(conveyorIndex * 2 + 1 > itemsChain.size() - 1){
            SatisCraftory.LOGGER.error("trying to get items for conveyor not in conveyor chain");
            return null;
        }
        return new ItemStack[]{itemsChain.get(conveyorIndex * 2), itemsChain.get(conveyorIndex * 2 + 1)};
    }

    public void save(CompoundTag compoundTag) {
        SatisCraftory.LOGGER.info("saving items : "+itemsChain);
        compoundTag.putIntArray("itemsChain", itemsChain.stream().mapToInt((itemStack -> Item.getId(itemStack.getItem()))).toArray());
        int[] poses = new int[conveyorChain.size() * 3];
        for (int i = 0; i < conveyorChain.size(); i++) {
            BlockPos pos = conveyorChain.get(i).getBlockPos();
            poses[i * 3] = pos.getX();
            poses[i * 3 + 1] = pos.getY();
            poses[i * 3 + 2] = pos.getZ();
        }
        SatisCraftory.LOGGER.info("saving poses : "+ Arrays.stream(poses).boxed().toList());
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
            itemsChain.add(item);
        }

        loadPoses = compoundTag.getIntArray("conveyorsChainPoses");
        loadOutputPose = compoundTag.getIntArray("outputPose");
    }

    public void activate(Level world){
        SatisCraftory.LOGGER.warn("activating conveyor linker");
        activated = true;
        conveyorChain = new ArrayList<>();
        SatisCraftory.LOGGER.info("conveyor poses : "+Arrays.stream(loadPoses).boxed().toList());
        for (int i = 0; i < loadPoses.length; i+=3) {
            BlockPos conveyorPos = new BlockPos(loadPoses[i], loadPoses[i + 1], loadPoses[i + 2]);
            ConveyorTileEntity conveyorTile = WorldUtils.getTileEntity(ConveyorTileEntity.class, world, conveyorPos);
            conveyorChain.add(conveyorTile);
            SatisCraftory.LOGGER.info("adding conveyor to chain : "+conveyorTile.getBlockPos());
        }

        SatisCraftory.LOGGER.info("conveyor chain masters before update : "+conveyorChain.stream().map((conveyorTileEntity -> conveyorTileEntity.getLinker().toString())).toList());
        updateAllLinkers();
        SatisCraftory.LOGGER.info("conveyor chain masters after update : "+conveyorChain.stream().map((conveyorTileEntity -> conveyorTileEntity.getLinker().toString())).toList());

        BlockPos outputTilePos = new BlockPos(loadOutputPose[0], loadOutputPose[1], loadOutputPose[2]);
        outputTile = (IItemInputable) WorldUtils.getTileEntity(BlockEntity.class, world, outputTilePos);

        conveyorChain.get(0).setIsMaster(true);

        if(!world.isClientSide()){
            sendLinkerUpdate();
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public BlockPos getPos() {
        return conveyorChain.get(0).getBlockPos();
    }
}
