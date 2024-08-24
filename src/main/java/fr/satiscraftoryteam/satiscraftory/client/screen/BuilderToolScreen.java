package fr.satiscraftoryteam.satiscraftory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BuilderToolScreen extends ManagementMachineGui<BuilderToolMenu> {
    private static final ResourceLocation GUI = SatisCraftory.rl("textures/gui/miner_mk1_gui.png");


    public BuilderToolScreen(BuilderToolMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int pMouseX, int pMouseY) {
        super.renderBg(graphics, partialTick, pMouseX, pMouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // GUIs are machine specific, so keep it here
        graphics.blit(GUI, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    double getPowerUsage() {
        return 0;
    }

    @Override
    double getSpeed() {
        return 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
