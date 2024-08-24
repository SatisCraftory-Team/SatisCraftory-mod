package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import fr.satiscraftoryteam.satiscraftory.client.models.blocks.EliocubeModel;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.EliocubeBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class EliocubeRenderer extends GeoBlockRenderer<EliocubeBlockEntity> {
    public EliocubeRenderer(BlockEntityRendererProvider.Context context) {
        super(new EliocubeModel());
    }
}
