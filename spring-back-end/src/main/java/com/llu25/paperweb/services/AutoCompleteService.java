package com.llu25.paperweb.services;

import com.llu25.paperweb.datastructures.Tries;
import com.llu25.paperweb.datastructures.TriesNode;

public class AutoCompleteService {

    private TriesNode currNode;
    private StringBuilder inputBuffer;
    private Tries tries;

    public AutoCompleteService(Tries tries) {
        inputBuffer = new StringBuilder();
        this.tries = tries;
        currNode = tries.getRoot();
    }

    public void searchWords() {

    }
}
