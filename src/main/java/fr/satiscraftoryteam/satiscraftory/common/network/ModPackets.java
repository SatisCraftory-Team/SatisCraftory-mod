package fr.satiscraftoryteam.satiscraftory.common.network;

import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.*;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server.RequestMachineInfos;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server.UpdateMachineInfos;
import net.neoforged.bus.api.IEventBus;

public class ModPackets extends AbtractPacketHandler {

    public ModPackets(IEventBus modEventBus) {
        super(modEventBus,"1.0");
    }

    @Override
    protected void registerClientToServer(PacketRegistrar registrar) {
        registrar.play(RequestMachineInfos.TYPE, RequestMachineInfos.STREAM_CODEC);
        registrar.play(UpdateMachineInfos.TYPE, UpdateMachineInfos.STREAM_CODEC);
    }

    @Override
    protected void registerServerToClient(PacketRegistrar registrar) {
        registrar.play(fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.UpdateMachineInfos.TYPE, fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client.UpdateMachineInfos.STREAM_CODEC);
        registrar.play(UpdateTileEntity.TYPE, UpdateTileEntity.STREAM_CODEC);
        registrar.play(UpdateConveyorLinker.TYPE, UpdateConveyorLinker.STREAM_CODEC);
        registrar.play(NewConveyorLinker.TYPE, NewConveyorLinker.STREAM_CODEC);
    }
}
