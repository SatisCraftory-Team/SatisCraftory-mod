package fr.vgtom4.satiscraftory.client;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.client.renderer.blocks.EliocubeRenderer;
import fr.vgtom4.satiscraftory.client.renderer.blocks.MinerMk1Renderer;
import fr.vgtom4.satiscraftory.common.init.TileEntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SatisCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ClientEventBus {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TileEntityInit.ELIOCUBE_ENTITY.get(), EliocubeRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.MINER_MK1_BLOCK_ENTITY.get(), MinerMk1Renderer::new);
    }

}
