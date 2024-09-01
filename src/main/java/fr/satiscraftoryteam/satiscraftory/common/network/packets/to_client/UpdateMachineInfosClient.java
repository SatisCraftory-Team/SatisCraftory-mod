package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.ClientAccessor;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateMachineInfosClient(BlockPos pos, boolean isActive, int overclockPercentage) implements IPacket {
    public static final CustomPacketPayload.Type<UpdateMachineInfosClient> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("update_machine_infos"));
    public static final StreamCodec<ByteBuf, UpdateMachineInfosClient> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UpdateMachineInfosClient::pos,
            ByteBufCodecs.BOOL, UpdateMachineInfosClient::isActive,
            ByteBufCodecs.INT, UpdateMachineInfosClient::overclockPercentage,
            UpdateMachineInfosClient::new
    );

    @NotNull
    @Override
    public CustomPacketPayload.Type<UpdateMachineInfosClient> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        context.enqueueWork(() ->{
            MachineBaseTileEntity blockEntity = (MachineBaseTileEntity) context.player().level().getBlockEntity(this.pos);
            blockEntity.updateMachineInfos(this.overclockPercentage);
            blockEntity.setActive(this.isActive);
            ClientAccessor.updateMachineScreen(this.isActive, this.overclockPercentage);
        });
    }
}
