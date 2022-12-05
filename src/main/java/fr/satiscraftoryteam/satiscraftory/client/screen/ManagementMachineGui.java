package fr.satiscraftoryteam.satiscraftory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.element.CheckBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public abstract class ManagementMachineGui<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected ForgeSlider sliderOverclockInner;
    protected CheckBox checkBoxOnOff;
    public boolean isActive = false;
    public int overclockPercentage = 100;

    private static final ResourceLocation INVENTORY =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/inventory.png");
    private static final ResourceLocation CONFIG_BAR =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/config_bar.png");

    public ManagementMachineGui(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float PartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        RenderSystem.setShaderTexture(0, CONFIG_BAR);
        this.blit(poseStack, x-9, y+10, 0, 0, 256, 60);
        this.blit(poseStack, x+imageWidth+3, y+40, 0, 61, 62, 86);
        this.blit(poseStack, x-100, y+10, 0, 0, 100, 60);

        RenderSystem.setShaderTexture(0, INVENTORY);
        this.blit(poseStack, x, y + 100, 0, 0, 176, 100);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        //GuiComponent.drawString(poseStack, this.font, String.valueOf(overclock_percentage) + " %", 196, 26, 0xff8c00);
        //GuiComponent.drawString(poseStack, this.font, "⚡ " + String.valueOf((double) Math.round((default_energy_use * Math.pow( (double) overclock_percentage / 100, 1.6)) * 100.0) / 100.0) + " MW", -90, 26, 0xff8c00);
        //GuiComponent.drawString(poseStack, this.font, "⌛ " + String.valueOf((double) Math.round((purity_modifier * (double) overclock_percentage / 100 * default_mining_speed) * 100.0) / 100.0) + " items/min", -90, 48, 0xff8c00);
    }

    public void updateMachineInfos(boolean isActive, int overclockPercentage) {
        this.isActive = isActive;
        this.checkBoxOnOff.setToggled(isActive);

        this.overclockPercentage = overclockPercentage;
        this.sliderOverclockInner.setValue(overclockPercentage);
    }
}
