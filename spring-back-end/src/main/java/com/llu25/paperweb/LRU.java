package com.llu25.paperweb;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

// item is a list, each list has at most Utils.newsPerList news
public class LRU<Item1, Item2> {

    private Map<Item1, Item2> map;
    private final int CAPACITY;

    public LRU(int capacity) {
        CAPACITY = capacity;
        map = Collections.synchronizedMap(new LinkedHashMap<Item1, Item2>(capacity, 0.75f, true){
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > CAPACITY;
            }
        });
    }

    public Item2 get(Item1 key) { return map.getOrDefault(key, null); }

    public void set(Item1 key, Item2 value) { map.put(key, value); }

    public boolean containsKey(Item1 item1) { return map.containsKey(item1); }
}
