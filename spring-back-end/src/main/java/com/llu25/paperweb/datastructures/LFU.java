package com.llu25.paperweb.datastructures;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LFU<Item1, Item2> {

    private static class Node<Item1, Item2> {
        Item1 key;
        Item2 val;
        int freq;
        Node<Item1, Item2> prev, next;

        public Node(Item1 k, Item2 v) {
            key = k;
            val = v;
            freq = 0;
            prev = next = this;
        }

        public void remove() {
            prev.next = next;
            next.prev = prev;
        }

        public void addBefore(Node<Item1, Item2> head) {
            prev = head.prev;
            prev.next = this;
            next = head;
            head.prev = this;
        }
    }

    private HashMap<Item1, Node<Item1, Item2>> nodeMap = new HashMap<>();
    private HashMap<Integer, Node<Item1, Item2>> freqMap = new HashMap<>();
    private int minFreq, cap;

    public LFU(int capacity) {
        cap = capacity;
    }

    public Item2 get(Item1 key) {
        Node<Item1, Item2> cur = nodeMap.get(key);
        if (cur == null) return null;

        removeNode(cur);
        addNode(cur);

        return cur.val;
    }

    public void set(Item1 key, Item2 value) {
        if (cap == 0) return;

        Node<Item1, Item2> cur = nodeMap.get(key);

        if (cur != null) {
            cur.val = value;
            removeNode(cur);
        } else {
            if (nodeMap.size() == cap) {
                Node<Item1, Item2> head = freqMap.get(minFreq);
                nodeMap.remove(head.next.key);
                removeNode(head.next);
            }

            cur = new Node<>(key, value);
            minFreq = 1;
            nodeMap.put(key, cur);
        }
        addNode(cur);
    }

    public List<Item1> getKeys() {
        return new LinkedList<>(nodeMap.keySet());
    }

    private void removeNode(Node <Item1, Item2>cur) {
        Node<Item1, Item2> nextNode = cur.next;

        cur.remove();
        if (nextNode.next == nextNode) {
            freqMap.remove(cur.freq);
            if (minFreq == cur.freq)
                minFreq++;
        }
    }

    private void addNode(Node<Item1, Item2> cur) {
        cur.freq++;

        Node<Item1, Item2> head = freqMap.get(cur.freq);
        if (head == null) {
            head = new Node<>(null, null);
            freqMap.put(cur.freq, head);
        }
        cur.addBefore(head);
    }
}
