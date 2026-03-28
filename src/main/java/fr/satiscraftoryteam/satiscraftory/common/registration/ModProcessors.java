package fr.satiscraftoryteam.satiscraftory.common.registration;

import com.mojang.serialization.MapCodec;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.worldgen.processor.RandomizePropertyProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModProcessors {

    public static final DeferredRegister<StructureProcessorType<?>> REGISTRY =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, SatisCraftory.MODID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<RandomizePropertyProcessor>> RANDOMIZE_PROPERTY =
            REGISTRY.register("randomize_property",
                    () -> explicitProcessorTypeTyping(RandomizePropertyProcessor.CODEC));

    private static <P extends StructureProcessor> StructureProcessorType<P> explicitProcessorTypeTyping(MapCodec<P> codec) {
        return () -> codec;
    }
}
