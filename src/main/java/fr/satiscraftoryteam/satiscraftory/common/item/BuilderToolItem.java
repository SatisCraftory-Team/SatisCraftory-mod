package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.client.screen.BuilderToolMenu;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Objects;

public class BuilderToolItem extends Item {
    private Display.BlockDisplay hologram = null;
    private Block selectedBlock = BlockInit.MINER_MK1.getBlock();
    public BuilderToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!Objects.requireNonNull(context.getPlayer()).isShiftKeyDown()) {
//            context.getLevel().setBlockAndUpdate( context.getClickedPos().above(), BlockInit.MINER_MK1.getBlock().defaultBlockState());
//            return super.useOn(context);
            placeHolographicBlock(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        ItemStack stack = player.getItemInHand(pUsedHand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide)
                player.openMenu(new SimpleMenuProvider((pId, pInv, pPlayer1) -> new BuilderToolMenu(pId, pInv, null), Component.nullToEmpty("Builder Tool")));
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    public void placeHolographicBlock(UseOnContext context) {
        if (this.hologram != null) {
            this.hologram.remove(Entity.RemovalReason.KILLED);
        }
        this.hologram = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, context.getLevel());

        this.hologram.setPos(context.getClickedPos().getX(), context.getClickedPos().getY() + 0.5, context.getClickedPos().getZ());
        this.hologram.setYRot((Objects.requireNonNull(context.getPlayer()).getDirection().getClockWise().get2DDataValue() + 3) * 90);

        CompoundTag entityNbt = new CompoundTag();
        this.hologram.save(entityNbt);

        entityNbt.getCompound("block_state").putString("Name", BuiltInRegistries.BLOCK.wrapAsHolder(this.selectedBlock).getRegisteredName());
        entityNbt.getCompound("block_state").put("Properties", new CompoundTag());
        entityNbt.getCompound("block_state").getCompound("Properties").putString("facing", Objects.requireNonNull(context.getPlayer()).getDirection().getClockWise().getName().toLowerCase());
        entityNbt.put("brightness", new CompoundTag());
        entityNbt.getCompound("brightness").putInt("sky", 15);
        entityNbt.getCompound("brightness").putInt("block", 15);

        this.hologram.load(entityNbt);
        context.getLevel().addFreshEntity(this.hologram);
    }

}
