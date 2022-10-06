package fr.satiscraftoryteam.satiscraftory.common.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class MachineProperties {

    private static final int DEFAULT_OVERCLOCK_PERCENTAGE = 100;

    public static int setOverclock(ItemStack machine, int overclock_percentage) {
        machine.getOrCreateTag().putInt("overclock", overclock_percentage);
        return overclock_percentage;
    }

    public static int getOverclock(ItemStack machine) {
        CompoundTag compound = machine.getOrCreateTag();
        return !compound.contains("overclock") ? setOverclock(machine, 100) : compound.getInt("overclock");
    }
}
