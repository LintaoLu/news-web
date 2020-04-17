package com.llu25.paperweb;

import java.util.LinkedHashMap;
import java.util.Map;

// item is a list, each list has at most 5 news
public class LRU<Item> {

    private LinkedHashMap<Integer, Item> map;
    private final int CAPACITY;

    public LRU(int capacity) {
        CAPACITY = capacity;
        map = new LinkedHashMap<Integer, Item>(capacity, 0.75f, true){
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > CAPACITY;
            }
        };
    }

    public Item get(int key) { return map.getOrDefault(key, null); }

    public void set(int key, Item value) { map.put(key, value); }

    @Override
    public String toString() {
        return "LRU{" +
                "map=" + map +
                ", CAPACITY=" + CAPACITY +
                '}';
    }
}
