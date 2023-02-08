package fr.satiscraftoryteam.satiscraftory.common.network.packets;

import fr.satiscraftoryteam.satiscraftory.client.ClientAccessor;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundUpdateMachineInfos implements IPacket {
    public boolean isActive;
    public int overclockPercentage;

    public ClientboundUpdateMachineInfos(boolean isActive, int overclockPercentage) {
        this.isActive = isActive;
        this.overclockPercentage = overclockPercentage;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(
                () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    ClientAccessor.updateMachineScreen(this.isActive, this.overclockPercentage);
                }));
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.isActive);
        buffer.writeInt(overclockPercentage);
    }

    public static ClientboundUpdateMachineInfos decode(FriendlyByteBuf buffer) {
        boolean isActive = buffer.readBoolean();
        int overclockPercentage = buffer.readInt();
        return new ClientboundUpdateMachineInfos(isActive, overclockPercentage);
    }
}
