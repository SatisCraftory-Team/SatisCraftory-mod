package fr.vgtom4.satiscraftory.common.tileentity.base;

import fr.vgtom4.satiscraftory.common.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityBoundingBlock extends TileEntityUpdatable {

    private BlockPos mainPos = BlockPos.ZERO;

    private boolean receivedCoords;

    public TileEntityBoundingBlock(BlockPos pos, BlockState state) {
        super(TileEntityInit.BOUNDING_BLOCK, pos, state);
    }

    public void onNeighborChange(Block block, BlockPos neighborPos) {
        if (!isRemote()) {

        }
    }

    @Override
    public BlockPos getTilePos() {
        return null;
    }

    @Override
    public Level getTileWorld() {
        return null;
    }

    public boolean hasReceivedCoords() {
        return receivedCoords;
    }

    public BlockPos getMainPos() {
        if (mainPos == null) {
            mainPos = BlockPos.ZERO;
        }
        return mainPos;
    }
}
