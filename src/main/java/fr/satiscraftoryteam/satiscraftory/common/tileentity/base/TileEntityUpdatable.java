package fr.satiscraftoryteam.satiscraftory.common.tileentity.base;

import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.UpdateTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.registration.TileEntityDeferredHolder;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.GeoBlockAnimable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class TileEntityUpdatable<BE extends BlockEntity> extends GeoBlockAnimable<BE> {


    public TileEntityUpdatable(BlockEntityType<BE> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
        return getReducedUpdateTag(lookupProvider);
    }

    public CompoundTag getReducedUpdateTag(HolderLookup.Provider lookupProvider) {
        //Add the base update tag information
        return super.getUpdateTag(lookupProvider);
    }

    public void blockRemoved() {
    }

    @NotNull
    protected Level getWorldNN() {
        return Objects.requireNonNull(getLevel(), "getWorldNN called before world set");
    }

    public boolean isRemote() {
        return getWorldNN().isClientSide();
    }

    public void handleUpdatePacket(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        handleUpdateTag(tag, lookupProvider);
    }


    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        //We don't want to do a full read from NBT so simply call the super's read method to let Forge do whatever
        // it wants, but don't treat this as if it was the full saved NBT data as not everything has to be synced to the client
        super.loadAdditional(tag, lookupProvider);
    }

    @Override
    public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt, @NotNull HolderLookup.Provider provider) {
        if (isRemote() && net.getDirection() == PacketFlow.CLIENTBOUND) {
            //Handle the update tag when we are on the client
            CompoundTag tag = pkt.getTag();
            if(tag != null) {
                handleUpdatePacket(tag, provider);
            }
        }
    }

    public void sendUpdatePacket(BlockEntity tracking) {
        if (isRemote()) {
            // Mekanism.logger.warn("Update packet call requested from client side", new IllegalStateException());
        } else if (isRemoved()) {
            // Mekanism.logger.warn("Update packet call requested for removed tile", new IllegalStateException());
        } else {
            //TODO: custom channel read bellow
            //Note: We use our own update packet/channel to avoid chunk trashing and minecraft attempting to rerender
            // the entire chunk when most often we are just updating a TileEntityRenderer, so the chunk itself
            // does not need to and should not be redrawn
            PacketDistributor.sendToAllPlayers(new UpdateTileEntity(this));
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
