package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateMachineInfos(BlockPos pos, boolean isActive, int overclockPercentage) implements IPacket {
    public static final CustomPacketPayload.Type<UpdateMachineInfos> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("update_packet_infos"));
    public static final StreamCodec<ByteBuf, UpdateMachineInfos> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateMachineInfos::pos,
            ByteBufCodecs.BOOL, UpdateMachineInfos::isActive,
            ByteBufCodecs.INT, UpdateMachineInfos::overclockPercentage,
            UpdateMachineInfos::new
    );

    public UpdateMachineInfos(BlockPos pos, boolean isActive, int overclockPercentage) {
        this.pos = pos;
        this.isActive = isActive;
        this.overclockPercentage = overclockPercentage;
    }

    @NotNull
    @Override
    public CustomPacketPayload.Type<UpdateMachineInfos> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            final MachineBaseTileEntity blockEntity = (MachineBaseTileEntity) context.player().level().getBlockEntity(this.pos);
            blockEntity.isActive = isActive;
            blockEntity.overclockPercentage = overclockPercentage;
        });
    }
}
