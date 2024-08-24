package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import fr.satiscraftoryteam.satiscraftory.client.models.blocks.SmelterModel;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SmelterRenderer extends GeoBlockRenderer<SmelterBlockEntity> {
    public SmelterRenderer(BlockEntityRendererProvider.Context context) {
        super(new SmelterModel());
    }
//    public SmelterRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
//        super(rendererDispatcherIn, new SmelterModel());
//    }
//
//    @Override
//    public RenderType getRenderType(SmelterBlockEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
//        return RenderType.entityTranslucent(getTextureLocation(animatable));
//    }
//
//    @Override
//    public void renderEarly(SmelterBlockEntity animatable, PoseStack stackIn, float partialTicks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
//        stackIn.translate(0,-0.01f,0);
//    }
}
