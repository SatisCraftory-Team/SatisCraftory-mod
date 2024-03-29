package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class ConveyorStreamPartBlockEntity extends TickableTileEntity {

    private BlockEntity master;
    private BlockEntity connectedConveyor;

    public ConveyorStreamPartBlockEntity(TileEntityRegistryObject<?> blockType, BlockPos blockPos, BlockState blockState) {
        super(blockType, blockPos, blockState);
    }

    public void setMaster(BlockEntity master) {
        this.master = master;
    }

    public void setConnectedConveyor(BlockEntity connectedConveyor) {
        this.connectedConveyor = connectedConveyor;
    }

    public void onUse(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (master != null) {
            master.getBlockState().use(level, player, interactionHand, blockHitResult);
        }
    }
}
