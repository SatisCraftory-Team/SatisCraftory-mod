package fr.satiscraftoryteam.satiscraftory.common.network.packets;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class PacketGetMachineInfos implements IPacket {

    public final BlockPos pos;

    public PacketGetMachineInfos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            final MachineBaseTileEntity blockEntity = (MachineBaseTileEntity) context.getSender().level.getBlockEntity(this.pos);
             SatisCraftory.packetHandler.getChannel().send(PacketDistributor.ALL.noArg(),
                            new ClientboundUpdateMachineInfos(blockEntity.isActive, blockEntity.overclockPercentage));
        });
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
    }

    public static PacketGetMachineInfos decode(FriendlyByteBuf buffer) {
        return new PacketGetMachineInfos(buffer.readBlockPos());
    }
}
