package fr.satiscraftoryteam.satiscraftory.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.satiscraftoryteam.satiscraftory.common.init.RecipeInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record SmelterRecipe(Ingredient inputItem, ItemStack output) implements Recipe<SmelterRecipeInput> {
    // inputItem & output ==> Read From JSON File!
    // SmelterRecipeInput --> INVENTORY of the Block Entity

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    @Override
    public boolean matches(SmelterRecipeInput smelterRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(smelterRecipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(SmelterRecipeInput smelterRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.SMELTER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.SMELTER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<SmelterRecipe> {
        public static final MapCodec<SmelterRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SmelterRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(SmelterRecipe::output)
//                Codec.INT.fieldOf("smeltingTime").orElse(smeltingTime).forGetter(p_300834_ -> p_300834_.smeltingTime)
        ).apply(inst, SmelterRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SmelterRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, SmelterRecipe::inputItem,
                        ItemStack.STREAM_CODEC, SmelterRecipe::output,
                        SmelterRecipe::new);

        @Override
        public MapCodec<SmelterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmelterRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}