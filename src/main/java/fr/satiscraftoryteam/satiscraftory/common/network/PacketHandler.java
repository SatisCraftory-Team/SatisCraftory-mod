package fr.satiscraftoryteam.satiscraftory.common.network;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateConveyor;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateMasterConveyorLinker;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateTile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketHandler  {

    private final SimpleChannel netHandler = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(SatisCraftory.MODID))
                .clientAcceptedVersions("1"::equals)
                .serverAcceptedVersions("1"::equals)
                .networkProtocolVersion(() -> "1")
                .simpleChannel();

    public void init() {

        netHandler.registerMessage(1, PacketUpdateTile.class, IPacket::encode, PacketUpdateTile::decode, IPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        netHandler.registerMessage(2, PacketUpdateConveyor.class, IPacket::encode, PacketUpdateConveyor::decode, IPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        netHandler.registerMessage(3, PacketUpdateMasterConveyorLinker.class, IPacket::encode, PacketUpdateMasterConveyorLinker::decode, IPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public <MSG> void sendToAllTracking(MSG message, Entity entity) {
        netHandler.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    public <MSG> void sendToAllTracking(MSG message, BlockEntity tile) {
        sendToAllTracking(message, tile.getLevel(), tile.getBlockPos());
    }

    public <MSG> void sendToAllTracking(MSG message, Level world, BlockPos pos) {
        if (world instanceof ServerLevel level) {
            //If we have a ServerWorld just directly figure out the ChunkPos to not require looking up the chunk
            // This provides a decent performance boost over using the packet distributor
            level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> sendTo(message, p));
        } else {
            //Otherwise, fallback to entities tracking the chunk if some mod did something odd and our world is not a ServerWorld
            netHandler.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)), message);
        }
    }

    public <MSG> void sendTo(MSG message, ServerPlayer player) {
        //Validate it is not a fake player, even though none of our code should call this with a fake player
        if (!(player instanceof FakePlayer)) {
            netHandler.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
