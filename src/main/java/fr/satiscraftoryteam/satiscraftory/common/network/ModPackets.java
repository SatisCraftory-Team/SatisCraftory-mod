package fr.satiscraftoryteam.satiscraftory.common.network;

import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateConveyor;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateTile;

public class ModPackets extends AbtractPacketHandler {
    public void register() {

        registerS2CPacket(PacketUpdateTile.class, PacketUpdateTile::decode);
        registerS2CPacket(PacketUpdateConveyor.class, PacketUpdateConveyor::decode);
    }
}
