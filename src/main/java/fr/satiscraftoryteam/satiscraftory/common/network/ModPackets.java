package fr.satiscraftoryteam.satiscraftory.common.network;

import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketNewConveyorLinker;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateConveyorLinker;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketUpdateTile;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.*;

public class ModPackets extends AbtractPacketHandler {
    public void register() {

        registerC2SPacket(PacketUpdateTile.class, PacketUpdateTile::decode);
        registerC2SPacket(PacketUpdateConveyorLinker.class, PacketUpdateConveyorLinker::decode);
        registerC2SPacket(PacketNewConveyorLinker.class, PacketNewConveyorLinker::decode);

        registerS2CPacket(PacketGetMachineInfos.class, PacketGetMachineInfos::decode);

        registerS2CPacket(ServerboundUpdatePacketInfos.class, ServerboundUpdatePacketInfos::decode);

        registerC2SPacket(ClientboundUpdateMachineInfos.class, ClientboundUpdateMachineInfos::decode);
    }
}
