package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.UpdateMachineInfosClient;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateMachineInfosServer(BlockPos pos, boolean isActive, int overclockPercentage) implements IPacket {
    public static final CustomPacketPayload.Type<UpdateMachineInfosServer> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("update_packet_infos"));
    public static final StreamCodec<ByteBuf, UpdateMachineInfosServer> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateMachineInfosServer::pos,
            ByteBufCodecs.BOOL, UpdateMachineInfosServer::isActive,
            ByteBufCodecs.INT, UpdateMachineInfosServer::overclockPercentage,
            UpdateMachineInfosServer::new
    );

    public UpdateMachineInfosServer(BlockPos pos, boolean isActive, int overclockPercentage) {
        this.pos = pos;
        this.isActive = isActive;
        this.overclockPercentage = overclockPercentage;
    }

    @NotNull
    @Override
    public CustomPacketPayload.Type<UpdateMachineInfosServer> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            final MachineBaseTileEntity<?> blockEntity = WorldUtils.getTileEntity(MachineBaseTileEntity.class, context.player().level(), this.pos, true);
            blockEntity.setActive(isActive);
            blockEntity.updateMachineInfos(overclockPercentage);

            PacketDistributor.sendToPlayer(((ServerPlayer)context.player()), new UpdateMachineInfosClient(this.pos, isActive, overclockPercentage));
        });
    }
}
