package fr.satiscraftoryteam.satiscraftory.client.screen.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CheckBox extends AbstractWidget
{
    private static final ResourceLocation BUTTON_ON_OFF = SatisCraftory.rl("satiscraftory:textures/gui/components.png");

    private boolean toggled = false;

    public CheckBox(int left, int top, Component title)
    {
        super(left, top, 8, 8, title);
    }

    public void setToggled(boolean toggled)
    {
        this.toggled = toggled;
    }

    public boolean isToggled()
    {
        return this.toggled;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BUTTON_ON_OFF);
        this.blit(poseStack, this.x, this.y, 0, 0, 8, 8);
        if(!this.toggled)
        {
            this.blit(poseStack, this.x, this.y, 8, 0, 9, 8);
        }
        drawString(poseStack, Minecraft.getInstance().font, this.getMessage(), this.x + 12, this.y, 0xFFFFFF);
    }

    private int oof;

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    public void onClick(double mouseX, double mouseY)
    {
        this.toggled = !this.toggled;
    }

//    @Override
//    public void updateNarration(NarrationElementOutput output)
//    {
//        this.defaultButtonNarrationText(output);
//    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public void playDownSound(SoundManager soundManager) {
        if (this.toggled) {
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BEACON_ACTIVATE, 1.0F));
        } else {
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BEACON_DEACTIVATE, 1.0F));
        }
    }
}