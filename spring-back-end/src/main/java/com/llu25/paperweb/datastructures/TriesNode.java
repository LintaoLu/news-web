package com.llu25.paperweb.datastructures;

import java.util.HashMap;
import java.util.Map;

public class TriesNode {

    private Map<Character, TriesNode> next;
    private SizedStack<String> words;

    public TriesNode() {
        next = new HashMap<>();
        words = new SizedStack<>(10);
    }

    public Map<Character, TriesNode> getNext() {
        return next;
    }

    public SizedStack<String> getWords() {
        return words;
    }
}

