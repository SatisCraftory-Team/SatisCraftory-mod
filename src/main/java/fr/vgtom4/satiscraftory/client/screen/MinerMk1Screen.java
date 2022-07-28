package fr.vgtom4.satiscraftory.client.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import fr.vgtom4.satiscraftory.SatisCraftory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class MinerMk1Screen extends AbstractContainerScreen<MinerMk1Menu> {
    private static final ResourceLocation GUI =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");

    private static boolean OnOff = false;

    private CheckBox checkBoxOnOff;

    @Override
    public void init() {
        this.checkBoxOnOff = this.addRenderableWidget(new CheckBox(this.leftPos + 172, this.topPos + 51, Component.translatable("gui.satiscraftory.miner_mk1.power")));
        this.checkBoxOnOff.setToggled(MinerMk1Screen.OnOff);
    }

    public MinerMk1Screen(MinerMk1Menu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean result = super.mouseClicked(mouseX, mouseY, mouseButton);
        MinerMk1Screen.OnOff = this.checkBoxOnOff.isToggled();
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /*
    @Override
    protected void renderBg(PoseStack pPoseStack, float PartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }*/

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }


    private Inventory playerInventory;
    private Button btnCraft;

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {

        int startX = this.leftPos;
        int startY = this.topPos;

        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(poseStack, startX, startY, 0, 0, 173, 184);
        blit(poseStack, startX + 173, startY, 78, 184, 173, 0, 1, 184, 256, 256);
        this.blit(poseStack, startX + 251, startY, 174, 0, 24, 184);
        this.blit(poseStack, startX + 172, startY + 16, 198, 0, 20, 20);
    }
}
