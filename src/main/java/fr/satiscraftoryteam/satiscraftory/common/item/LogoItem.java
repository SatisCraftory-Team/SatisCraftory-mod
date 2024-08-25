package fr.satiscraftoryteam.satiscraftory.common.item;

import fr.satiscraftoryteam.satiscraftory.client.renderer.items.LogoItemRenderer;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animation.AnimatableManager;

public class LogoItem extends GeoItemAnimable<LogoItemRenderer> {

    public LogoItem(Block block, Properties properties) {
        super(block, properties, new LogoItemRenderer(), "spin");
    }
}
