package fr.satiscraftoryteam.satiscraftory.common.network;

import fr.satiscraftoryteam.satiscraftory.common.network.packets.*;

public class ModPackets extends AbtractPacketHandler {
    public void register() {

        registerC2SPacket(PacketUpdateTile.class, PacketUpdateTile::decode);
        registerC2SPacket(PacketUpdateConveyor.class, PacketUpdateConveyor::decode);

        registerS2CPacket(PacketGetMachineInfos.class, PacketGetMachineInfos::decode);

        registerS2CPacket(ServerboundUpdatePacketInfos.class, ServerboundUpdatePacketInfos::decode);

        registerC2SPacket(ClientboundUpdateMachineInfos.class, ClientboundUpdateMachineInfos::decode);

    }
}
