package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class ConveyorOutputPartBlockEntity extends ConveyorStreamPartBlockEntity implements IItemOutputable {

    private MachineBaseTileEntity machine;
    private IItemInputable output;
    public ConveyorOutputPartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.CONVEYOR_OUTPUT_PART_ENTITY, blockPos, blockState);
    }

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if(machine == null || output == null) return;
        ItemStackHandler outputInv = machine.getOutputInventory();
        for (int i = outputInv.getSlots() - 1; i >= 0; i--){
            ItemStack stack = outputInv.getStackInSlot(i);
            if(!stack.isEmpty()){
                if(tryOutputItem(stack.getItem())){
                    stack.setCount(stack.getCount()-1);
                    return;
                }
            }
        }
    }

    public boolean tryOutputItem(Item item) {
        if(output != null && output.canInputItem(item)){
            output.inputItem(item);
            return true;
        }
        return false;
    }

    @Override
    public void setOutput(IItemInputable inputTile) {
        output = inputTile;
    }

    public void setMachine(MachineBaseTileEntity machine){
        this.machine = machine;
    }
}
