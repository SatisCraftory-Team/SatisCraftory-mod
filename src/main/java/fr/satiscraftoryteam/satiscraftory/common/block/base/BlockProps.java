package fr.satiscraftoryteam.satiscraftory.common.block.base;

import java.util.HashMap;
import java.util.Map;

public class BlockProps {

    private final Map<Class<? extends Attribute>, Attribute> attributeMap = new HashMap<>();

    public boolean has(Class<? extends Attribute> type) {
        return attributeMap.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T extends Attribute> T get(Class<T> type) {
        return (T) attributeMap.get(type);
    }

    public void add(Attribute... attrs) {
        for (Attribute attr : attrs) {
            attributeMap.put(attr.getClass(), attr);
        }
    }

}
