package fr.satiscraftoryteam.satiscraftory.common.network.packets.to_client;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UpdateConveyorLinker implements IPacket {
    public static final CustomPacketPayload.Type<UpdateConveyorLinker> TYPE = new CustomPacketPayload.Type<>(SatisCraftory.rl("update_conveyor_linker"));

    private int tickCounter;
    private ItemStack[] itemsChain;
    private BlockPos[] conveyorChainPoses;
    private BlockPos outputPos;

    public static final StreamCodec<ByteBuf, UpdateConveyorLinker> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateConveyorLinker::getTickCounter,
            SatisByteBufCodecs.ITEMS_ARRAY, UpdateConveyorLinker::getItemsChain,
            SatisByteBufCodecs.BLOCK_POS_ARRAY, UpdateConveyorLinker::getConveyorChainPoses,
            BlockPos.STREAM_CODEC, UpdateConveyorLinker::getOutputPos,
            UpdateConveyorLinker::new
    );

    public UpdateConveyorLinker(int tickCounter, List<ItemStack> itemsChain, List<ConveyorTileEntity> conveyorChain, IItemInputable outputTile) {
        this.tickCounter = tickCounter;
        this.itemsChain = itemsChain.toArray(new ItemStack[0]);

        conveyorChainPoses = new BlockPos[conveyorChain.size()];
        for (int i = 0; i < conveyorChain.size(); i++) {
            conveyorChainPoses[i] = conveyorChain.get(i).getBlockPos();
        }

        if(outputTile instanceof BlockEntity blockEntity){
            outputPos = blockEntity.getBlockPos();
        }
        else {
            SatisCraftory.LOGGER.warn("block at output position not found (target : {})",outputTile);
            outputPos = new BlockPos(0,0,0);
        }
    }

    public UpdateConveyorLinker(int tickCounter, ItemStack[] itemsChain, BlockPos[] conveyorChainPoses, BlockPos outputPos) {
        this.tickCounter = tickCounter;
        this.itemsChain = itemsChain;
        this.conveyorChainPoses = conveyorChainPoses;
        this.outputPos = outputPos;
    }

    @NotNull
    @Override
    public CustomPacketPayload.Type<? extends UpdateConveyorLinker> type() {
        return TYPE;
    }

    @Override
    public void handle(IPayloadContext context) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockPos supposedMasterPos = conveyorChainPoses[0];
        //Only handle the update packet if the block is currently loaded
        if (WorldUtils.isBlockLoaded(world, supposedMasterPos)) {
            ConveyorTileEntity tile = WorldUtils.getTileEntity(ConveyorTileEntity.class, world, supposedMasterPos, true);
            if (tile == null) {
                SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but no valid tile was found.", supposedMasterPos,
                        world.dimension().location());
            } else {
                tile.getLinker().handleLinkerUpdate(this);
            }
        }
        else {
            SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but the block is not loaded.", supposedMasterPos,
                    world.dimension().location());
        }
    }

    public int getTickCounter() {
        return tickCounter;
    }

    public ItemStack[] getItemsChain() {
        return itemsChain;
    }

    public BlockPos[] getConveyorChainPoses() {
        return conveyorChainPoses;
    }

    public BlockPos getOutputPos() {
        return outputPos;
    }

    public ArrayList<ItemStack> getItemsChainList() {
        return new ArrayList<>(List.of(this.itemsChain));
    }

    public ArrayList<ConveyorTileEntity> getConveyorChainDeducedFromPosition() {
        ArrayList<ConveyorTileEntity> conveyorChain = new ArrayList<>();
        for (int i = 0; i < conveyorChainPoses.length; i++) {
            conveyorChain.add(WorldUtils.getTileEntity(ConveyorTileEntity.class, Minecraft.getInstance().level, conveyorChainPoses[i], true));
        }
        return conveyorChain;
    }

    public IItemInputable getOutputTile() {
        BlockEntity tile = WorldUtils.getTileEntity(Minecraft.getInstance().level, outputPos);
        if(tile instanceof IItemInputable inputable){
            return (IItemInputable) inputable;
        }
        else {
            return null;
        }
    }
}
