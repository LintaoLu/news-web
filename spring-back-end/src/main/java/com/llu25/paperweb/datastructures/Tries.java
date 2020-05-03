package com.llu25.paperweb.datastructures;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tries {

    private TriesNode root;
    private int autocompleteNum;

    public Tries(int autocompleteNum) {
        this.autocompleteNum = autocompleteNum;
        root = new TriesNode(autocompleteNum);
    }

    private void loadTires() {}

    public void addWord(String word) {
        TriesNode curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!curr.getNext().containsKey(c)) {
                curr.getNext().put(c, new TriesNode(autocompleteNum));
            }
            curr = curr.getNext().get(c);
            curr.add(word);
        }
    }

    public List<String> getSuggestions(String str) {
        TriesNode triesNode = root;
        for (char c : str.toCharArray()) {
            if (!triesNode.next.containsKey(c)) return new LinkedList<>();
            triesNode = triesNode.next.get(c);
        }
        return triesNode.getWords();
    }

    public TriesNode getRoot() { return root; }

    public static class TriesNode {

        private Map<Character, TriesNode> next;
        private LFU<String, Object> lfu;

        public TriesNode(int suggestionsNum) {
            next = new HashMap<>();
            lfu = new LFU<>(suggestionsNum);
        }

        public Map<Character, TriesNode> getNext() {
            return next;
        }

        public List<String> getWords() {
            return lfu.getKeys();
        }

        public void add(String word) {
            lfu.set(word, null);
        }
    }
}
