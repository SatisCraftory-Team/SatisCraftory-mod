package fr.satiscraftoryteam.satiscraftory.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public abstract class AbtractPacketHandler {

    protected AbtractPacketHandler(IEventBus modEventBus, String version) {
        modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
            PayloadRegistrar registrar = event.registrar(version);
            registerClientToServer(new PacketRegistrar(registrar, true));
            registerServerToClient(new PacketRegistrar(registrar, false));
        });
    }

    protected abstract void registerClientToServer(PacketRegistrar registrar);

    protected abstract void registerServerToClient(PacketRegistrar registrar);

    protected record SimplePacketPayLoad(CustomPacketPayload.Type<CustomPacketPayload> type) implements CustomPacketPayload {

        private SimplePacketPayLoad(ResourceLocation id) {
            this(new CustomPacketPayload.Type<>(id));
        }
    }

    protected record PacketRegistrar(PayloadRegistrar registrar, boolean toServer) {

        public <MSG extends IPacket> void configuration(CustomPacketPayload.Type<MSG> type, StreamCodec<? super FriendlyByteBuf, MSG> reader) {
            if (toServer) {
                registrar.configurationToServer(type, reader, IPacket::handle);
            } else {
                registrar.configurationToClient(type, reader, IPacket::handle);
            }
        }

        public <MSG extends IPacket> void play(CustomPacketPayload.Type<MSG> type, StreamCodec<? super RegistryFriendlyByteBuf, MSG> reader) {
            if (toServer) {
                registrar.playToServer(type, reader, IPacket::handle);
            } else {
                registrar.playToClient(type, reader, IPacket::handle);
            }
        }

        public SimplePacketPayLoad playInstanced(ResourceLocation id, IPayloadHandler<CustomPacketPayload> handler) {
            SimplePacketPayLoad payload = new SimplePacketPayLoad(id);
            if (toServer) {
                registrar.playToServer(payload.type(), StreamCodec.unit(payload), handler);
            } else {
                registrar.playToClient(payload.type(), StreamCodec.unit(payload), handler);
            }
            return payload;
        }
    }
}
