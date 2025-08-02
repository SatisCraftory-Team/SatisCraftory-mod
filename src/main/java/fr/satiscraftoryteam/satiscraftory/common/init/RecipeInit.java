package fr.satiscraftoryteam.satiscraftory.common.init;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.recipe.SmelterRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, SatisCraftory.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, SatisCraftory.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SmelterRecipe>> SMELTER_SERIALIZER =
            SERIALIZERS.register("smelter", SmelterRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<SmelterRecipe>> SMELTER_TYPE =
            TYPES.register("smelter", () -> new RecipeType<SmelterRecipe>() {
                @Override
                public String toString() {
                    return "smelter";
                }
            });


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
