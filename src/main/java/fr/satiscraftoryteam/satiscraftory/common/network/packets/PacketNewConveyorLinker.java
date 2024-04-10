package fr.satiscraftoryteam.satiscraftory.common.network.packets;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.IItemInputable;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;

public class PacketNewConveyorLinker extends PacketUpdateConveyorLinker{
    protected final BlockPos previousMasterPos;

    public PacketNewConveyorLinker(int tickCounter, List<ItemStack> itemsChain, List<ConveyorTileEntity> conveyorChain, IItemInputable outputTile, ConveyorTileEntity previousMaster) {
        super(tickCounter, itemsChain, conveyorChain, outputTile);
        this.previousMasterPos = previousMaster.getBlockPos();
    }

    public PacketNewConveyorLinker(int tickCounter, ItemStack[] itemsChain, BlockPos[] conveyorChainPoses, BlockPos outputPos, BlockPos previousMasterPos) {
        super(tickCounter, itemsChain, conveyorChainPoses, outputPos);
        this.previousMasterPos = previousMasterPos;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(tickCounter);
        buffer.writeInt(itemsChain.length);
        for (int i = 0; i < itemsChain.length; i++) {
            ItemStack item = itemsChain[i];
            buffer.writeItem(item);
        }
        buffer.writeInt(conveyorChainPoses.length);
        for (int i = 0; i < conveyorChainPoses.length; i++) {
            buffer.writeBlockPos(conveyorChainPoses[i]);
        }
        buffer.writeBlockPos(outputPos);
        buffer.writeBlockPos(previousMasterPos);
    }

    public static PacketNewConveyorLinker decode(FriendlyByteBuf buffer){
        int tickCounter = buffer.readInt();

        int itemsChainSize = buffer.readInt();
        ItemStack[] itemsChain = new ItemStack[itemsChainSize];
        for (int i = 0; i < itemsChainSize; i++) {
            itemsChain[i] = buffer.readItem();
        }

        int conveyorChainPosesSize = buffer.readInt();
        BlockPos[] conveyorChainPoses = new BlockPos[conveyorChainPosesSize];
        for (int i = 0; i < conveyorChainPosesSize; i++) {
            conveyorChainPoses[i] = buffer.readBlockPos();
        }

        BlockPos outputPos = buffer.readBlockPos();
        BlockPos previousMasterPos = buffer.readBlockPos();

        return new PacketNewConveyorLinker(tickCounter, itemsChain, conveyorChainPoses, outputPos, previousMasterPos);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
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
}
