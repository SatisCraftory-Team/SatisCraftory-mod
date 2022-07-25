package fr.vgtom4.satiscraftory.common.blockentity;

import fr.vgtom4.satiscraftory.common.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ConveyorOutputPartBlockEntity extends ConveyorStreamPartBlockEntity {

    public ConveyorOutputPartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.CONVEYOR_OUTPUT_PART_ENTITY.get(), blockPos, blockState);
    }

    public boolean canOutput(){
        //todo: check if conveyor can take new item
        return true;
    }

    public void output(ItemStack item){
        //todo: output item on conveyor
    }
}
