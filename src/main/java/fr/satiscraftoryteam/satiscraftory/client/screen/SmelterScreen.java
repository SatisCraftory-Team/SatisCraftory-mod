package fr.satiscraftoryteam.satiscraftory.client.screen;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.element.CheckBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmelterScreen extends ManagementMachineGui<SmelterMenu> {
    private static final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");

    private Inventory playerInventory;

    private static boolean OnOff = false;

    private CheckBox checkBoxOnOff;
    private int overclock_percentage = 100;
    private float default_energy_use = 4;

    //level miner_mk1
    private float default_speed = 30;

    //items_per_minute =  overclock_percentage / 100 * default_speed
    //int items_per_minute;

    @Override
    public void init() {
        super.init();
        this.checkBoxOnOff = this.addRenderableWidget(new CheckBox(null, this.leftPos + 180, this.topPos + 20, Component.translatable("gui.satiscraftory.machine.power")));
        this.checkBoxOnOff.setToggled(SmelterScreen.OnOff);
//        this.addRenderableWidget(new Button(this.leftPos + 107, this.topPos + 25, 10, 10, Component.literal("-"), button -> {
//            if (overclock_percentage > 0) {
//                overclock_percentage--;
//            }
//        }));
//        this.addRenderableWidget(new Button(this.leftPos + 149, this.topPos + 25, 10, 10, Component.literal("+"), button -> {
//            if (overclock_percentage < 250) {
//                overclock_percentage++;
//            }
//        }));
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

//    @Override
//    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
//        GuiComponent.drawString(poseStack, this.font, String.valueOf(overclock_percentage) + " %", 119, 26, 0xff8c00);
//        GuiComponent.drawString(poseStack, this.font, "⚡ " + String.valueOf((double) Math.round((default_energy_use * Math.pow( (double) overclock_percentage / 100, 1.6)) * 100.0) / 100.0) + " MW", 10, 15, 0xff8c00);
//        GuiComponent.drawString(poseStack, this.font, "⌛ " + String.valueOf((double) Math.round(((double) overclock_percentage / 100 * default_speed) * 100.0) / 100.0) + " items/min", 10, 26, 0xff8c00);
//    }

    public SmelterScreen(SmelterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GUI);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean result = super.mouseClicked(mouseX, mouseY, mouseButton);
        SmelterScreen.OnOff = this.checkBoxOnOff.isToggled();
        return result;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float PartialTick, int pMouseX, int pMouseY) {
        super.renderBg(graphics, PartialTick, pMouseX, pMouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // GUIs are machine specific, so keep it here
        graphics.blit(GUI, x, y, 0, 0, imageWidth, imageHeight);
    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
