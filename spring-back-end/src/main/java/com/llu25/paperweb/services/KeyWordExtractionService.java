package com.llu25.paperweb.services;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class KeyWordExtractionService {

    public static void main(String[] args) throws IOException {
        List<String> list = KeyWordExtractionService.getKeyWords("The Mysterious Case Of Customs And Border Protection's Unmarked S-76 Helicopters");
        System.out.println(list);
    }

    public static List<String> getKeyWords(String input) throws IOException {
        String directory = new File("./").getCanonicalPath() + "\\src\\main\\scripts\\";

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

        String[] keyWords = sb.toString().split(",");
        return new LinkedList<>(Arrays.asList(keyWords));
    }
}