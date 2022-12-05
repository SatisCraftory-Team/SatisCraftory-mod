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
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class MinerMk1Screen extends ManagementMachineGui<MinerMk1Menu> {
    private static final ResourceLocation GUI =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");
    private static final ResourceLocation INVENTORY =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/inventory.png");
    private static final ResourceLocation CONFIG_BAR =
            new ResourceLocation(SatisCraftory.MODID, "textures/gui/config_bar.png");

    private Inventory playerInventory;

    private static boolean OnOff = false;
    private ForgeSlider rangeSlider;
    private int overclock;
    private ItemStack machine;
    private int overclock_inner;

    //private int overclock_percentage = 100;
    private float default_energy_use = 5;

    //level miner_mk1 (default = 60)
    private float default_mining_speed = 60;

    //info gisement
    private float purity_modifier = 1;

    private final MinerMk1BlockEntity tileEntity;

    //items_per_minute = purity_modifier * overclock_percentage / 100 * default_mining_speed

    //TODO: Abstract this class correctly with ManagementMachineGui
    public MinerMk1Screen(MinerMk1Menu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        tileEntity = pMenu.blockEntity;
        SatisCraftory.packetHandler.getChannel().sendToServer(new PacketGetMachineInfos(this.tileEntity.getBlockPos()));
    }


    @Override
    public void init() {
        super.init();

        this.checkBoxOnOff = this.addRenderableWidget(new CheckBox(this.leftPos + 6, this.topPos + 60, Component.translatable("gui.satiscraftory.machine.power")));
        this.checkBoxOnOff.setToggled(isActive);
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
        sliderOverclockInner = new ForgeSlider(this.leftPos + 184, this.topPos + 15, 52, 20, Component.empty(), Component.translatable(" %"), 1, 250, this.overclockPercentage, true){
            @Override
            protected void applyValue() {
                overclockPercentage = this.getValueInt();
            }
        };

        this.

        addRenderableWidget(sliderOverclockInner);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        //GuiComponent.drawString(poseStack, this.font, String.valueOf(overclock_percentage) + " %", 196, 26, 0xff8c00);
        GuiComponent.drawString(poseStack, this.font, "⚡ " + String.valueOf((double) Math.round((default_energy_use * Math.pow( (double) overclockPercentage / 100, 1.6)) * 100.0) / 100.0) + " MW", -90, 26, 0xff8c00);
        GuiComponent.drawString(poseStack, this.font, "⌛ " + String.valueOf((double) Math.round((purity_modifier * (double) overclockPercentage / 100 * default_mining_speed) * 100.0) / 100.0) + " items/min", -90, 48, 0xff8c00);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        this.sliderOverclockInner.mouseClicked(mouseX, mouseY, mouseButton);
        boolean result = super.mouseClicked(mouseX, mouseY, mouseButton);
        isActive = this.checkBoxOnOff.isToggled();
        SatisCraftory.packetHandler.getChannel().sendToServer(new ServerboundUpdatePacketInfos(this.tileEntity.getBlockPos(), isActive, overclockPercentage));
        return result;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (sliderOverclockInner.isMouseOver(pMouseX, pMouseY)) {
            sliderOverclockInner.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
        SatisCraftory.packetHandler.getChannel().sendToServer(new ServerboundUpdatePacketInfos(this.tileEntity.getBlockPos(), isActive, overclockPercentage));
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (sliderOverclockInner.isMouseOver(mouseX, mouseY)) {
            sliderOverclockInner.setValue(sliderOverclockInner.getValueInt() + (delta > 0 ? 1 : -1));
            overclockPercentage = sliderOverclockInner.getValueInt();
            SatisCraftory.packetHandler.getChannel().sendToServer(new ServerboundUpdatePacketInfos(this.tileEntity.getBlockPos(), isActive, overclockPercentage));
            return true;
        }
        return false;
//        this.sliderMap.forEach((slider, consumer) -> {
//            if (slider.isMouseOver(mouseX, mouseY)) {
//                slider.setValue(slider.getValueInt() + (delta > 0 ? 1 : -1));
//                consumer.accept(slider.getValueInt());
//            }
//        });
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
