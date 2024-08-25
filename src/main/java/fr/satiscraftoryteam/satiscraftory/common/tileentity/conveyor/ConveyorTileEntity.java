package fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor;

import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.MultiBlockUtil;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ConveyorTileEntity extends TickableTileEntity<ConveyorTileEntity> {

    public static final int itemPerConveyor = 2;
    private IItemInputable output;
    private ConveyorLinker linker;
    private boolean isMaster;
    private int itemPerMin = 60;

    public ConveyorTileEntity(BlockPos blockPos, BlockState blockState, boolean full) {
        super(TileEntityInit.CONVEYOR_FULL.get(), blockPos, blockState);
        linker = new ConveyorLinker(itemPerMin);
        linker.conveyorChain.add(this);
        isMaster = true;

        linker.itemsChain.add(ItemInit.IRON_RESIDUE.get().getDefaultInstance());
        linker.itemsChain.add(ItemInit.IRON_RESIDUE.get().getDefaultInstance());
    }

    public ConveyorTileEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.CONVEYOR.get(), blockPos, blockState);
        linker = new ConveyorLinker(itemPerMin);
        linker.conveyorChain.add(this);
        isMaster = true;

        linker.itemsChain.add(ItemStack.EMPTY);
        linker.itemsChain.add(ItemStack.EMPTY);
    }


    public void setItemPerMin(int itemPerMin) {
        this.itemPerMin = itemPerMin;
    }

    public int getItemPerMin() {
        return itemPerMin;
    }

    public ItemStack[] getItems() {
        return linker.getItemsForConveyor(this);
    }


    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if(isMaster) {
            if(!linker.isActivated()) linker.activate(level);
            linker.masterServerTick();
        }
    }

    @Override
    public void onClientTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if(isMaster) {
            if(!linker.isActivated()) linker.activate(level);
            linker.masterClientTick();
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        isMaster = tag.getBoolean("master");
        if(isMaster){
            linker.load(tag);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putBoolean("master", isMaster);
        if(isMaster){
            linker.save(tag);
        }
    }

    public void setLinker(ConveyorLinker linker){
        this.linker = linker;
    }

    public ConveyorLinker getLinker(){
        return this.linker;
    }

    public void setIsMaster(boolean value){
        isMaster = value;
    }

    public boolean isMaster(){
        return isMaster;
    }


    public float getProgress() {
        return linker.progress;
    }


    public void onPlaced(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(ConveyorBlock.FACING);
        Vec3i inputConnectionPos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(new Vec3i(0,0,-1),direction);
        Vec3i outputConnectionPos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(new Vec3i(0,0,1),direction);
        BlockPos posForInputConnection = blockPos.offset(inputConnectionPos);
        BlockPos posForOutputConnection = blockPos.offset(outputConnectionPos);

        BlockEntity inputConnection = WorldUtils.getTileEntity(level, posForInputConnection);
        if(inputConnection instanceof ConveyorTileEntity conveyorTile){
            conveyorTile.linker.merge(linker);
        }
        else {
            if(inputConnection instanceof IItemOutputable tileOutputable){
                tileOutputable.setOutput(linker);
            }
        }

        BlockEntity outputConnection = WorldUtils.getTileEntity(level, posForOutputConnection);
        if(outputConnection instanceof ConveyorTileEntity conveyorTile){
            linker.merge(conveyorTile.linker);
        }
        else {
            if (outputConnection instanceof IItemInputable tileInputable){
                linker.trySetOutput(tileInputable,this);
            }
        }
    }

    public void onRemove(Level level, BlockPos blockPos, BlockState blockState) {

        Direction direction = blockState.getValue(ConveyorBlock.FACING);
        Vec3i inputConnectionPos = MultiBlockUtil.getAbsolutePosFromRelativeFacingSouth(new Vec3i(0,0,-1),direction);
        BlockPos posForInputConnection = blockPos.offset(inputConnectionPos);
        BlockEntity inputConnection = WorldUtils.getTileEntity(level, posForInputConnection);
        if(inputConnection instanceof IItemOutputable tileOutputable){
            tileOutputable.setOutput(null);
        }

        ConveyorLinker.removeConveyor(this);
    }
}
