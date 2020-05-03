package com.llu25.paperweb.services;

import com.llu25.paperweb.Utils;
import com.llu25.paperweb.datastructures.Pair;
import com.llu25.paperweb.datastructures.Tries;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoCompleteService {

    private Map<Integer, Pair<String, Tries.TriesNode>> map;
    private Tries tries;

    public AutoCompleteService() {
        tries = new Tries(Utils.autocompleteNum);
        map = new HashMap<>();
    }

    public List<String> search(Integer searchId, String str) {
        if (!map.containsKey(searchId)) {
            map.put(searchId, new Pair<>("", tries.getRoot()));
        }
        Pair<String, Tries.TriesNode> pair = map.get(searchId);
        List<String> res = tries.getSuggestions(str, pair);
        if (str.charAt(str.length()-1) == '^') {
            tries.addWord(pair.getKey());
            map.put(searchId, new Pair<>("", tries.getRoot()));
        }
        return res;
    }

    public static void main(String[] args) {
        AutoCompleteService as = new AutoCompleteService();
        System.out.println(as.search(1, "a"));
        System.out.println(as.search(1, "a"));
        System.out.println(as.search(1, "a"));
        System.out.println(as.search(1, "a"));
        System.out.println(as.search(1, "a^"));
        System.out.println(as.search(2, "a"));
        System.out.println(as.search(2, "aa^"));
        System.out.println(as.search(3, "a"));
        System.out.println(as.search(3, "aaa"));
    }
}
