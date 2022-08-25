package fr.satiscraftoryteam.satiscraftory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.element.CheckBox;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class MinerMk1Screen extends AbstractContainerScreen<MinerMk1Menu>{
    private static final ResourceLocation GUI =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");
    private static final ResourceLocation INVENTORY =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/inventory.png");
    private static final ResourceLocation CONFIG_BAR =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/config_bar.png");

    private Inventory playerInventory;

    private static boolean OnOff = false;
    private ForgeSlider sliderOverclock;
    private CheckBox checkBoxOnOff;
    private int overclock_percentage = 100;
    private float default_energy_use = 5;

    //level miner_mk1 (default = 60)
    private float default_mining_speed = 60;

    //info gisement
    private float purity_modifier = 1;

    //items_per_minute = purity_modifier * overclock_percentage / 100 * default_mining_speed
    //int items_per_minute;

    @Override
    public void init() {
        super.init();
        this.checkBoxOnOff = this.addRenderableWidget(new CheckBox(this.leftPos + 6, this.topPos + 60, Component.translatable("gui.satiscraftory.machine.power")));
        this.checkBoxOnOff.setToggled(MinerMk1Screen.OnOff);
        this.addRenderableWidget(new Button(this.leftPos + 184, this.topPos + 25, 10, 10, Component.literal("-"), button -> {
            if (overclock_percentage > 0) {
                overclock_percentage--;
            }
        }));
        this.addRenderableWidget(new Button(this.leftPos + 226, this.topPos + 25, 10, 10, Component.literal("+"), button -> {
            if (overclock_percentage < 250) {
                overclock_percentage++;
            }
        }));

        int baseX = width / 2, baseY = height / 2;
        sliderOverclock = new ForgeSlider(baseX - (150), baseY - 10, 54, 10, Component.empty(), Component.translatable(" %"), 0, 250, this.overclock_percentage, true){
            @Override
            protected void applyValue() {
                overclock_percentage = this.getValueInt();
            }
        };

        addRenderableWidget(sliderOverclock);
        /*
        radiusLabel = new ScrollableLabel()
                .hint(1, 1, 1, 1)
                .name("radius")
                .visible(false)
                .realMaximum(20);
        visibleRadiusLabel = label(55, 4, 30, 13, "")
                .desiredWidth(30)
                .horizontalAlignment(HorizontalAlignment.ALIGN_LEFT);*/
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        GuiComponent.drawString(poseStack, this.font, String.valueOf(overclock_percentage) + " %", 196, 26, 0xff8c00);
        GuiComponent.drawString(poseStack, this.font, "⚡ " + String.valueOf((double) Math.round((default_energy_use * Math.pow( (double) overclock_percentage / 100, 1.6)) * 100.0) / 100.0) + " MW", -90, 26, 0xff8c00);
        GuiComponent.drawString(poseStack, this.font, "⌛ " + String.valueOf((double) Math.round((purity_modifier * (double) overclock_percentage / 100 * default_mining_speed) * 100.0) / 100.0) + " items/min", -90, 48, 0xff8c00);
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

        //int x = this.leftPos;
        //int y = this.topPos;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);



        RenderSystem.setShaderTexture(0, CONFIG_BAR);
        this.blit(pPoseStack, x-100, y+10, 0, 0, 100, 60);
        this.blit(pPoseStack, x+imageWidth, y+10, 106, 60, 176, 120);


        RenderSystem.setShaderTexture(0, INVENTORY);
        this.blit(pPoseStack, x, y + 100, 0, 0, 176, 100);


    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
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
