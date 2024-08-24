package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import fr.satiscraftoryteam.satiscraftory.client.models.blocks.MinerMk1Model;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MinerMk1Renderer extends GeoBlockRenderer<MinerMk1BlockEntity> {
    public MinerMk1Renderer(BlockEntityRendererProvider.Context context) {
        super(new MinerMk1Model());
    }

}
