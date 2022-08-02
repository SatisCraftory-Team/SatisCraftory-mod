package fr.vgtom4.satiscraftory.common.tileentity.base;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.common.init.TileEntityInit;
import fr.vgtom4.satiscraftory.common.interfaces.IBoundingBlock;
import fr.vgtom4.satiscraftory.common.network.packets.PacketUpdateTile;
import fr.vgtom4.satiscraftory.utils.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

public class TileEntityBoundingBlock extends MachineBaseTileEntity {

    private BlockPos mainPos = BlockPos.ZERO;

    private boolean receivedCoords;

    public TileEntityBoundingBlock(BlockPos pos, BlockState state) {
        super(TileEntityInit.BOUNDING_BLOCK, pos, state);
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
            LoggerFactory.getLogger("test").error("Found tile {} instead of an IBoundingBlock, at {}. Multiblock cannot function", tile, getMainPos());
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
    public CompoundTag getReducedUpdateTag() {
        CompoundTag updateTag = super.getReducedUpdateTag();
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
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        receivedCoords = tag.getBoolean("receivedCoords");
        int[] coords = tag.getIntArray("mainPos");
        if (coords != null && coords.length == 3) {
            setMainLocation(new BlockPos(coords[0], coords[1], coords[2]));
        }
        super.handleUpdateTag(tag);
    }

    public void sendUpdatePacket() {
        sendUpdatePacket(this);
    }

    public void sendUpdatePacket(BlockEntity tracking) {
        if (isRemote()) {
           // Mekanism.logger.warn("Update packet call requested from client side", new IllegalStateException());
        } else if (isRemoved()) {
           // Mekanism.logger.warn("Update packet call requested for removed tile", new IllegalStateException());
        } else {
            //Note: We use our own update packet/channel to avoid chunk trashing and minecraft attempting to rerender
            // the entire chunk when most often we are just updating a TileEntityRenderer, so the chunk itself
            // does not need to and should not be redrawn
            SatisCraftory.packetHandler.sendToAllTracking(new PacketUpdateTile(this), tracking);
        }
    }
}
