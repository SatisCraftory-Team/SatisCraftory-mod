package fr.vgtom4.satiscraftory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.vgtom4.satiscraftory.SatisCraftory;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MinerMk1Screen extends AbstractContainerScreen<MinerMk1Menu> {
    private static final ResourceLocation GUI =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");

    private Inventory playerInventory;

    private static boolean OnOff = false;

    private CheckBox checkBoxOnOff;
    private int percentage_overclock = 100;

    @Override
    public void init() {
        super.init();
        this.checkBoxOnOff = this.addRenderableWidget(new CheckBox(this.leftPos + 180, this.topPos + 20, Component.translatable("gui.satiscraftory.miner_mk1.power")));
        this.checkBoxOnOff.setToggled(MinerMk1Screen.OnOff);
        this.addRenderableWidget(new Button(this.leftPos + 107, this.topPos + 25, 10, 10, Component.literal("-"), button -> {
            if (percentage_overclock > 0) {
                percentage_overclock--;
            }
        }));
        this.addRenderableWidget(new Button(this.leftPos + 149, this.topPos + 25, 10, 10, Component.literal("+"), button -> {
            if (percentage_overclock < 300) {
                percentage_overclock++;
            }
        }));/*
        radiusLabel = new ScrollableLabel()
                .hint(1, 1, 1, 1)
                .name("radius")
                .visible(false)
                .realMaximum(20);
        visibleRadiusLabel = label(55, 4, 30, 13, "")
                .desiredWidth(30)
                .horizontalAlignment(HorizontalAlignment.ALIGN_LEFT);*/
    }

    public MinerMk1Screen(MinerMk1Menu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean result = super.mouseClicked(mouseX, mouseY, mouseButton);
        MinerMk1Screen.OnOff = this.checkBoxOnOff.isToggled();
        return result;
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float PartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        GuiComponent.drawString(poseStack, this.font, String.valueOf(percentage_overclock) + " %", 119, 26, 0xff8c00);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);

        int startX = this.leftPos;
        int startY = this.topPos;
    }


}
