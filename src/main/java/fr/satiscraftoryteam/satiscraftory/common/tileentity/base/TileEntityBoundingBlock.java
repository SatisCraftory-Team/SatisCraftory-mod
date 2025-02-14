package fr.satiscraftoryteam.satiscraftory.common.tileentity.base;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityBoundingBlock extends TileEntityUpdatable<TileEntityBoundingBlock> {

    private BlockPos mainPos = BlockPos.ZERO;

    private boolean receivedCoords;

    public TileEntityBoundingBlock(BlockPos pos, BlockState state) {
        super(TileEntityInit.BOUNDING_BLOCK.get(), pos, state);
    }


    @Override
    public boolean triggerEvent(int id, int param) {
        boolean handled = super.triggerEvent(id, param);
        IBoundingBlock main = getMain();
        return main != null && main.triggerBoundingEvent(worldPosition.subtract(getMainPos()), id, param) || handled;
    }

    @Nullable
    public BlockEntity getMainTile() {
        return receivedCoords ? WorldUtils.getTileEntity(level, getMainPos()) : null;
    }

    @Nullable
    private IBoundingBlock getMain() {
        // Return the main tile; note that it's possible, esp. when chunks are
        // loading that the main tile has not yet loaded and thus is null.
        BlockEntity tile = getMainTile();
        if (tile != null && !(tile instanceof IBoundingBlock)) {
            // On the off chance that another block got placed there (which seems only likely with corruption, go ahead and log what we found.)
            SatisCraftory.LOGGER.error("Found tile {} instead of an IBoundingBlock, at {}. Multiblock cannot function", tile, getMainPos());
            return null;
        }
        return (IBoundingBlock) tile;
    }

    public void onNeighborChange(Block block, BlockPos neighborPos) {
        if (!isRemote()) {
            //System.out.println("aaa");
        }
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

    public void setMainLocation(BlockPos pos) {
        receivedCoords = pos != null;
        mainPos = pos;
        if (!isRemote()) {
            sendUpdatePacket();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider lookupProvider) {
        super.saveAdditional(compoundTag, lookupProvider);
        int[] coords = new int[]{
                mainPos.getX(),
                mainPos.getY(),
                mainPos.getZ()
        };
        compoundTag.putIntArray("mainPos", coords);
        compoundTag.putBoolean("receivedCoords", receivedCoords);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.loadAdditional(tag, lookupProvider);
        receivedCoords = tag.getBoolean("receivedCoords");
        int[] coords = tag.getIntArray("mainPos");
        mainPos = new BlockPos(coords[0], coords[1], coords[2]);
}

    @Override
    public CompoundTag getReducedUpdateTag(HolderLookup.Provider lookupProvider) {
        CompoundTag updateTag = super.getReducedUpdateTag(lookupProvider);
        int[] coords = new int[]{
                mainPos.getX(),
                mainPos.getY(),
                mainPos.getZ()
        };
        updateTag.putIntArray("mainPos", coords);
        updateTag.putBoolean("receivedCoords", receivedCoords);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        receivedCoords = tag.getBoolean("receivedCoords");
        int[] coords = tag.getIntArray("mainPos");
        setMainLocation(new BlockPos(coords[0], coords[1], coords[2]));
        super.handleUpdateTag(tag, lookupProvider);
    }
}
