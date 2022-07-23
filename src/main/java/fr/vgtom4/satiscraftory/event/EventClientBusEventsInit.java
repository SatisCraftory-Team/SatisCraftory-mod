package fr.vgtom4.satiscraftory.event;

import fr.vgtom4.satiscraftory.SatisCraftory;
import fr.vgtom4.satiscraftory.block.entity.client.EliocubeRenderer;
import fr.vgtom4.satiscraftory.block.entity.client.MinerMk1Renderer;
import fr.vgtom4.satiscraftory.init.BlockEntityInit;
import fr.vgtom4.satiscraftory.init.BlockInit;
import fr.vgtom4.satiscraftory.init.MenuTypesInit;
import fr.vgtom4.satiscraftory.screen.MinerMk1Screen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SatisCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventClientBusEventsInit {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.ELIOCUBE_ENTITY.get(), EliocubeRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.MINER_MK1_BLOCK_ENTITY.get(), MinerMk1Renderer::new);
    }

    /*
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(MenuTypesInit.MINER_MK1_MENU.get(), MinerMk1Screen::new);
    }*/
}
