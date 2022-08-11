package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import fr.satiscraftoryteam.satiscraftory.common.block.buildings.logistics.conveyors.ConveyorBlock;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.ConveyorTileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.Item;
public class ConveyorRenderer implements BlockEntityRenderer<ConveyorTileEntity> {
    static {
        BlockEntityRenderers.register(TileEntityInit.CONVEYOR.get(), ConveyorRenderer::new);
        BlockEntityRenderers.register(TileEntityInit.CONVEYOR_FULL.get(), ConveyorRenderer::new);
    }

    public ItemRenderer itemRenderer;
    public BlockRenderDispatcher dispatcher;

    public ConveyorRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
        dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(ConveyorTileEntity conveyorTileEntity, float partialTick, PoseStack pose, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        Item[] items = conveyorTileEntity.getItems();
        float progress = conveyorTileEntity.getProgress();
        Quaternion rotation = conveyorTileEntity.getBlockState().getValue(ConveyorBlock.FACING).getRotation();
        pose.pushPose();
        pose.translate(0.5f, 0.5f, 0.5f);
        for(int i = 0; i < items.length; i++) {
            Item item = items[i];
            if(item != null) {
                pose.pushPose();
                pose.mulPose(rotation);
                pose.translate(0, (i+progress) * (1f/ ConveyorTileEntity.itemPerConveyor) - 0.5f, -0.2f);
                itemRenderer.renderStatic(item.getDefaultInstance(), ItemTransforms.TransformType.FIXED, combinedOverlay, packedLight, pose, buffer,(int) conveyorTileEntity.getBlockPos().asLong() + i);
                pose.popPose();
            }
        }
        pose.popPose();
    }
}
