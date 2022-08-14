package fr.satiscraftoryteam.satiscraftory.common.network;

import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateConveyor;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateTile;

public class ModPackets extends AbtractPacketHandler {
    public void register() {

        registerC2SPacket(PacketUpdateTile.class, PacketUpdateTile::decode);
        registerC2SPacket(PacketUpdateConveyor.class, PacketUpdateConveyor::decode);
    }
}
