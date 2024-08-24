package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import fr.satiscraftoryteam.satiscraftory.client.models.blocks.MinerMk1Model;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MinerMk1Renderer extends GeoBlockRenderer<MinerMk1BlockEntity> {
    public MinerMk1Renderer(BlockEntityRendererProvider.Context context) {
        super(new MinerMk1Model());
    }
//    public MinerMk1Renderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
//        super(rendererDispatcherIn, new MinerMk1Model());
//    }

//    @Override
//    public RenderType getRenderType(MinerMk1BlockEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
//        return RenderType.entityTranslucent(getTextureLocation(animatable));
//    }
//
//    @Override
//    public void renderEarly(MinerMk1BlockEntity animatable, PoseStack stackIn, float partialTicks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
//        stackIn.translate(0,-0.01f,0);
//    }
}
