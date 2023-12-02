package org.example;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        TextPreprocessor tp = new TextPreprocessor();
        List<String> fileList = tp.TextPreprocessor("dataset/");
        WordMap wordMap = new WordMap();

        for (String text : fileList) {
            String fileName = text.substring(0, text.indexOf(" "));
            text = text.substring(text.indexOf(" ") + 1);
            String[] words = text.split(" ");

            int position = 0; // Initialize position counter

            for (String word : words) {
                if (!word.isEmpty()) {
                    word = word.toLowerCase();

                    FileMap fileMap = wordMap.get(word);

                    if (fileMap == null) {
                        fileMap = new FileMap();
                        wordMap.put(word, fileMap);
                    }

                    List<Integer> positions = fileMap.computeIfAbsent(
                        Collections.singletonList(fileName), k -> new ArrayList<>());

                    positions.add(position);
                    position++; // Increment position for the next word
                }
            }
        }

        //print the fileMap
        for (Entry<String, FileMap> entry : wordMap.entrySet()) {
            for (Entry<List<String>, List<Integer>> entry2 : entry.getValue().entrySet()) {
                System.out.println(entry.getKey() + " " + entry2.getKey() + " " + entry2.getValue());
            }
        }

        // Example usage
        String currentWord = "coffee";
        FileMap fileMap = wordMap.get(currentWord);


        System.out.println("The most probable bigram of \"" + currentWord + "\" is \"" + findMostProbableWord(fileMap, currentWord, fileList) + "\"");
    }

    private static String findMostProbableWord(FileMap fileMap, String word, List<String> fileList) {
        List<String> followingWords = new ArrayList<>();
        int fileIndex = 0;
        String[] words = new String[0];
        for (Entry<List<String>, List<Integer>> entry : fileMap.entrySet()) {
            List<Integer> positions = entry.getValue();

            //get the correct text by comparing the file name with the first element of the list
            for (String text : fileList) {
                if (text.startsWith(entry.getKey().get(0))) {
                    words = text.split(" ");
                    break;
                }
                fileIndex++;
            }
                for (Integer position : positions) {
                    followingWords.add(words[position+2]);
                }
                fileIndex++;

                }

        //calculate the probability of each word
        Map<String, Long> wordCount = followingWords.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        //return the most probable word, if there are more then one, return the shortest one
        String mostProbableWord = wordCount.entrySet().stream().max((entry1, entry2) -> {
            if (entry1.getValue() > entry2.getValue()) {
                return 1;
            } else if (entry1.getValue() < entry2.getValue()) {
                return -1;
            } else {
                return entry1.getKey().length() - entry2.getKey().length();
            }
        }).get().getKey();

      return mostProbableWord;
    }




}