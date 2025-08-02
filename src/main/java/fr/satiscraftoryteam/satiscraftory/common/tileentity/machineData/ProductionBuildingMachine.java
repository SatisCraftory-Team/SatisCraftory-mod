package fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData;

import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.MachineBaseTileEntity;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.base.TickableTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ProductionBuildingMachine<BE extends BlockEntity, RC extends Recipe<?>> extends MachineBaseTileEntity<BE> {

    private final double initialPowerUsage;
    private final double initialProductionRate;

    private double totalPowerUsage;
    private double totalProductionRate;

    protected RecipeType<RC> recipeType;

    public ProductionBuildingMachine(BlockEntityType<BE> type, BlockPos pos, BlockState state, int numberOfInput, int numberOfOutput, double initialPowerUsage, double initialProductionRate, boolean hasOverclockPartition, RecipeType recipeType) {
        super(type, pos, state, numberOfInput, numberOfOutput, hasOverclockPartition);
        this.initialPowerUsage = initialPowerUsage;
        this.initialProductionRate = initialProductionRate;
        this.recipeType = recipeType;
        updateMachineInfos(100);
    }

    @Override
    public void updateMachineInfos(int overclockPercentage) {
        setPowerUsage(overclockPercentage);
        setProductionRate(overclockPercentage);
        this.overclockPercentage = overclockPercentage;
        this.maxProgress = (int) (60 * 20 / getProductionRate());
    }

    public void setPowerUsage(int overclockPercentage) {
        this.totalPowerUsage = (double) Math.round((initialPowerUsage * Math.pow( (double) overclockPercentage / 100, 1.321928)) * 100.0) / 100.0;
    }

    public void setProductionRate(int overclockPercentage) {
        this.totalProductionRate = (double) Math.round((initialProductionRate * Math.pow( (double) overclockPercentage / 100, 1.321928)) * 10.0) / 10.0;
    }

    public double getPowerUsage() {
        return totalPowerUsage;
    }

    public double getProductionRate() {
        return totalProductionRate;
    }

    public abstract @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player);

    protected abstract boolean hasRecipe();

    protected abstract Optional<RecipeHolder<RC>> getCurrentRecipe();

    protected abstract void craftItem();

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state, TickableTileEntity tile) {
        if (hasNotReachedStackLimit()) {
            if (hasPower() && this.isActive) {
                if (progress >= maxProgress) {
                    progress = 0;
                    updateMachineInfos(overclockPercentage);
                    craftItem();
                }
                progress++;
            }
        } else {
            progress = 0;
        }
    }
}
