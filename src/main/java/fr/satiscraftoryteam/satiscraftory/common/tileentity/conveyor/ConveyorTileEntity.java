package fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateMasterConveyorLinker;
import fr.satiscraftoryteam.satiscraftory.utils.MultiBlockUtil;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateConveyor;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class ConveyorTileEntity extends TickableTileEntity implements IItemStreamable {

    public static final int itemPerConveyor = 2;

    public ItemStack[] items = new ItemStack[itemPerConveyor];
    private IItemInputable output;
    public MasterConveyorLinker master;
    public boolean isMaster;
    private int itemPerMin = 60;

    public ConveyorTileEntity(BlockPos blockPos, BlockState blockState, boolean full) {
        super(TileEntityInit.CONVEYOR_FULL, blockPos, blockState);
        master = new MasterConveyorLinker(itemPerMin, this);

        master.itemsChain.add(ItemInit.IRON_RESIDUE.get().getDefaultInstance());
        master.itemsChain.add(ItemInit.IRON_RESIDUE.get().getDefaultInstance());
    }

    public ConveyorTileEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.CONVEYOR, blockPos, blockState);
        master = new MasterConveyorLinker(itemPerMin, this);
        master.itemsChain.add(null);
        master.itemsChain.add(null);
    }


    public void setItemPerMin(int itemPerMin) {
        this.itemPerMin = itemPerMin;
    }

    public int getItemPerMin() {
        return itemPerMin;
    }

    public ItemStack[] getItems() {
        return master.getItemsForConveyor(this);
    }



    private int tickCounter = 0;

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if(isMaster) {
            if(!master.isActivated()) master.activate(level);
            master.masterServerTick();
        }
        /*tickCounter++;
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
        }*/
    }

    @Override
    public void onClientTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if(isMaster) master.masterClientTick();
        /*tickCounter++;
        if(tickCounter >= 20f * (itemPerMin/60)) {
            tickCounter = 0;
            for(int i = items.length - 2; i >= 0; i--){
                if(items[i+1] == null){
                    items[i+1] = items[i];
                    items[i] = null;
                }
            }
        }*/
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putBoolean("master", isMaster);
        if(isMaster){

            master.save(compoundTag);
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        isMaster = compoundTag.getBoolean("master");
        if(isMaster){
            master.load(compoundTag);
        }
    }

    @Override
    public void setOutput(IItemInputable inputTile) {
        this.output = inputTile;
    }

    @Override
    public boolean canInputItem(ItemStack item) {
        return items[0] == null;
    }

    @Override
    public void inputItem(ItemStack item) {
        items[0] = item;
        sendUpdateConveyorPacket();
    }

    public boolean tryOutputItem(ItemStack item) {
        if(output != null && output.canInputItem(item)){
            output.inputItem(item);
            items[items.length - 1] = null;
            sendUpdateConveyorPacket();
            return true;
        }
        return false;
    }

    public float getProgress() {
        /*return  tickCounter / (20f * (itemPerMin/60));*/
        return master.progress;
    }

    public void handleUpdateConveyorPacket(PacketUpdateConveyor packetUpdateConveyor) {
        items = packetUpdateConveyor.getItems();
    }

    public void sendUpdateConveyorPacket(){
        SatisCraftory.packetHandler.sendToAllTracking(new PacketUpdateConveyor(this), this);
    }

    //todo: create packet
    public void handleUpdateMasterConveyorLinkerPacket(PacketUpdateMasterConveyorLinker updatePacket){
        master.handleUpdateMasterConveyorLinkerPacket(updatePacket);
    }

    public void sendUpdateMasterConveyorLinkerPacket(PacketUpdateMasterConveyorLinker packet){
        SatisCraftory.packetHandler.sendToAllTracking(packet, this);
    }

    public void onPlaced(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(ConveyorBlock.FACING);
        Vec3i inputConnectionPos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(new Vec3i(0,0,-1),direction);
        Vec3i outputConnectionPos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(new Vec3i(0,0,1),direction);
        BlockPos posForInputConnection = blockPos.offset(inputConnectionPos);
        BlockPos posForOutputConnection = blockPos.offset(outputConnectionPos);

        BlockEntity inputConnection = WorldUtils.getTileEntity(level, posForInputConnection);
        if(inputConnection instanceof IItemOutputable tileOutputable){
            if(inputConnection instanceof ConveyorTileEntity conveyorTile){
                conveyorTile.master.merge(master);
            }
            else {
                tileOutputable.setOutput(master);
            }
        }

        BlockEntity outputConnection = WorldUtils.getTileEntity(level, posForOutputConnection);
        if (outputConnection instanceof IItemInputable tileInputable){
            if(outputConnection instanceof ConveyorTileEntity conveyorTile){
                master.merge(conveyorTile.master);
            }
            else {
                master.trySetOutput(tileInputable,this);
            }
        }
    }


}
