package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import fr.satiscraftoryteam.satiscraftory.client.models.blocks.MinerMk1Model;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MinerMk1Renderer extends GeoBlockRenderer<MinerMk1BlockEntity> {
    public MinerMk1Renderer(BlockEntityRendererProvider.Context context) {
        super(new MinerMk1Model());
    }

    @Override
    public AABB getRenderBoundingBox(MinerMk1BlockEntity blockEntity) {
        return blockEntity.getDynamicRenderBoundingBox();
    }
}
