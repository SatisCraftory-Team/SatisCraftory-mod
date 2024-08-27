package fr.satiscraftoryteam.satiscraftory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.element.CheckBox;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server.UpdateMachineInfosServer;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class ManagementMachineGui<T extends MachineBaseMenu<?>> extends AbstractContainerScreen<T> {

    protected ExtendedSlider sliderOverclockInner;
    protected CheckBox checkBoxOnOff;
    public int overclockPercentage = 100;
    private final MachineBaseTileEntity blockEntity;
    private final ResourceLocation GUI;

    private static final ResourceLocation INVENTORY =
            ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/gui/inventory.png");
    private static final ResourceLocation CONFIG_BAR =
            ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/gui/config_bar.png");

    public ManagementMachineGui(MachineBaseMenu menu, Inventory inventory, Component component, ResourceLocation machineGUI) {
        super((T) menu, inventory, component);
        this.GUI = machineGUI;
        this.blockEntity = menu.machineEntity;
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

        graphics.blit(GUI, x, y, 0, 0, imageWidth, imageHeight);
    }

    public void updateMachineInfos(boolean isActive, int overclockPercentage) {
        this.checkBoxOnOff.setToggled(isActive);

        this.overclockPercentage = overclockPercentage;
        this.sliderOverclockInner.setValue(overclockPercentage);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        this.sliderOverclockInner.mouseClicked(mouseX, mouseY, mouseButton);
        PacketDistributor.sendToServer(new UpdateMachineInfosServer(this.blockEntity.getBlockPos(), checkBoxOnOff.isToggled(), overclockPercentage));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (sliderOverclockInner.isMouseOver(pMouseX, pMouseY)) {
            sliderOverclockInner.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
            PacketDistributor.sendToServer(new UpdateMachineInfosServer(this.blockEntity.getBlockPos(), checkBoxOnOff.isToggled(), overclockPercentage));
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (sliderOverclockInner.isMouseOver(mouseX, mouseY)) {
            sliderOverclockInner.setValue(sliderOverclockInner.getValueInt() + (scrollY > 0 ? 1 : -1));
            overclockPercentage = sliderOverclockInner.getValueInt();
            PacketDistributor.sendToServer(new UpdateMachineInfosServer(this.blockEntity.getBlockPos(), checkBoxOnOff.isToggled(), overclockPercentage));
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.checkBoxOnOff = this.addRenderableWidget(new CheckBox(blockEntity, this.leftPos + 6, this.topPos + 60, Component.translatable("gui.satiscraftory.machine.power")));

        int baseX = width / 2, baseY = height / 2;
        sliderOverclockInner = new ExtendedSlider(this.leftPos + 184, this.topPos + 15, 52, 20, Component.empty(), Component.translatable(" %"), 1, 250, this.overclockPercentage, true){
            @Override
            protected void applyValue() {
                overclockPercentage = this.getValueInt();
            }
        };

        addRenderableWidget(sliderOverclockInner);
    }
}
