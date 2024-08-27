package fr.satiscraftoryteam.satiscraftory.client.screen.MachineGUIType;

import fr.satiscraftoryteam.satiscraftory.client.screen.MachineBaseMenu;
import fr.satiscraftoryteam.satiscraftory.client.screen.ManagementMachineGui;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server.RequestMachineInfos;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData.MinerExtractorMachine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class MinerExtractorMachineGUI<T extends MachineBaseMenu<?>> extends ManagementMachineGui<T> {
    private final MinerExtractorMachine<?> blockEntity;

    public MinerExtractorMachineGUI(MachineBaseMenu<?> menu, Inventory inventory, Component component, ResourceLocation machineGUI) {
        super(menu, inventory, component, machineGUI);
        this.blockEntity = (MinerExtractorMachine<?>) menu.machineEntity;
        PacketDistributor.sendToServer(new RequestMachineInfos(blockEntity.getBlockPos()));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, "⚡ " + blockEntity.getPowerUsage() + " MW", -90, 26, 0xff8c00);
        graphics.drawString(this.font, "⌛ " + blockEntity.getExtractionRate() + " items/min", -90, 48, 0xff8c00);
    }
}
