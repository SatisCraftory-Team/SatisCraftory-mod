package fr.satiscraftoryteam.satiscraftory.client.screen;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.MachineGUIType.ProductionBuildingMachineGUI;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmelterScreen extends ProductionBuildingMachineGUI<SmelterMenu> {
    private static final ResourceLocation GUI = SatisCraftory.rl("textures/gui/miner_mk1_gui.png");

    public SmelterScreen(SmelterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GUI);
    }
}
