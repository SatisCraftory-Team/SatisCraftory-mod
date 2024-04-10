package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import java.util.Arrays;

public class ConveyorDebugger extends Item {
    public ConveyorDebugger(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        BlockPos clickPos = useOnContext.getClickedPos();
        ConveyorTileEntity conveyor = WorldUtils.getTileEntity(ConveyorTileEntity.class, useOnContext.getLevel(), clickPos);
        if(conveyor != null){
            if(useOnContext.getPlayer().isCrouching()){
                conveyor.getLinker().sendLinkerUpdate();
            }
            else{
                useOnContext.getPlayer().sendSystemMessage(Component.literal(
                        "--------------------"
                                +"\nClient side : " + conveyor.getLevel().isClientSide
                                +"\nMaster is : "+conveyor.getLinker()
                                +"\nConveyor is master holder : "+conveyor.isMaster()
                                +"\nConveyor index in master is : " +conveyor.getLinker().conveyorChain.indexOf(conveyor)
                                +"\nConveyor render items are : "+ Arrays.stream(conveyor.getLinker().getItemsForConveyor(conveyor)).map(itemStack -> itemStack == null ? null : itemStack.getItem()).toList()
                                +"\nMaster items are : "+conveyor.getLinker().itemsChain.stream().map(itemStack -> itemStack == null ? null : itemStack.getItem()).toList()
                                +"\n--------------------"));
            }

        }
        else{
            useOnContext.getPlayer().sendSystemMessage(Component.literal("No conveyor have been found at this position"));
        }
        return InteractionResult.PASS;
    }
}
