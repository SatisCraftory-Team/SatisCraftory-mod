package fr.satiscraftoryteam.satiscraftory.common.network.packets;

import fr.satiscraftoryteam.satiscraftory.common.network.IPacket;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorTileEntity;
import fr.satiscraftoryteam.satiscraftory.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateConveyor implements IPacket {

    private final BlockPos pos;
    private final CompoundTag updateTag;
    private final Item[] items;

    public PacketUpdateConveyor(ConveyorTileEntity tile) {
        this(tile.getBlockPos(), tile.getReducedUpdateTag(), tile.getItems());
    }

    public PacketUpdateConveyor(BlockPos pos, CompoundTag updateTag, Item[] items) {
        this.pos = pos;
        this.updateTag = updateTag;
        this.items = items;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ClientLevel world = Minecraft.getInstance().level;
        if(WorldUtils.isBlockLoaded(world,pos)){
            ConveyorTileEntity tile = WorldUtils.getTileEntity(ConveyorTileEntity.class, world, pos, true);
            if(tile != null){
                tile.handleUpdateConveyorPacket(this);
            }
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeNbt(updateTag);
        buffer.writeInt(items.length);
        for(Item item : items){
            if(item != null){
                buffer.writeItem(item.getDefaultInstance());
            }else {
                buffer.writeItem(ItemStack.EMPTY);
            }
        }
    }

    public static PacketUpdateConveyor decode(FriendlyByteBuf buffer){
        BlockPos pos = buffer.readBlockPos();
        CompoundTag updateTag = buffer.readNbt();
        int size = buffer.readInt();
        Item[] items = new Item[size];
        for(int i = 0; i < size; i++){
            ItemStack stack = buffer.readItem();
            if(stack == ItemStack.EMPTY || stack.getItem() == Items.AIR){
                items[i] = null;
            }
            else {
                items[i] = stack.getItem();
            }
        }
        return new PacketUpdateConveyor(pos, updateTag, items);
    }

    public Item[] getItems() {
        return items;
    }
}
