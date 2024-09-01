package fr.satiscraftoryteam.satiscraftory.client.screen;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmelterScreen extends ManagementMachineGui<SmelterMenu> {
    private static final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");

    public SmelterScreen(SmelterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GUI);
    }
}
