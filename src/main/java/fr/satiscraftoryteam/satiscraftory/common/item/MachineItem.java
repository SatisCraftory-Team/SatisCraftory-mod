package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.common.block.base.MachineBaseBlock;
import fr.satiscraftoryteam.satiscraftory.common.block.base.RestrictedPlacementAttribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class MachineItem extends BlockItem {

    public MachineItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext blockPlaceContext, BlockState blockState) {
        MachineBaseBlock block = (MachineBaseBlock) blockState.getBlock();

        if (block.has(RestrictedPlacementAttribute.class)) {
            if (!Arrays.asList(block.get(RestrictedPlacementAttribute.class).blocks()).contains(blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().below()).getBlock())) {
                return false;
            }
        }
        return super.canPlace(blockPlaceContext, blockState);
    }
}
