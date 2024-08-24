package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import fr.satiscraftoryteam.satiscraftory.client.models.blocks.SmelterModel;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SmelterRenderer extends GeoBlockRenderer<SmelterBlockEntity> {
    public SmelterRenderer(BlockEntityRendererProvider.Context context) {
        super(new SmelterModel());
    }
}
