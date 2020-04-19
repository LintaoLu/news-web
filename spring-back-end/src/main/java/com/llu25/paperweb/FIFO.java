package com.llu25.paperweb;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class FIFO<Item> {

    private Map<Integer, Item> map;
    private final int CAPACITY;
    private Queue<Item> queue;
    private int offset, curr;

    public FIFO(int capacity) {
        this.CAPACITY = capacity;
        map = new ConcurrentHashMap<>();
        queue = new LinkedList<>();
    }

    public void set(Integer id, Item item) {
        if (map.containsKey(getId(id))) return;
        if (queue.size() == CAPACITY) {
            queue.poll();
            offset++;
        }
        map.put(++curr, item);
        queue.offer(item);
    }

    public Item get(int id) {
        return map.getOrDefault(getId(id), null);
    }

    private int getId(int id) { return id + offset; }

    @Override
    public String toString() {
        return "FIFO{" +
                "map=" + map +
                ", CAPACITY=" + CAPACITY +
                ", queue=" + queue +
                ", offset=" + offset +
                '}';
    }
}
