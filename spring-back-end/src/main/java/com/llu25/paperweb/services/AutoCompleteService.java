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
        return tries.getSuggestions(str);
    }

    public void addWord(String str) {
        tries.addWord(str);
    }
}
