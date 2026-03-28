package fr.satiscraftoryteam.satiscraftory.common.worldgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WeightedValue(int value, int weight) {

    public static final Codec<WeightedValue> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("value").forGetter(WeightedValue::value),
                    Codec.INT.fieldOf("weight").forGetter(WeightedValue::weight)
            ).apply(instance, WeightedValue::new)
    );
}