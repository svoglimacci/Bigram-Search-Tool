package org.example;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        TextPreprocessor tp = new TextPreprocessor();
        List<String> fileList = tp.TextPreprocessor("dataset/");


        for (String text : fileList) {
    // set fileName to the first item of the text String
    String fileName = text.substring(0, text.indexOf(" "));
    // remove the fileName from the text String
    text = text.substring(text.indexOf(" ") + 1);
    // split the text String into an array of words
    String[] words = text.split(" ");

    // print the processed text
          System.out.println(text + "\n");

    for (int i = 0; i < words.length; i++) {
        String word = words[i];
       //TODO
        }
    }
}

}