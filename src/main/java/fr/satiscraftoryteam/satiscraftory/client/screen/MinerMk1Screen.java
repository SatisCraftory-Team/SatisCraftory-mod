package fr.satiscraftoryteam.satiscraftory.client.screen;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.client.screen.MachineGUIType.MinerExtractorMachineGUI;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MinerMk1Screen extends MinerExtractorMachineGUI<MinerMk1Menu> {
    private static final ResourceLocation GUI =
            ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "textures/gui/miner_mk1_gui.png");

    public MinerMk1Screen(MinerMk1Menu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, GUI);
    }
}
