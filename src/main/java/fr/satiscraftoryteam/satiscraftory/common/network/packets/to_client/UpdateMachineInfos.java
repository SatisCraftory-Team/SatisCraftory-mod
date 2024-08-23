package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.ClientAccessor;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateMachineInfos(boolean isActive, int overclockPercentage) implements IPacket {
    public static final CustomPacketPayload.Type<UpdateMachineInfos> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("update_machine_infos"));
    public static final StreamCodec<ByteBuf, UpdateMachineInfos> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateMachineInfos::isActive,
            ByteBufCodecs.INT, UpdateMachineInfos::overclockPercentage,
            UpdateMachineInfos::new
    );

    public UpdateMachineInfos(boolean isActive, int overclockPercentage) {
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
        context.enqueueWork(() -> ClientAccessor.updateMachineScreen(this.isActive, this.overclockPercentage));
    }
}
