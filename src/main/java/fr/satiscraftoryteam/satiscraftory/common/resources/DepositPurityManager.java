package fr.satiscraftoryteam.satiscraftory.common.resources;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.worldgen.processor.WeightedValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = SatisCraftory.MODID)
public class DepositPurityManager {
    private static final String CONFIG_FILE_NAME = "wordgen.toml";
    private static final String PURITY_WEIGHTS = "purity_weights";
    private static final Map<ResourceLocation, List<WeightedValue>> PURITY_VALUES = new ConcurrentHashMap<>();

    private static CommentedFileConfig config;

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        load(event.getServer());
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        PURITY_VALUES.clear();
        WorldConfigFile.close(config);
        config = null;
    }

    private static void load(MinecraftServer server) {
        PURITY_VALUES.clear();

        WorldConfigFile.close(config);
        config = WorldConfigFile.openToml(server, SatisCraftory.MODID, CONFIG_FILE_NAME);

        ensureDefaults();
        config.save();

        for (DepositNodeConfigDefaults defaults : DepositNodeConfigDefaults.defaults()) {
            PURITY_VALUES.put(defaults.blockId(), readWeights(defaults));
        }
    }

    private static void ensureDefaults() {
        for (DepositNodeConfigDefaults defaults : DepositNodeConfigDefaults.defaults()) {
            setDefaultWeights(defaults);
        }
    }

    private static void setDefaultWeights(DepositNodeConfigDefaults defaults) {
        config.setComment(List.of(defaults.configNode()), "Worldgen settings for " + defaults.configNode() + ".");
        config.setComment(List.of(defaults.configNode(), PURITY_WEIGHTS), "Relative weights used to randomize the purity property.");

        for (Map.Entry<Integer, Integer> entry : defaults.purityWeights().entrySet()) {
            List<String> path = List.of(defaults.configNode(), PURITY_WEIGHTS, Integer.toString(entry.getKey()));
            if (!config.contains(path)) {
                config.set(path, entry.getValue());
            }
            config.setComment(path, "Purity level " + entry.getKey() + " weight.");
        }
    }

    private static List<WeightedValue> readWeights(DepositNodeConfigDefaults defaults) {
        return defaults.purityWeights().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new WeightedValue(
                        entry.getKey(),
                        config.getIntOrElse(
                                List.of(defaults.configNode(), PURITY_WEIGHTS, Integer.toString(entry.getKey())),
                                entry.getValue()
                        )
                ))
                .toList();
    }

    public static Optional<Integer> getRandomPurity(ResourceLocation blockId, RandomSource random) {
        List<WeightedValue> values = PURITY_VALUES.get(blockId);
        if (values == null || values.isEmpty()) {
            return Optional.empty();
        }

        int totalWeight = values.stream().mapToInt(WeightedValue::weight).sum();
        if (totalWeight <= 0) {
            SatisCraftory.LOGGER.warn("Ignoring purity config {} because total weight is not positive", blockId);
            return Optional.empty();
        }

        int target = random.nextInt(totalWeight);
        int cumulative = 0;
        for (WeightedValue weightedValue : values) {
            cumulative += weightedValue.weight();
            if (target < cumulative) {
                return Optional.of(weightedValue.value());
            }
        }

        return Optional.of(values.get(values.size() - 1).value());
    }
}
