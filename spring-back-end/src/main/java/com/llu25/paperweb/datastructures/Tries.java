package com.llu25.paperweb.datastructures;

public class Tries {

    private TriesNode root;

    public Tries() { root = new TriesNode(); }

    public void addWord(String word) {
        TriesNode curr = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!curr.getNext().containsKey(c)) {
                curr.getNext().put(c, new TriesNode());
            }
            curr = curr.getNext().get(c);
            curr.getWords().push(word);
        }
    }

    public TriesNode getRoot() { return root; }
}
