package fr.satiscraftoryteam.satiscraftory.common.init;

import com.mojang.serialization.Codec;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentTypeInit {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SatisCraftory.MODID);

    private static final Supplier<AttachmentType<Integer>> OVERCLOCK_PERCENTAGE = ATTACHMENT_TYPES.register(
            "overclock_percentage", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static void register(IEventBus bus)  {
        ATTACHMENT_TYPES.register(bus);
    }
}
