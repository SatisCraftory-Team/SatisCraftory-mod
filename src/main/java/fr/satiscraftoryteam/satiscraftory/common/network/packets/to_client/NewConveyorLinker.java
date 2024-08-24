package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.stream_codecs.SatisByteBufCodecs;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.IItemInputable;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewConveyorLinker extends UpdateConveyorLinker {
    public static final CustomPacketPayload.Type<NewConveyorLinker> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("new_conveyor_linker"));

    public BlockPos previousMasterPos;

    public static final StreamCodec<ByteBuf, NewConveyorLinker> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, NewConveyorLinker::getTickCounter,
            SatisByteBufCodecs.ITEMS_ARRAY, NewConveyorLinker::getItemsChain,
            SatisByteBufCodecs.BLOCK_POS_ARRAY, NewConveyorLinker::getConveyorChainPoses,
            BlockPos.STREAM_CODEC, NewConveyorLinker::getOutputPos,
            BlockPos.STREAM_CODEC, NewConveyorLinker::getPreviousMasterPos,
            NewConveyorLinker::new
    );

    public NewConveyorLinker(int tickCounter, List<ItemStack> itemsChain, List<ConveyorTileEntity> conveyorChain, IItemInputable outputTile, ConveyorTileEntity previousMaster) {
        super(tickCounter, itemsChain, conveyorChain, outputTile);
        this.previousMasterPos = previousMaster.getBlockPos();
    }

    public NewConveyorLinker(int tickCounter, ItemStack[] itemsChain, BlockPos[] conveyorChainPoses, BlockPos outputPos, BlockPos previousMasterPos) {
        super(tickCounter, itemsChain, conveyorChainPoses, outputPos);
        this.previousMasterPos = previousMasterPos;
    }

    @NotNull
    @Override
    public CustomPacketPayload.Type<? extends NewConveyorLinker> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        ClientLevel world = Minecraft.getInstance().level;
        //Only handle the update packet if the block is currently loaded
        if (WorldUtils.isBlockLoaded(world, previousMasterPos)) {
            ConveyorTileEntity tile = WorldUtils.getTileEntity(ConveyorTileEntity.class, world, previousMasterPos, true);
            if (tile == null) {
                SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but no valid tile was found.", previousMasterPos,
                        world.dimension().location());
            } else {
                tile.getLinker().handleSplitLinkerUpdate(this);
            }
        }
        else {
            SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but the block is not loaded.", previousMasterPos,
                    world.dimension().location());
        }
    }

    public BlockPos getPreviousMasterPos() {
        return previousMasterPos;
    }
}
