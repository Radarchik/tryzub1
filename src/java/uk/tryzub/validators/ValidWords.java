/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.validators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author tszin
 */
public class ValidWords {

    public HashSet <String> h = new HashSet();

    public ValidWords(){
        //String fileName = "/home/3zub/DataForTryzub/wordlist/words.txt";
        String fileName = "F:/noun1.txt";
        try {
            Path path = Paths.get(fileName);
            try (Scanner scanner = new Scanner(path, "UTF-8")) {
                String inputLine;
                while (scanner.hasNextLine()) {
                    inputLine = new String(scanner.next().getBytes("UTF-8"), Charset.forName("UTF-8"));
                    h.add(inputLine);                                
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean contains(String word) {
        return h.contains(word);
    }

    public int size() {
        return h.size();
    }
}
