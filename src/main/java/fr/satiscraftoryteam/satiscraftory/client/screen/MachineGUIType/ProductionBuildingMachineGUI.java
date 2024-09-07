package fr.satiscraftoryteam.satiscraftory.client.screen.MachineGUIType;

import fr.satiscraftoryteam.satiscraftory.client.screen.MachineBaseMenu;
import fr.satiscraftoryteam.satiscraftory.client.screen.ManagementMachineGui;
import fr.satiscraftoryteam.satiscraftory.common.network.packets.to_server.RequestMachineInfos;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData.ProductionBuildingMachine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class ProductionBuildingMachineGUI<T extends MachineBaseMenu<?>> extends ManagementMachineGui<T> {
    private final ProductionBuildingMachine<?> blockEntity;

    public ProductionBuildingMachineGUI(MachineBaseMenu<?> menu, Inventory inventory, Component component, ResourceLocation machineGUI) {
        super(menu, inventory, component, machineGUI);
        this.blockEntity = (ProductionBuildingMachine<?>) menu.machineEntity;
        PacketDistributor.sendToServer(new RequestMachineInfos(blockEntity.getBlockPos()));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, "⚡ " + blockEntity.getPowerUsage() + " MW", -90, 26, 0xff8c00);
        graphics.drawString(this.font, "⌛ " + blockEntity.getProductionRate() + " items/min", -90, 48, 0xff8c00);
    }
}
