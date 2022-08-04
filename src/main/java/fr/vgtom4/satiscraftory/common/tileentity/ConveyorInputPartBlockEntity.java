package fr.vgtom4.satiscraftory.common.tileentity;

import fr.vgtom4.satiscraftory.common.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ConveyorInputPartBlockEntity extends ConveyorStreamPartBlockEntity {

    public ConveyorInputPartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.CONVEYOR_INPUT_PART_ENTITY, blockPos, blockState);
    }

    public boolean canInput(){
        //todo: check if machine can take new item
        return true;
    }

    public void input(ItemStack item){
        //todo: output item in machine
    }
}
