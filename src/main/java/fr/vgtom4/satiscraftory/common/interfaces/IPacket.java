package fr.vgtom4.satiscraftory.common.interfaces;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket {
    void handle(NetworkEvent.Context context);

    void encode(FriendlyByteBuf buffer);

    static <PACKET extends IPacket> void handle(PACKET message, Supplier<NetworkEvent.Context> ctx) {
        if (message != null) {
            //Message should never be null unless something went horribly wrong decoding.
            // In which case we don't want to try enqueuing handling it, or set the packet as handled
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> message.handle(context));
            context.setPacketHandled(true);
        }
    }
}
