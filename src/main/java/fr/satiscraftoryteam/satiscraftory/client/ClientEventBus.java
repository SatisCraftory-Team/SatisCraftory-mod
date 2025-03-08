package fr.satiscraftoryteam.satiscraftory.client;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.renderer.blocks.*;
import fr.satiscraftoryteam.satiscraftory.client.screen.BuilderToolScreen;
import fr.satiscraftoryteam.satiscraftory.client.screen.MinerMk1Screen;
import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterScreen;
import fr.satiscraftoryteam.satiscraftory.common.init.MenuTypesInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = SatisCraftory.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientEventBus {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TileEntityInit.LOGO_ENTITY.get(), LogoRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.ELIOCUBE_ENTITY.get(), EliocubeRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.MINER_MK1_BLOCK_ENTITY.get(), MinerMk1Renderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.SMELTER_BLOCK_ENTITY.get(), SmelterRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.CONVEYOR.get(), ConveyorRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.CONVEYOR_FULL.get(), ConveyorRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypesInit.MINER_MK1_MENU.get(), MinerMk1Screen::new);
        event.register(MenuTypesInit.SMELTER_MENU.get(), SmelterScreen::new);
        event.register(MenuTypesInit.BUILDER_TOOL_MENU.get(), BuilderToolScreen::new);
    }
}
