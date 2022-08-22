package fr.satiscraftoryteam.satiscraftory.common.tileentity.base;

import fr.satiscraftoryteam.satiscraftory.common.registry.TileEntityRegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class TileEntityUpdatable extends BlockEntity {


    public TileEntityUpdatable(TileEntityRegistryObject<?> type, BlockPos pos, BlockState state) {
        super(type.get(), pos, state);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        return getReducedUpdateTag();
    }

    public CompoundTag getReducedUpdateTag() {
        //Add the base update tag information
        return super.getUpdateTag();
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

    public void handleUpdatePacket(@NotNull CompoundTag tag) {
        handleUpdateTag(tag);
    }


    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        //We don't want to do a full read from NBT so simply call the super's read method to let Forge do whatever
        // it wants, but don't treat this as if it was the full saved NBT data as not everything has to be synced to the client
        super.load(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (isRemote() && net.getDirection() == PacketFlow.CLIENTBOUND) {
            //Handle the update tag when we are on the client
            CompoundTag tag = pkt.getTag();
            if(tag != null) {
                handleUpdatePacket(tag);
            }
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
