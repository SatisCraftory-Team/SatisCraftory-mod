package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TileEntityUpdatable;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateTileEntity(BlockPos pos, CompoundTag updateTag) implements IPacket {
    public static final CustomPacketPayload.Type<UpdateTileEntity> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("update_tile_entity"));
    public static final StreamCodec<ByteBuf, UpdateTileEntity> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateTileEntity::pos,
            ByteBufCodecs.TRUSTED_COMPOUND_TAG, UpdateTileEntity::updateTag,
            UpdateTileEntity::new
    );

    public UpdateTileEntity(TileEntityUpdatable tile) {
        this(tile.getBlockPos(), tile.getReducedUpdateTag(tile.getLevel().registryAccess()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        ClientLevel world = Minecraft.getInstance().level;
        //Only handle the update packet if the block is currently loaded
        if (WorldUtils.isBlockLoaded(world, pos)) {
            TileEntityUpdatable tile = WorldUtils.getTileEntity(TileEntityUpdatable.class, world, pos, true);
            if (tile == null) {
               SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but no valid tile was found.", pos,
                        world.dimension().location());
            } else {
                tile.handleUpdatePacket(updateTag, world.registryAccess());
            }
        }
    }
}
