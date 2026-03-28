package fr.satiscraftoryteam.satiscraftory.common.worldgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.satiscraftoryteam.satiscraftory.common.registration.ModProcessors;
import fr.satiscraftoryteam.satiscraftory.common.resources.DepositPurityManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

public class RandomizePropertyProcessor extends StructureProcessor {

    public static final MapCodec<RandomizePropertyProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(p -> p.block),
                    Codec.STRING.fieldOf("property").forGetter(p -> p.property),
                    WeightedValue.CODEC.listOf().optionalFieldOf("values", List.of()).forGetter(p -> p.values)
            ).apply(instance, RandomizePropertyProcessor::new)
    );
    private final Block block;
    private final String property;
    private final List<WeightedValue> values;

    public RandomizePropertyProcessor(Block block, String property, List<WeightedValue> values) {
        this.block = block;
        this.property = property;
        this.values = values;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(
            LevelReader level,
            BlockPos pos,
            BlockPos pivot,
            StructureTemplate.StructureBlockInfo original,
            StructureTemplate.StructureBlockInfo current,
            StructurePlaceSettings settings
    ) {
        BlockState state = current.state();

        if (state.is(block)) {

            Property<?> prop = state.getBlock().getStateDefinition().getProperty(property);

            if (prop instanceof IntegerProperty intProp) {
                int value = getRandomValue(settings.getRandom(pos));
                if (intProp.getPossibleValues().contains(value)) {
                    state = state.setValue(intProp, value);
                }
            }

            return new StructureTemplate.StructureBlockInfo(
                    current.pos(),
                    state,
                    current.nbt()
            );
        }

        return current;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.RANDOMIZE_PROPERTY.get();
    }

    private int getRandomValue(RandomSource random) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        if (values.isEmpty()) {
            if ("purity".equals(property)) {
                return DepositPurityManager.getRandomPurity(blockId, random)
                        .orElseThrow(() -> new IllegalStateException("Missing purity config for block " + blockId));
            }

            throw new IllegalStateException("No values configured for property " + property + " on block " + blockId);
        }

        int totalWeight = values.stream().mapToInt(WeightedValue::weight).sum();
        if (totalWeight <= 0) {
            throw new IllegalStateException("RandomizePropertyProcessor requires a positive total weight");
        }

        int target = random.nextInt(totalWeight);
        int cumulative = 0;
        for (WeightedValue weightedValue : values) {
            cumulative += weightedValue.weight();
            if (target < cumulative) {
                return weightedValue.value();
            }
        }

        return values.get(values.size() - 1).value();
    }
}
