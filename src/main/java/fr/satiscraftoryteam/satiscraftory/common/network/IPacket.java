package fr.satiscraftoryteam.satiscraftory.common.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IPacket extends CustomPacketPayload {
    void handle(IPayloadContext ctx);
}
