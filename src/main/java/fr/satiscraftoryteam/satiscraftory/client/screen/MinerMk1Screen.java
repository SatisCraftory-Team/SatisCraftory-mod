package fr.satiscraftoryteam.satiscraftory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.element.CheckBox;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.PacketGetMachineInfos;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.ServerboundUpdatePacketInfos;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.MinerMk1BlockEntity;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class MinerMk1Screen extends ManagementMachineGui<MinerMk1Menu> {
    private static final ResourceLocation GUI =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");
    private Inventory playerInventory;
    private ForgeSlider rangeSlider;
    private float default_energy_use = 5;

    //level miner_mk1 (default = 60)
    private float default_mining_speed = 60;

    //info gisement
    private float purity_modifier = 1;

    private final MinerMk1BlockEntity tileEntity;

    //items_per_minute = purity_modifier * overclock_percentage / 100 * default_mining_speed
    
    public MinerMk1Screen(MinerMk1Menu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        tileEntity = pMenu.blockEntity;
        SatisCraftory.packetHandler.getChannel().sendToServer(new PacketGetMachineInfos(this.tileEntity.getBlockPos()));
    }

    @Override
    public void init() {
        super.init();
        this.checkBoxOnOff = this.addRenderableWidget(new CheckBox(this.leftPos + 6, this.topPos + 60, Component.translatable("gui.satiscraftory.machine.power")));

        int baseX = width / 2, baseY = height / 2;
        sliderOverclockInner = new ForgeSlider(this.leftPos + 184, this.topPos + 15, 52, 20, Component.empty(), Component.translatable(" %"), 1, 250, this.overclockPercentage, true){
            @Override
            protected void applyValue() {
                overclockPercentage = this.getValueInt();
            }
        };

        addRenderableWidget(sliderOverclockInner);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        // Labels are machine specific, so keep it here
        GuiComponent.drawString(poseStack, this.font, "⚡ " + String.valueOf((double) Math.round((default_energy_use * Math.pow( (double) overclockPercentage / 100, 1.6)) * 100.0) / 100.0) + " MW", -90, 26, 0xff8c00);
        GuiComponent.drawString(poseStack, this.font, "⌛ " + String.valueOf((double) Math.round((purity_modifier * (double) overclockPercentage / 100 * default_mining_speed) * 100.0) / 100.0) + " items/min", -90, 48, 0xff8c00);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        this.sliderOverclockInner.mouseClicked(mouseX, mouseY, mouseButton);
        SatisCraftory.packetHandler.getChannel().sendToServer(new ServerboundUpdatePacketInfos(this.tileEntity.getBlockPos(), checkBoxOnOff.isToggled(), overclockPercentage));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (sliderOverclockInner.isMouseOver(pMouseX, pMouseY)) {
            sliderOverclockInner.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
            SatisCraftory.packetHandler.getChannel().sendToServer(new ServerboundUpdatePacketInfos(this.tileEntity.getBlockPos(), checkBoxOnOff.isToggled(), overclockPercentage));
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (sliderOverclockInner.isMouseOver(mouseX, mouseY)) {
            sliderOverclockInner.setValue(sliderOverclockInner.getValueInt() + (delta > 0 ? 1 : -1));
            overclockPercentage = sliderOverclockInner.getValueInt();
            SatisCraftory.packetHandler.getChannel().sendToServer(new ServerboundUpdatePacketInfos(this.tileEntity.getBlockPos(), checkBoxOnOff.isToggled(), overclockPercentage));
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float partialTick, int pMouseX, int pMouseY) {
        super.renderBg(pPoseStack, partialTick, pMouseX, pMouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // GUIs are machine specific, so keep it here
        RenderSystem.setShaderTexture(0, GUI);
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }
}
