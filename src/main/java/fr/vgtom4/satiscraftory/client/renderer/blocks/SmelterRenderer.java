package fr.vgtom4.satiscraftory.client.renderer.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.vgtom4.satiscraftory.client.models.blocks.MinerMk1Model;
import fr.vgtom4.satiscraftory.client.models.blocks.SmelterModel;
import fr.vgtom4.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import fr.vgtom4.satiscraftory.common.tileentity.SmelterBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SmelterRenderer extends GeoBlockRenderer<SmelterBlockEntity> {
    public SmelterRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new SmelterModel());
    }

    @Override
    public RenderType getRenderType(SmelterBlockEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void renderEarly(SmelterBlockEntity animatable, PoseStack stackIn, float partialTicks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        stackIn.translate(0,-0.01f,0);
    }
}
