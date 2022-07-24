package fr.vgtom4.satiscraftory.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

public class XenoZapperItem extends Item {
    public XenoZapperItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean hurtEnemy(ItemStack p_41395_, LivingEntity p_41396_, LivingEntity p_41397_) {

        return super.hurtEnemy(p_41395_, p_41396_, p_41397_);
    }
}
