package com.llu25.paperweb.services;

import org.apache.commons.lang3.SystemUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KeyWordExtractionService {

    public List<String> getKeyWordsFromLocal(String input) throws IOException {
        String directory = null;
        if (SystemUtils.IS_OS_WINDOWS)
             directory = new File("./").getCanonicalPath() + "\\src\\main\\scripts\\";
        else if (SystemUtils.IS_OS_LINUX)
            directory = new File("./").getCanonicalPath() + "/src/main/scripts/";

        if (directory == null || input == null || input.length() == 0) return new LinkedList<>();

        Runtime rt = Runtime.getRuntime();
        String[] commands = {"python", directory + "KeyWordExtraction.py", input};
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        // Read the output from the command
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = stdInput.readLine()) != null) {
            sb.append(line);
        }

        if (sb.length() == 0) {
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            // Read any errors from the attempted command
            System.out.println("Here is the standard error of the command:\n");
            while ((line = stdError.readLine()) != null) {
                System.out.println(line);
            }
        }
        proc.destroy();

        String[] keyWords = sb.toString().split(",");
        List<String> res = new ArrayList<>();
        for (String str : keyWords) {
            if (!isValidKeyWord(str)) continue;
            res.add(str);
        }
        return res;
    }

    private boolean isValidKeyWord(String keyword) {
        if (keyword == null || keyword.length() == 0) return false;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            if (!Character.isAlphabetic(c) && c != ' ') return false;
        }
        return true;
    }
}