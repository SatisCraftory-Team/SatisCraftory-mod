//package fr.satiscraftoryteam.satiscraftory.common.network.packets;
//
//import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
//import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
//import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.ConveyorTileEntity;
//import fr.satiscraftoryteam.satiscraftory.common.tileentity.conveyor.IItemInputable;
//import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PacketUpdateConveyorLinker implements IPacket {
//
//    protected final int tickCounter;
//    protected final ItemStack[] itemsChain;
//    protected final BlockPos[] conveyorChainPoses;
//    protected final BlockPos outputPos;
//
//
//    public PacketUpdateConveyorLinker(int tickCounter, List<ItemStack> itemsChain, List<ConveyorTileEntity> conveyorChain, IItemInputable outputTile) {
//        this.tickCounter = tickCounter;
//        this.itemsChain = itemsChain.toArray(new ItemStack[0]);
//
//        conveyorChainPoses = new BlockPos[conveyorChain.size()];
//        for (int i = 0; i < conveyorChain.size(); i++) {
//            conveyorChainPoses[i] = conveyorChain.get(i).getBlockPos();
//        }
//
//        if(outputTile instanceof BlockEntity blockEntity){
//            outputPos = blockEntity.getBlockPos();
//        }
//        else {
//            SatisCraftory.LOGGER.warn("block at output position not found (target : {})",outputTile);
//            outputPos = new BlockPos(0,0,0);
//        }
//    }
//
//    public PacketUpdateConveyorLinker(int tickCounter, ItemStack[] itemsChain, BlockPos[] conveyorChainPoses, BlockPos outputPos){
//        this.tickCounter = tickCounter;
//        this.itemsChain = itemsChain;
//        this.conveyorChainPoses = conveyorChainPoses;
//        this.outputPos = outputPos;
//    }
//
//    @Override
//    public void handle(NetworkEvent.Context context) {
//        ClientLevel world = Minecraft.getInstance().level;
//        BlockPos supposedMasterPos = conveyorChainPoses[0];
//        //Only handle the update packet if the block is currently loaded
//        if (WorldUtils.isBlockLoaded(world, supposedMasterPos)) {
//            ConveyorTileEntity tile = WorldUtils.getTileEntity(ConveyorTileEntity.class, world, supposedMasterPos, true);
//            if (tile == null) {
//                SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but no valid tile was found.", supposedMasterPos,
//                        world.dimension().location());
//            } else {
//                tile.getLinker().handleLinkerUpdate(this);
//            }
//        }
//        else {
//            SatisCraftory.LOGGER.warn("Update tile packet received for position: {} in world: {}, but the block is not loaded.", supposedMasterPos,
//                    world.dimension().location());
//        }
//    }
//
//    @Override
//    public void encode(FriendlyByteBuf buffer) {
//        buffer.writeInt(tickCounter);
//        buffer.writeInt(itemsChain.length);
//        for (int i = 0; i < itemsChain.length; i++) {
//            ItemStack item = itemsChain[i];
//            buffer.writeItem(item);
//        }
//        buffer.writeInt(conveyorChainPoses.length);
//        for (int i = 0; i < conveyorChainPoses.length; i++) {
//            buffer.writeBlockPos(conveyorChainPoses[i]);
//        }
//        buffer.writeBlockPos(outputPos);
//    }
//
//    public static PacketUpdateConveyorLinker decode(FriendlyByteBuf buffer){
//        int tickCounter = buffer.readInt();
//
//        int itemsChainSize = buffer.readInt();
//        ItemStack[] itemsChain = new ItemStack[itemsChainSize];
//        for (int i = 0; i < itemsChainSize; i++) {
//            itemsChain[i] = buffer.readItem();
//        }
//
//        int conveyorChainPosesSize = buffer.readInt();
//        BlockPos[] conveyorChainPoses = new BlockPos[conveyorChainPosesSize];
//        for (int i = 0; i < conveyorChainPosesSize; i++) {
//            conveyorChainPoses[i] = buffer.readBlockPos();
//        }
//
//        BlockPos outputPos = buffer.readBlockPos();
//
//        return new PacketUpdateConveyorLinker(tickCounter, itemsChain, conveyorChainPoses, outputPos);
//    }
//
//    public int getTickCounter() {
//        return tickCounter;
//    }
//
//    public ArrayList<ItemStack> getItemsChain() {
//        return new ArrayList<>(List.of(this.itemsChain));
//    }
//
//    public BlockPos[] getConveyorChainPoses() {
//        return conveyorChainPoses;
//    }
//
//    public ArrayList<ConveyorTileEntity> getConveyorChainDeducedFromPosition() {
//        ArrayList<ConveyorTileEntity> conveyorChain = new ArrayList<>();
//        for (int i = 0; i < conveyorChainPoses.length; i++) {
//            conveyorChain.add(WorldUtils.getTileEntity(ConveyorTileEntity.class, Minecraft.getInstance().level, conveyorChainPoses[i], true));
//        }
//        return conveyorChain;
//    }
//
//    public IItemInputable getOutputTile() {
//        BlockEntity tile = WorldUtils.getTileEntity(Minecraft.getInstance().level, outputPos);
//        if(tile instanceof IItemInputable inputable){
//            return (IItemInputable) inputable;
//        }
//        else {
//            return null;
//        }
//    }
//}
