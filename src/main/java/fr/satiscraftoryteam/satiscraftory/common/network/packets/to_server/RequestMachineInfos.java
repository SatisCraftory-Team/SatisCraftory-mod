package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.UpdateMachineInfosClient;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestMachineInfos(BlockPos pos) implements IPacket {
    public static final CustomPacketPayload.Type<RequestMachineInfos> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("request_machine_infos"));
    public static final StreamCodec<ByteBuf, RequestMachineInfos> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RequestMachineInfos::pos,
            RequestMachineInfos::new
    );

    @NotNull
    @Override
    public CustomPacketPayload.Type<RequestMachineInfos> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            final MachineBaseTileEntity blockEntity = (MachineBaseTileEntity) context.player().level().getBlockEntity(this.pos);
            PacketDistributor.sendToAllPlayers(new UpdateMachineInfosClient(this.pos, blockEntity.isActive, blockEntity.overclockPercentage));
        });
    }

}
