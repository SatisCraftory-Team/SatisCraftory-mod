package fr.satiscraftoryteam.satiscraftory.common.tileentity;

import fr.satiscraftoryteam.satiscraftory.client.screen.SmelterMenu;
import fr.satiscraftoryteam.satiscraftory.common.init.RecipeInit;
import fr.satiscraftoryteam.satiscraftory.common.init.TileEntityInit;
import fr.satiscraftoryteam.satiscraftory.common.interfaces.IBoundingBlock;
import fr.satiscraftoryteam.satiscraftory.common.recipe.SmelterRecipe;
import fr.satiscraftoryteam.satiscraftory.common.recipe.SmelterRecipeInput;
import fr.satiscraftoryteam.satiscraftory.common.tileentity.machineData.ProductionBuildingMachine;
import fr.satiscraftoryteam.satiscraftory.utils.RelativeOrientationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Tuple;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SmelterBlockEntity extends ProductionBuildingMachine<SmelterBlockEntity, SmelterRecipe> implements MenuProvider, IBoundingBlock {
    public List<RecipeHolder<SmelterRecipe>> recipeHolderList;
    public SmelterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TileEntityInit.SMELTER_BLOCK_ENTITY.get(), blockPos, blockState, 1, 1, 4, 30, true, RecipeInit.SMELTER_TYPE.get());

        this.CONVEYOR_OUTPUT_POS_ORIENTATION.add(new Tuple<>(new Vec3i(0, 0, 1), RelativeOrientationUtils.RelativeOrientation.FRONT));

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -1; z <= 3; z++) {
                    Vec3i pos = new Vec3i(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    boolean shouldNotAdd = false;
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_OUTPUT_POS_ORIENTATION) {
                        if (tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_INPUT_POS_ORIENTATION) {
                        if (tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    if (shouldNotAdd) {
                        continue;
                    }
                    this.BOUNDING_BLOCKS_POS.add(pos);
                }
            }
        }

        this.CONVEYOR_INPUT_POS_ORIENTATION.add(new Tuple<>(new Vec3i(0, 0, -1), RelativeOrientationUtils.RelativeOrientation.BACK));
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -1; z <= 3; z++) {
                    Vec3i pos = new Vec3i(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    boolean shouldNotAdd = false;
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_INPUT_POS_ORIENTATION) {
                        if (tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    for (Tuple<Vec3i, RelativeOrientationUtils.RelativeOrientation> tuple : CONVEYOR_OUTPUT_POS_ORIENTATION) {
                        if (tuple.getA().equals(pos)) {
                            shouldNotAdd = true;
                            break;
                        }
                    }
                    if (shouldNotAdd) {
                        continue;
                    }
                    this.BOUNDING_BLOCKS_POS.add(pos);
                }
            }
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        if (this.level != null) {
            this.recipeHolderList = this.level.getRecipeManager().getAllRecipesFor(RecipeInit.SMELTER_TYPE.get());
        }
        System.out.println(this.recipeHolderList);
        for (RecipeHolder<SmelterRecipe> recipe : this.recipeHolderList) {
            System.out.println(recipe);
        }
        return new SmelterMenu(pContainerId, pInventory, this);
    }

    @Override
    protected Optional<RecipeHolder<SmelterRecipe>> getCurrentRecipe() {
        assert this.level != null;
        return this.level.getRecipeManager()
                .getRecipeFor(RecipeInit.SMELTER_TYPE.get(), new SmelterRecipeInput(this.inputPartition.getStackInSlot(0)), level);
    }

    @Override
    protected boolean hasRecipe() {
        Optional<RecipeHolder<SmelterRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    @Override
    protected void craftItem() {
        Optional<RecipeHolder<SmelterRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return;
        }
        ItemStack output = recipe.get().value().output();
        if (output.isEmpty()) {
            return;
        }
        this.inputPartition.extractItem(0, 1, false);
        this.outputPartition.setStackInSlot(0, new ItemStack(output.getItem(),
                this.outputPartition.getStackInSlot(0).getCount() + output.getCount()));
    }
}