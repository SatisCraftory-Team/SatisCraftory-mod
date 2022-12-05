package fr.satiscraftoryteam.satiscraftory.common.network.packets;

import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundUpdatePacketInfos implements IPacket {

    public BlockPos pos;
    public boolean isActive;
    public int overclockPercentage;

    public ServerboundUpdatePacketInfos(BlockPos pos, boolean isActive, int overclockPercentage) {
        this.pos = pos;
        this.isActive = isActive;
        this.overclockPercentage = overclockPercentage;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            final MachineBaseTileEntity blockEntity = (MachineBaseTileEntity) context.getSender().level.getBlockEntity(this.pos);
            blockEntity.isActive = isActive;
            blockEntity.overclockPercentage = overclockPercentage;
        });
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(isActive);
        buffer.writeInt(overclockPercentage);
    }

    public static ServerboundUpdatePacketInfos decode(FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        boolean isActive = buffer.readBoolean();
        int overclockPercentage = buffer.readInt();
        return new ServerboundUpdatePacketInfos(pos, isActive, overclockPercentage);
    }
}
