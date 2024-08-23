package fr.satiscraftoryteam.satiscraftory.common.registration;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceKey;

public class DeferredMapCodecHolder<R, T extends R> extends WrappedDeferredHolder<MapCodec<? extends R>, MapCodec<T>> {

    protected DeferredMapCodecHolder(ResourceKey<MapCodec<? extends R>> key) {
        super(key);
    }
}