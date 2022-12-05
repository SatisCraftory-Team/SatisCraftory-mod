package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.utils.MultiBlockUtil;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateConveyor;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ConveyorTileEntity extends TickableTileEntity implements IItemStreamable {

    public static final int itemPerConveyor = 2;

    public Item[] items = new Item[itemPerConveyor];
    private IItemInputable output;
    private int itemPerMin = 60;

    public ConveyorTileEntity(BlockPos blockPos, BlockState blockState, boolean full) {
        super(TileEntityInit.CONVEYOR_FULL, blockPos, blockState);

        items[0] = (ItemInit.IRON_RESIDUE.get());
        items[1] = (ItemInit.IRON_RESIDUE.get());
    }

    public ConveyorTileEntity(BlockPos blockPos, BlockState blockState) {
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
    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        tickCounter++;
        if(tickCounter >= 20f * (itemPerMin/60)){
            tickCounter = 0;
            if(items[items.length - 1] != null){
                tryOutputItem(items[items.length - 1]);
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
    public void onClientTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
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
    public void setOutput(IItemInputable inputTile) {
        this.output = inputTile;
    }

    @Override
    public boolean canInputItem(Item item) {
        return items[0] == null;
    }

    @Override
    public void inputItem(Item item) {
        items[0] = item;
        sendUpdateConveyorPacket();
    }

    public boolean tryOutputItem(Item item) {
        if(output != null && output.canInputItem(item)){
            output.inputItem(item);
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
        Direction direction = blockState.getValue(ConveyorBlock.FACING);
        Vec3i inputConnectionPos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(new Vec3i(0,0,-1),direction);
        Vec3i outputConnectionPos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(new Vec3i(0,0,1),direction);
        BlockPos posForInputConnection = blockPos.offset(inputConnectionPos);
        BlockPos posForOutputConnection = blockPos.offset(outputConnectionPos);

        if(WorldUtils.getTileEntity(level, posForInputConnection) instanceof IItemOutputable){
            ((IItemOutputable) WorldUtils.getTileEntity(level, posForInputConnection)).setOutput(this);
        }

        if(WorldUtils.getTileEntity(level, posForOutputConnection) instanceof IItemInputable){
            setOutput((IItemInputable) WorldUtils.getTileEntity(level, posForOutputConnection));
        }
    }
}
