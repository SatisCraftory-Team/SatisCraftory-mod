package fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData;

import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PowerGeneratorMachine<BE extends BlockEntity> extends MachineBaseTileEntity<BE> {

    private final double initialFuelBurnTime;
    private final double initialPowerCapacity;
    private double totalFuelBurnTime;
    private double totalPowerCapacity;

    private int progress = 0;
    private int maxProgress = 0;

    public PowerGeneratorMachine(BlockEntityType<BE> type, BlockPos pos, BlockState state, int numberOfInput, int numberOfOutput, double initialFuelBurnTime, double initialPowerCapacity, boolean hasOverclockPartition) {
        super(type, pos, state, numberOfInput, numberOfOutput, hasOverclockPartition);
        this.initialFuelBurnTime = initialFuelBurnTime;
        this.initialPowerCapacity = initialPowerCapacity;
        updateMachineInfos(100);
    }

    @Override
    public void updateMachineInfos(int overclockPercentage) {
        setFuelBurnTime(overclockPercentage);
        setPowerCapacity(overclockPercentage);
        maxProgress = (int) (60 * 20 / getFuelBurnTime());
    }

    public void setPowerCapacity(int overclockPercentage) {
        this.totalPowerCapacity = (double) Math.round((initialPowerCapacity * overclockPercentage / 100) * 10.0) / 10.0;
    }

    public void setFuelBurnTime(int overclockPercentage) {
        this.totalFuelBurnTime = (double) Math.round((initialFuelBurnTime * 100 / overclockPercentage) * 10.0) / 10.0;
    }

    public double getFuelBurnTime() {
        return totalFuelBurnTime;
    }

    public double getPowerCapacity() {
        return totalPowerCapacity;
    }
}
