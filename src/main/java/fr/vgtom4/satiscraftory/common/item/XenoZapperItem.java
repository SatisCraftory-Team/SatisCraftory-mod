package fr.vgtom4.satiscraftory.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class XenoZapperItem extends Item {
    public XenoZapperItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack item, LivingEntity attackedEntity, LivingEntity attackingEntity) {
        if (attackingEntity instanceof ServerPlayer player) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(player.level);
            lightningBolt.setPos(attackedEntity.getX(), attackedEntity.getY(), attackedEntity.getZ());
            lightningBolt.setVisualOnly(true);
            player.level.addFreshEntity(lightningBolt);
        }
        return super.hurtEnemy(item, attackedEntity, attackingEntity);
    }
}
