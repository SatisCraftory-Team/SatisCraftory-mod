package fr.satiscraftoryteam.satiscraftory.client.screen.element;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CheckBox extends AbstractButton
{
    private static final ResourceLocation BUTTON_ON_OFF = ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/gui/components.png");

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
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BUTTON_ON_OFF);
        guiGraphics.blit(BUTTON_ON_OFF, this.getX(), this.getY(), 0, 0, 8, 8);
        if(!this.toggled)
        {
            guiGraphics.blit(BUTTON_ON_OFF, this.getX(), this.getY(), 8, 0, 9, 8);
        }
        guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 12, this.getY(), 0xFFFFFF);
    }


    @Override
    public void onPress() {
        this.toggled = !this.toggled;
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