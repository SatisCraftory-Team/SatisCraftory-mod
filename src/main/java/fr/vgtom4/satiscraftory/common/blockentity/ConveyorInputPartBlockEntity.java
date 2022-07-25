package fr.vgtom4.satiscraftory.common.blockentity;

import fr.vgtom4.satiscraftory.common.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ConveyorInputPartBlockEntity extends ConveyorStreamPartBlockEntity {

    public ConveyorInputPartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.CONVEYOR_INPUT_PART_ENTITY.get(), blockPos, blockState);
    }

    public boolean canInput(){
        //todo: check if machine can take new item
        return true;
    }

    public void input(ItemStack item){
        //todo: output item in machine
    }
}
