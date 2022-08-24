package fr.satiscraftoryteam.satiscraftory.common.block.base.properties;

import fr.satiscraftoryteam.satiscraftory.common.block.base.Attribute;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlockProps {

    final Map<Class<? extends Attribute>, Attribute> attributeMap = new HashMap<>();

    public boolean has(Class<? extends Attribute> type) {
        return attributeMap.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    public <T extends Attribute> T get(Class<T> type) {
        return (T) attributeMap.get(type);
    }

    public void addProperties(Attribute... attrs) {
        for (Attribute attr : attrs) {
            attributeMap.put(attr.getClass(), attr);
        }
    }

    public Collection<Attribute> getAll() {
        return attributeMap.values();
    }


}
