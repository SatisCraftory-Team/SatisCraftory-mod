package fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.block.resources.DepositBlock;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class MinerExtractorMachine<BE extends BlockEntity> extends MachineBaseTileEntity<BE> {

    private final double initialPowerUsage;
    private final double initialExtractionRate;
    private float purityModifier;
    private double totalPowerUsage;
    private double totalExtractionRate;
    private DepositBlock blockResource;

    public MinerExtractorMachine(BlockEntityType<BE> type, BlockPos blockPos, BlockState blockState, int numberOfOutput, double initialPowerUsage, double initialExtractionRate, boolean hasOverclockPartition) {
        super(type, blockPos, blockState, 0, numberOfOutput, hasOverclockPartition);
        this.initialPowerUsage = initialPowerUsage;
        this.initialExtractionRate = initialExtractionRate;

        updateMachineInfos(100);
    }

    public void setBlockResource(DepositBlock blockResource) {
        this.blockResource = blockResource;
        this.purityModifier = blockResource.getPurityModifier();
        SatisCraftory.LOGGER.info("Purity Modifier: " + purityModifier);
    }

    public void setPowerUsage(int overclockPercentage) {
        this.totalPowerUsage = (double) Math.round((initialPowerUsage * Math.pow( (double) overclockPercentage / 100, 1.321928)) * 10.0) / 10.0;
    }

    public void setExtractionRate(int overclockPercentage) {
        this.totalExtractionRate = (double) Math.round((purityModifier * overclockPercentage / 100 * initialExtractionRate) * 10.0) / 10.0;
    }

    public void updateMachineInfos(int overclockPercentage) {
        setPowerUsage(overclockPercentage);
        setExtractionRate(overclockPercentage);
        maxProgress = (int) (60 * 20 / getExtractionRate());
    }

    public double getPowerUsage() {
        return totalPowerUsage;
    }

    public double getExtractionRate() {
        return totalExtractionRate;
    }


    // --------------------------------------MachineLogic---------------------------------------------------------//

    private int progress = 0;
    private int maxProgress = 0;

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if (hasNotReachedStackLimit()) {
            if(hasPower() && this.isActive) {
                SatisCraftory.LOGGER.info("Progress: " + progress + " / " + maxProgress + " | Power Usage: " + getPowerUsage() + " | Extraction Rate: " + getExtractionRate() + " | Overclock Percentage: " + overclockPercentage);
                if (progress >= maxProgress) {
                    progress = 0;
                    updateMachineInfos(overclockPercentage);
                    extractResource();
                }
                progress++;
            }
        } else {
            progress=0;
        }
    }

    private void extractResource() {
        outputPartition.setStackInSlot(0, new ItemStack(blockResource.getResidueExtracted(),
                outputPartition.getStackInSlot(0).getCount() + 1));
    }

    protected boolean hasPower() {
        // TODO: implement here power system
        return true;
    }

    public abstract @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player);
}
