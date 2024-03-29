package fr.satiscraftoryteam.satiscraftory.client;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.renderer.blocks.EliocubeRenderer;
import fr.satiscraftoryteam.satiscraftory.client.renderer.blocks.LogoRenderer;
import fr.satiscraftoryteam.satiscraftory.client.renderer.blocks.MinerMk1Renderer;
import fr.satiscraftoryteam.satiscraftory.client.renderer.blocks.SmelterRenderer;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SatisCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ClientEventBus {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TileEntityInit.LOGO_ENTITY.get(), LogoRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.ELIOCUBE_ENTITY.get(), EliocubeRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.MINER_MK1_BLOCK_ENTITY.get(), MinerMk1Renderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.SMELTER_BLOCK_ENTITY.get(), SmelterRenderer::new);
    }

}
