package com.llu25.paperweb;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FIFO<Item> {

    private Map<Integer, Item> map;
    private final int CAPACITY;
    private LinkedList<Item> list;

    public FIFO(int capacity) {
        this.CAPACITY = capacity;
        map = new ConcurrentHashMap<>();
        list = new LinkedList<>();
    }

    public void append(Map<Integer, Item> news) {
        LinkedList<Item> temp = new LinkedList<>();
        for (Map.Entry<Integer, Item> entry : news.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
            temp.add(entry.getValue());
        }
        int offset = temp.size() + 1;
        for (int i = 0; i < list.size(); i++) {
            int currId = i + offset;
            if (currId > CAPACITY) break;
            Item item = list.get(i);
            map.put(currId, item);
            temp.add(item);
        }
        list = temp;
    }

    public Item get(int id) {
        return map.getOrDefault(id, null);
    }

    public Map<Integer, Item> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "FIFO{" +
                "map=" + map +
                ", CAPACITY=" + CAPACITY +
                ", list=" + list +
                '}';
    }
}