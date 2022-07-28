package fr.vgtom4.satiscraftory.common.tileentity;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.block.ConveyorBlock;
import fr.vgtom4.satiscraftory.common.init.ItemInit;
import fr.vgtom4.satiscraftory.common.init.TileEntityInit;
import fr.vgtom4.satiscraftory.common.network.packets.PacketUpdateConveyor;
import fr.vgtom4.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.vgtom4.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ConveyorBlockEntity extends MachineBaseTileEntity implements IInputableItem {

    public static final int itemPerConveyor = 2;

    public Item[] items = new Item[itemPerConveyor];
    private IInputableItem connectedStream;
    private int itemPerMin = 60;

    public ConveyorBlockEntity(BlockPos blockPos, BlockState blockState, boolean full) {
        super(TileEntityInit.CONVEYOR_FULL, blockPos, blockState);

        items[0] = (ItemInit.IRON_RESIDUE.get());
        items[1] = (ItemInit.IRON_RESIDUE.get());
    }

    public ConveyorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.CONVEYOR, blockPos, blockState);
    }


    public void setItemPerMin(int itemPerMin) {
        this.itemPerMin = itemPerMin;
    }

    public int getItemPerMin() {
        return itemPerMin;
    }

    public Item[] getItems() {
        return items;
    }



    private int tickCounter = 0;

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, MachineBaseTileEntity tile) {
        tickCounter++;
        if(tickCounter >= 20f * (itemPerMin/60)){
            tickCounter = 0;
            if(items[items.length - 1] != null){
                TryOutputItem(items[items.length - 1]);
            }
            for(int i = items.length - 2; i >= 0; i--){
                if(items[i+1] == null){
                    items[i+1] = items[i];
                    items[i] = null;
                }
            }
        }
    }

    @Override
    public void onClientTick(Level level, BlockPos pos, BlockState state, MachineBaseTileEntity tile) {
        tickCounter++;
        if(tickCounter >= 20f * (itemPerMin/60)) {
            tickCounter = 0;
            for(int i = items.length - 2; i >= 0; i--){
                if(items[i+1] == null){
                    items[i+1] = items[i];
                    items[i] = null;
                }
            }
        }
    }

    @Override
    public CompoundTag getReducedUpdateTag() {
        return super.getReducedUpdateTag();
    }

    @Override
    public void setConnectedStream(IInputableItem stream) {
        this.connectedStream = stream;
    }

    @Override
    public boolean canInputItem(Item item) {
        return items[0] == null;
    }

    @Override
    public void InputItem(Item item) {
        items[0] = item;
        sendUpdateConveyorPacket();
    }

    public boolean TryOutputItem(Item item) {
        if(connectedStream != null && connectedStream.canInputItem(item)){
            connectedStream.InputItem(item);
            items[items.length - 1] = null;
            sendUpdateConveyorPacket();
            return true;
        }
        return false;
    }

    public float getProgress() {
        return  tickCounter / (20f * (itemPerMin/60));
    }

    public void handleUpdateConveyorPacket(PacketUpdateConveyor packetUpdateConveyor) {
        items = packetUpdateConveyor.getItems();
    }

    public void sendUpdateConveyorPacket(){
        SatisCraftory.packetHandler.sendToAllTracking(new PacketUpdateConveyor(this), this);
    }

    public void onPlaced(Level level, BlockPos blockPos, BlockState blockState) {
        BlockPos posForConnection;
        switch (blockState.getValue(ConveyorBlock.FACING)) {
            case EAST:
                posForConnection = blockPos.west();
                break;
            case WEST:
                posForConnection = blockPos.east();
                break;
            case NORTH:
                posForConnection = blockPos.south();
                break;
            case SOUTH:
                posForConnection = blockPos.north();
                break;
            default:
                return;
        }

        if(WorldUtils.getTileEntity(level, posForConnection) instanceof IInputableItem){
            ((IInputableItem) WorldUtils.getTileEntity(level, posForConnection)).setConnectedStream(this);
        }
    }
}
