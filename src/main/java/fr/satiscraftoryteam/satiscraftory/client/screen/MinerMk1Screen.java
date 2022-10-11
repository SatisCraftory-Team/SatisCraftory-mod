package fr.satiscraftoryteam.satiscraftory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.element.CheckBox;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.util.HashMap;
import java.util.Map;

public class MinerMk1Screen extends ManagementMachineGui<MinerMk1Menu> {
    private static final ResourceLocation GUI =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");
    private static final ResourceLocation INVENTORY =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/inventory.png");
    private static final ResourceLocation CONFIG_BAR =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/config_bar.png");

    private Inventory playerInventory;

    private static boolean OnOff = false;
    private ForgeSlider sliderOverclockInner;
    private ForgeSlider rangeSlider;
    private int overclock;
    private ItemStack machine;
    private int overclock_inner;
    private Map<ForgeSlider, IntConsumer> sliderMap = new HashMap<>();
    private CheckBox checkBoxOnOff;
    private int overclock_percentage = 100;
    private float default_energy_use = 5;

    //level miner_mk1 (default = 60)
    private float default_mining_speed = 60;

    //info gisement
    private float purity_modifier = 1;

    //items_per_minute = purity_modifier * overclock_percentage / 100 * default_mining_speed

    @Override
    public void init() {
        super.init();
        this.checkBoxOnOff = this.addRenderableWidget(new CheckBox(this.leftPos + 6, this.topPos + 60, Component.translatable("gui.satiscraftory.machine.power")));
        this.checkBoxOnOff.setToggled(MinerMk1Screen.OnOff);
        /*this.addRenderableWidget(new Button(this.leftPos + 184, this.topPos + 25, 10, 10, Component.literal("-"), button -> {
            if (overclock_percentage > 0) {
                overclock_percentage--;
            }
        }));
        this.addRenderableWidget(new Button(this.leftPos + 226, this.topPos + 25, 10, 10, Component.literal("+"), button -> {
            if (overclock_percentage < 250) {
                overclock_percentage++;
            }
        }));*/

        int baseX = width / 2, baseY = height / 2;
        sliderOverclockInner = new ForgeSlider(this.leftPos + 184, this.topPos + 15, 52, 20, Component.empty(), Component.translatable(" %"), 1, 250, this.overclock_percentage, true){
            @Override
            protected void applyValue() {
                overclock_percentage = this.getValueInt();
            }
        };

        addRenderableWidget(sliderOverclockInner);

        this.sliderMap = Map.of(
                sliderOverclockInner, (a) -> overclock = a
        );
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        //GuiComponent.drawString(poseStack, this.font, String.valueOf(overclock_percentage) + " %", 196, 26, 0xff8c00);
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
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        this.sliderMap.forEach((slider, consumer) -> {
            if (slider.isMouseOver(mouseX, mouseY)) {
                slider.setValue(slider.getValueInt() + (delta > 0 ? 1 : -1));
                consumer.accept(slider.getValueInt());
            }
        });

        return false;
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float partialTick, int pMouseX, int pMouseY) {
        super.renderBg(pPoseStack, partialTick, pMouseX, pMouseY);

        //RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //int x = this.leftPos;
        //int y = this.topPos;

//        RenderSystem.setShaderTexture(0, CONFIG_BAR);
//        //RIGHT part
//        this.blit(pPoseStack, x-9, y+10, 0, 0, 256, 60);
//        this.blit(pPoseStack, x+imageWidth+3, y+40, 0, 61, 62, 86);
//        //LEFT part
//        this.blit(pPoseStack, x-100, y+10, 0, 0, 100, 60);

        RenderSystem.setShaderTexture(0, GUI);
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

//        RenderSystem.setShaderTexture(0, INVENTORY);
//        this.blit(pPoseStack, x, y + 100, 0, 0, 176, 100);

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
