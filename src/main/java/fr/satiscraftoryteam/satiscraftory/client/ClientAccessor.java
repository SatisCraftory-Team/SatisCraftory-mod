package fr.satiscraftoryteam.satiscraftory.client;

import fr.satiscraftoryteam.satiscraftory.client.screen.ManagementMachineGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ClientAccessor {
    public static void updateMachineScreen(boolean isActive, int overclockPercentage) {
        final Screen screen = Minecraft.getInstance().screen;
        System.out.println(screen.getTitle());
        if (screen instanceof final ManagementMachineGui managementMachineGui) {
            System.out.println("updated");
            managementMachineGui.updateMachineInfos(isActive, overclockPercentage);
        }
    }
}
