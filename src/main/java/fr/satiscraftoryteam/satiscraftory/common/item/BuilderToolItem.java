package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.client.screen.BuilderToolMenu;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class BuilderToolItem extends Item {
    public BuilderToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        pContext.getLevel().setBlockAndUpdate( pContext.getClickedPos().above(), BlockInit.MINER_MK1.getBlock().defaultBlockState());
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(!pLevel.isClientSide)
            NetworkHooks.openScreen((ServerPlayer) pPlayer, new SimpleMenuProvider((pId, pInv, pPlayer1) -> new BuilderToolMenu(pId, pInv, (FriendlyByteBuf) ContainerLevelAccess.create(pLevel, pPlayer.blockPosition())), Component.nullToEmpty("Builder Tool")));
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }


}
