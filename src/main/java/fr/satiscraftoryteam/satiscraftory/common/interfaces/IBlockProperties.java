package fr.satiscraftoryteam.satiscraftory.common.interfaces;

import fr.satiscraftoryteam.satiscraftory.common.block.base.Attribute;

import java.util.HashMap;
import java.util.Map;

public interface IBlockProperties {
    final Map<Class<? extends Attribute>, Attribute> attributeMap = new HashMap<>();

    default boolean has(Class<? extends Attribute> type) {
        return attributeMap.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    default <T extends Attribute> T get(Class<T> type) {
        return (T) attributeMap.get(type);
    }

    default void addProperties(Attribute... attrs) {
        for (Attribute attr : attrs) {
            attributeMap.put(attr.getClass(), attr);
        }
    }
}
