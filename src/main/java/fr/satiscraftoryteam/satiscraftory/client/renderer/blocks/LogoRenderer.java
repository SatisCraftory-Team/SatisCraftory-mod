package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import fr.satiscraftoryteam.satiscraftory.client.models.blocks.LogoModel;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.LogoBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LogoRenderer extends GeoBlockRenderer<LogoBlockEntity> {
    public LogoRenderer(BlockEntityRendererProvider.Context context) {
        super(new LogoModel());
    }
//    public LogoRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
//        super(new LogoModel());
//    }

//    //@Override
//    public RenderType getRenderType(LogoBlockEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
//        return RenderType.entityTranslucent(getTextureLocation(animatable));
//    }
}
