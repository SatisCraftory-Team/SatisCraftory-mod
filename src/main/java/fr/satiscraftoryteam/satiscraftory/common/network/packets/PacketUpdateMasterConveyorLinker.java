package fr.satiscraftoryteam.satiscraftory.common.network.packets;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.renderer.blocks.ConveyorRenderer;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TileEntityUpdatable;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.IItemInputable;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.MasterConveyorLinker;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class PacketUpdateMasterConveyorLinker implements IPacket {

    private final int tickCounter;
    private final ItemStack[] itemsChain;
    private final BlockPos[] conveyorChainPoses;
    private final BlockPos outputPos;
    private final BlockPos conveyorTickerPos;


    public PacketUpdateMasterConveyorLinker(int tickCounter, ArrayList<ItemStack> itemsChain, ArrayList<ConveyorTileEntity> conveyorChain, IItemInputable outputTile, ConveyorTileEntity conveyorTicker) {
        this.tickCounter = tickCounter;
        this.itemsChain = itemsChain.toArray(new ItemStack[0]);

        conveyorChainPoses = new BlockPos[conveyorChain.size()];
        for (int i = 0; i < conveyorChain.size(); i++) {
            conveyorChainPoses[i] = conveyorChain.get(i).getTilePos();
        }

        if(outputTile instanceof BlockEntity blockEntity){
            outputPos = blockEntity.getBlockPos();
        }
        else {
            SatisCraftory.LOGGER.warn("block at output position not found (target : {})",outputTile);
            outputPos = new BlockPos(0,0,0);
        }

        conveyorTickerPos = conveyorTicker.getTilePos();
    }

    public PacketUpdateMasterConveyorLinker(int tickCounter,ItemStack[] itemsChain, BlockPos[] conveyorChainPoses, BlockPos outputPos, BlockPos conveyorTickerPos){
        this.tickCounter = tickCounter;
        this.itemsChain = itemsChain;
        this.conveyorChainPoses = conveyorChainPoses;
        this.outputPos = outputPos;
        this.conveyorTickerPos = conveyorTickerPos;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ClientLevel world = Minecraft.getInstance().level;
        //Only handle the update packet if the block is currently loaded
        if (WorldUtils.isBlockLoaded(world, conveyorTickerPos)) {
            ConveyorTileEntity tile = WorldUtils.getTileEntity(ConveyorTileEntity.class, world, conveyorTickerPos, true);
            if (tile == null) {
                SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but no valid tile was found.", conveyorTickerPos,
                        world.dimension().location());
            } else {
                tile.handleUpdateMasterConveyorLinkerPacket(this);
            }
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(tickCounter);
        buffer.writeInt(itemsChain.length);
        for (int i = 0; i < itemsChain.length; i++) {
            ItemStack item = itemsChain[i];
            if (item == null) {
                buffer.writeItem(ItemStack.EMPTY);
            } else {
                buffer.writeItem(item);
            }
        }
        buffer.writeInt(conveyorChainPoses.length);
        for (int i = 0; i < conveyorChainPoses.length; i++) {
            buffer.writeBlockPos(conveyorChainPoses[i]);
        }
        buffer.writeBlockPos(outputPos);
        buffer.writeBlockPos(conveyorTickerPos);
    }

    public static PacketUpdateMasterConveyorLinker decode(FriendlyByteBuf buffer){
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
        BlockPos conveyorTickerPos = buffer.readBlockPos();

        return new PacketUpdateMasterConveyorLinker(tickCounter, itemsChain, conveyorChainPoses, outputPos, conveyorTickerPos);
    }

    public int getTickCounter() {
        return tickCounter;
    }

    public ArrayList<ItemStack> getItemsChain() {
        return new ArrayList<>(List.of(this.itemsChain));
    }

    public ArrayList<ConveyorTileEntity> getConveyorChain() {
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
            SatisCraftory.LOGGER.warn("block at output position not found (target : {})",tile);
            return null;
        }
    }
}
