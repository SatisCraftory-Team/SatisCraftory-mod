package fr.satiscraftoryteam.satiscraftory.common.network;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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
import java.util.function.Function;

public abstract class AbtractPacketHandler {

    private final SimpleChannel netHandler = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(SatisCraftory.MODID))
            .clientAcceptedVersions("1"::equals)
            .serverAcceptedVersions("1"::equals)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();


    private int packetIndex = 0;
    // S2C = Server to Client
    protected <MSG extends IPacket> void registerS2CPacket(Class<MSG> type, Function<FriendlyByteBuf, MSG> decode) {
        registerPacketInternal(type, decode, NetworkDirection.PLAY_TO_SERVER);
    }

    protected <MSG extends IPacket> void registerC2SPacket(Class<MSG> type, Function<FriendlyByteBuf, MSG> decode) {
        registerPacketInternal(type, decode, NetworkDirection.PLAY_TO_CLIENT);
    }

    protected <MSG extends IPacket> void registerPacketInternal(Class<MSG> type, Function<FriendlyByteBuf, MSG> decode, NetworkDirection direction) {
        netHandler.registerMessage(packetIndex++, type, IPacket::encode, decode, IPacket::handle, Optional.of(direction));
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
