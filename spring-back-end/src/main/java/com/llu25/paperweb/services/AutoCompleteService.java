package com.llu25.paperweb.services;

import com.llu25.paperweb.Utils;
import com.llu25.paperweb.datastructures.Tries;
import java.util.List;

public class AutoCompleteService {

    private Tries tries;

    public AutoCompleteService() {
        tries = new Tries(Utils.autocompleteNum);
    }

    public List<String> search(String str) {
        List<String> res = tries.getSuggestions(str);
        if (str.charAt(str.length()-1) == '^') {
            tries.addWord(str.substring(0, str.length()-1));
        }
        return res;
    }

    public static void main(String[] args) {
        AutoCompleteService as = new AutoCompleteService();
        System.out.println(as.search("a"));
    }
}
