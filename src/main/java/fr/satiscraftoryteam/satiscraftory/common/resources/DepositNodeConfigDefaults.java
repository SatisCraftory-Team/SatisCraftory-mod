package fr.satiscraftoryteam.satiscraftory.common.resources;

import fr.satiscraftoryteam.satiscraftory.SatisCraftory;
import fr.satiscraftoryteam.satiscraftory.common.init.BlockInit;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public record DepositNodeConfigDefaults(ResourceLocation blockId, String configNode,
                                        Map<Integer, Integer> purityWeights) {
    public static List<DepositNodeConfigDefaults> defaults() {
        return List.of(
                new DepositNodeConfigDefaults(
                        ResourceLocation.fromNamespaceAndPath(SatisCraftory.MODID, "iron_resource_node"),
                        BlockInit.IRON_RESOURCE_NODE.getId().toString(),
                        Map.of(1, 50, 2, 35, 3, 15)
                )
        );
    }
}
