//package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client;
//
//import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
//import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
//import fr.satiscraftoryteam.satiscraftory.common.network.stream_codecs.SatisByteBufCodecs;
//import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorTileEntity;
//import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
//import io.netty.buffer.ByteBuf;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.world.item.Item;
//import net.neoforged.neoforge.network.handling.IPayloadContext;
//import org.jetbrains.annotations.NotNull;
//
//public record UpdateConveyor(BlockPos pos, CompoundTag updateTag, Item[] items) implements IPacket {
//    public static final CustomPacketPayload.Type<UpdateConveyor> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("update_conveyor"));
//    public static final StreamCodec<ByteBuf, UpdateConveyor> STREAM_CODEC = StreamCodec.composite(
//            BlockPos.STREAM_CODEC, UpdateConveyor::pos,
//            ByteBufCodecs.TRUSTED_COMPOUND_TAG, UpdateConveyor::updateTag,
//            SatisByteBufCodecs.ITEMS_ARRAY, UpdateConveyor::items,
//            UpdateConveyor::new
//    );
//
//    public UpdateConveyor(ConveyorTileEntity tile) {
//        this(tile.getBlockPos(), tile.getReducedUpdateTag(tile.getLevel().registryAccess()), tile.getItems());
//    }
//
//    public UpdateConveyor(BlockPos pos, CompoundTag updateTag, Item[] items) {
//        this.pos = pos;
//        this.updateTag = updateTag;
//        this.items = items;
//    }
//
//    @NotNull
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
//
//    @Override
//    public void handle(IPayloadContext context) {
//        ClientLevel world = Minecraft.getInstance().level;
//        if(WorldUtils.isBlockLoaded(world,pos)){
//            ConveyorTileEntity tile = WorldUtils.getTileEntity(ConveyorTileEntity.class, world, pos, true);
//            if(tile != null){
//                tile.handleUpdateConveyorPacket(this);
//            }
//        }
//    }
//
//
//    public Item[] getItems() {
//        return items;
//    }
//}
