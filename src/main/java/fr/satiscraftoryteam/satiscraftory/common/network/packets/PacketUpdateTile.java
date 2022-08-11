package fr.satiscraftoryteam.satiscraftory.common.network.packets;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TileEntityUpdatable;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateTile implements IPacket {

    private final CompoundTag updateTag;
    private final BlockPos pos;

    public PacketUpdateTile(TileEntityUpdatable tile) {
        this(tile.getBlockPos(), tile.getReducedUpdateTag());
    }

    private PacketUpdateTile(BlockPos pos, CompoundTag updateTag) {
        this.pos = pos;
        this.updateTag = updateTag;
    }

    public void handle(NetworkEvent.Context context) {
        ClientLevel world = Minecraft.getInstance().level;
        //Only handle the update packet if the block is currently loaded
        if (WorldUtils.isBlockLoaded(world, pos)) {
            TileEntityUpdatable tile = WorldUtils.getTileEntity(TileEntityUpdatable.class, world, pos, true);
            if (tile == null) {
               SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but no valid tile was found.", pos,
                        world.dimension().location());
            } else {
                tile.handleUpdatePacket(updateTag);
            }
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeNbt(updateTag);
    }

    public static PacketUpdateTile decode(FriendlyByteBuf buffer) {
        return new PacketUpdateTile(buffer.readBlockPos(), buffer.readNbt());
    }

}
