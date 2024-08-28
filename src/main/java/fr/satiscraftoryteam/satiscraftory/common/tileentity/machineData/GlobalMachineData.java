package fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData;

import fr.satiscraftoryteam.satiscraftory.common.init.ItemInit;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GlobalMachineData<BE extends BlockEntity> extends MachineBaseTileEntity<BE> {

    // Data for MinerExtractorMachine and ProductionBuildingMachine
    private double initialPowerUsage;
    private double totalPowerUsage;

    // Data for the MinerExtractorMachine
    private double initialExtractionRate;
    private float purityModifier;
    private double totalExtractionRate;

    // Data for the ProductionBuildingMachine
    private double initialProductionRate;
    private double totalProductionRate;

    // Data for PowerGeneratorMachine
    private double initialFuelBurnTime;
    private double initialPowerCapacity;
    private double totalFuelBurnTime;
    private double totalPowerCapacity;

    public GlobalMachineData(BlockEntityType<BE> type, BlockPos blockPos, BlockState blockState, boolean hasOverclockPartition) {
        super(type, blockPos, blockState, 1,1, hasOverclockPartition);
    }

    public void setMinerExtractorMachineData(double initialPowerUsage, double initialExtractionRate, float purityModifier) {
        this.initialPowerUsage = initialPowerUsage;
        this.initialExtractionRate = initialExtractionRate;
        this.purityModifier = purityModifier;
        setPowerUsage(100);
        setExtractionRate(purityModifier, 100);
    }

    public void setProductionBuildingMachineData(double initialPowerUsage, double initialProductionRate) {
        this.initialPowerUsage = initialPowerUsage;
        this.initialProductionRate = initialProductionRate;
        setPowerUsage(100);
        setProductionRate(100);
    }

    public void setPowerGeneratorMachineData(double initialFuelBurnTime, double initialPowerCapacity) {
        this.initialFuelBurnTime = initialFuelBurnTime;
        this.initialPowerCapacity = initialPowerCapacity;
        setFuelBurnTime(100);
        setPowerCapacity(100);
    }

    public void setPowerUsage(int overclockPercentage) {
        this.totalPowerUsage = (double) Math.round((initialPowerUsage * Math.pow( (double) overclockPercentage / 100, 1.321928)) * 10.0) / 10.0;
    }

    public void setExtractionRate(float purityModifier, int overclockPercentage) {
        this.totalExtractionRate = (double) Math.round((purityModifier * overclockPercentage / 100 * initialExtractionRate) * 10.0) / 10.0;
    }

    public void setProductionRate(int overclockPercentage) {
        this.totalProductionRate = (double) Math.round((initialProductionRate * Math.pow( (double) overclockPercentage / 100, 1.321928)) * 10.0) / 10.0;
    }

    public void setPowerCapacity(int overclockPercentage) {
        this.totalPowerCapacity = (double) Math.round((initialPowerCapacity * overclockPercentage / 100) * 10.0) / 10.0;
    }

    public void setFuelBurnTime(int overclockPercentage) {
        this.totalFuelBurnTime = (double) Math.round((initialFuelBurnTime * 100 / overclockPercentage) * 10.0) / 10.0;
    }

    public void updateMachineInfos(int overclockPercentage) {
        setPowerUsage(overclockPercentage);
        setExtractionRate(purityModifier, overclockPercentage);
    }

    public double getPowerUsage() {
        return totalPowerUsage;
    }

    public double getExtractionRate() {
        return totalExtractionRate;
    }

    public double getProductionRate() {
        return totalProductionRate;
    }

    public double totalPowerCapacity() {
        return totalPowerCapacity;
    }

    public double getFuelBurnTime() {
        return totalFuelBurnTime;
    }

    public double getPowerCapacity() {
        return totalPowerCapacity;
    }

    // --------------------------------------MachineLogic---------------------------------------------------------//

    private void craftItem() {/*
        itemHandler.extractItem(1, 1, false);
        itemHandler.extractItem(2, 1, false);
        itemHandler.extractItem(3, 1, false);*/

        outputPartition.setStackInSlot(0, new ItemStack(ItemInit.IRON_RESIDUE.get(),
                outputPartition.getStackInSlot(0).getCount() + 1));
    }

    private boolean hasPower() {
//        boolean hasItemInFirstSlot = overclockPartition.getStackInSlot(0).getItem() == ItemInit.POWER_SHARD.get();
//        boolean hasItemInSecondSlot = overclockPartition.getStackInSlot(1).getItem() == ItemInit.POWER_SHARD.get();
//        boolean hasItemInThirdSlot = overclockPartition.getStackInSlot(2).getItem() == ItemInit.POWER_SHARD.get();

//        return hasItemInFirstSlot && hasItemInSecondSlot && hasItemInThirdSlot;
        // TODO: implement here power system
        return true;
    }
}
