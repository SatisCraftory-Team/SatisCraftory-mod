package fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

import java.text.DecimalFormat;

public class MinerExtractorMachine<BE extends BlockEntity> extends MachineBaseTileEntity<BE> {
    private final float initialPowerUsage;
    private final float initialExtractionRate;

    private final float purityModifier;

    private double powerUsage;
    private double extractionRate;

    public MinerExtractorMachine(BlockEntityType<BE> type, BlockPos blockPos, BlockState blockState, float initialPowerUsage, float initialExtractionRate, float purityModifier) {
        super(type, blockPos, blockState);
        this.initialPowerUsage = initialPowerUsage;
        this.initialExtractionRate = initialExtractionRate;
        this.purityModifier = purityModifier;
    }

    public void setExtractionRate(float purityModifier, int overclockPercentage) {
        this.extractionRate = (double) Math.round((purityModifier * overclockPercentage / 100 * initialExtractionRate) * 10.0) / 10.0;
    }

    public void setPowerUsage(int overclockPercentage) {
        this.powerUsage = (double) Math.round((initialPowerUsage * Math.pow( (double) overclockPercentage / 100, 1.321928)) * 10.0) / 10.0;
    }

    public void updateMachineInfos(int overclockPercentage) {
        setPowerUsage(overclockPercentage);
        setExtractionRate(purityModifier, overclockPercentage);
    }

    public double getPowerUsage() {
        return powerUsage;
    }

    public double getExtractionRate() {
        return extractionRate;
    }

    @Override
    public IItemHandler getOutputInventory() {
        return null;
    }

    @Override
    public IItemHandler getInputInventory() {
        return null;
    }

    @Override
    public int getNumberOfOverclocks() {
        return 0;
    }
}
