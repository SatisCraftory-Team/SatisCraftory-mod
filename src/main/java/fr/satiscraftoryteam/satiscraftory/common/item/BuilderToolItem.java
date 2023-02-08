package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.client.screen.BuilderToolScreen;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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
        if (!pContext.getLevel().isClientSide()) {
            NetworkHooks.openScreen(((ServerPlayer)pContext.getPlayer()), null, pContext.getClickedPos());
        }
        return super.useOn(pContext);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(!pLevel.isClientSide)
            NetworkHooks.openScreen((ServerPlayer) pPlayer, new BuilderToolScreen(stack, ), pPlayer.blockPosition());
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
    }
}
