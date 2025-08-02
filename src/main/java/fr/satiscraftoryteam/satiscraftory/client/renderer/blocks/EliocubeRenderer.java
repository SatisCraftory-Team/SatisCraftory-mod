package fr.satiscraftoryteam.satiscraftory.client.renderer.blocks;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.models.blocks.EliocubeBlockModel;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.EliocubeBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class EliocubeRenderer extends GeoBlockRenderer<EliocubeBlockEntity> {
    public EliocubeRenderer(BlockEntityRendererProvider.Context context) {
//        super(new DefaultedBlockGeoModel<>(ResourceLocation.fromNamespaceAndPath(
//                SatisCraftory.MODID, "test/eliocube"
//        )));
        super(new EliocubeBlockModel<>(ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "test/eliocube"))
//                .withAltTexture(ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "eliocube"))
//                .withAltModel(ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "eliocube"))
        );
    }
}
