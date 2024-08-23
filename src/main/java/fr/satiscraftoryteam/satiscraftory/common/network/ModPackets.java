package fr.satiscraftoryteam.satiscraftory.common.network;

import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.UpdateMachineInfos;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.UpdatePacketInfos;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.UpdateTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server.RequestMachineInfos;
import net.neoforged.bus.api.IEventBus;

public class ModPackets extends AbtractPacketHandler {

    public ModPackets(IEventBus modEventBus) {
        super(modEventBus,"1.0");
    }

    @Override
    protected void registerClientToServer(PacketRegistrar registrar) {
        registrar.play(RequestMachineInfos.TYPE, RequestMachineInfos.STREAM_CODEC);
    }

    @Override
    protected void registerServerToClient(PacketRegistrar registrar) {
        registrar.play(UpdateMachineInfos.TYPE, UpdateMachineInfos.STREAM_CODEC);
        registrar.play(UpdatePacketInfos.TYPE, UpdatePacketInfos.STREAM_CODEC);
        registrar.play(UpdateTileEntity.TYPE, UpdateTileEntity.STREAM_CODEC);

    }
}
