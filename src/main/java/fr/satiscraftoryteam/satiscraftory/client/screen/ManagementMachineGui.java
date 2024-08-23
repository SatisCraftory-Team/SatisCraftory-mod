package fr.satiscraftoryteam.satiscraftory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.element.CheckBox;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

public abstract class ManagementMachineGui<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected ExtendedSlider sliderOverclockInner;
    protected CheckBox checkBoxOnOff;
    public int overclockPercentage = 100;

    private static final ResourceLocation INVENTORY =
            ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/gui/inventory.png");
    private static final ResourceLocation CONFIG_BAR =
            ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/gui/config_bar.png");

    public ManagementMachineGui(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float PartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(CONFIG_BAR, x-9, y+10, 0, 0, 256, 60);
        graphics.blit(CONFIG_BAR, x+imageWidth+3, y+40, 0, 61, 62, 86);
        graphics.blit(CONFIG_BAR, x-100, y+10, 0, 0, 100, 60);

        graphics.blit(INVENTORY, x, y + 100, 0, 0, 176, 100);
    }

    public void updateMachineInfos(boolean isActive, int overclockPercentage) {
        this.checkBoxOnOff.setToggled(isActive);

        this.overclockPercentage = overclockPercentage;
        this.sliderOverclockInner.setValue(overclockPercentage);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Labels are machine specific, so keep it here
//        GuiGraphics.drawString(poseStack, this.font, "⚡ " + String.valueOf(getPowerUsage()) + " MW", -90, 26, 0xff8c00);
//        GuiGraphics.drawString(poseStack, this.font, "⌛ " + String.valueOf(getSpeed()) + " items/min", -90, 48, 0xff8c00);
    }

    abstract double getPowerUsage();
    abstract double getSpeed();
}
