package org.example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The BigramFinder class is finds the most probable bigram following a word in a dataset.
 */
public class BigramFinder {

    // Dataset that contains a pair of a file name and the word it contains
    private final List<Pair<String, String[]>> dataset;

    // custom map that stores word occurrences and their positions in the dataset
    private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

    /**
     * Constructs a BigramFinder with the specified dataset and wordmap
     * @param dataset Dataset that contains a pair of a file name and the word it contains
     * @param wordMap Custom map that stores word occurrences and their positions in the dataset
     */
    public BigramFinder(List<Pair<String, String[]>> dataset,
                        CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap) {
        this.dataset = dataset;
        this.wordMap = wordMap;
    }

    /**
     * Finds most probable bigram following a word
     * @param word The word to find its most probable bigram for
     * @return The most probable word following the given word
     */
    String findMostProbableBigram(String word) {
        CustomHashMap<List<String>, List<Integer>> fileMap = wordMap.get(word);
        // verify if the word is in the dataset
        if (fileMap == null) {
            System.err.println("No such word in the dataset");
            return null;
        }

        List<String> followingWords = findFollowingWords(fileMap);

        return findMostProbableWord(followingWords);
    }

    /**
     * Extracts the words following each occurrence of the given word from a dataset
     * @param fileMap Map containing word occurrences and their positions in the dataset
     * @return List of words that follow the occurrences of the given word
     */
    private List<String> findFollowingWords(CustomHashMap<List<String>, List<Integer>> fileMap) {
        List<String> followingWords = new ArrayList<>();

        for (List<String> key : fileMap.keySet()) {
            List<Integer> positions = fileMap.get(key);
            for (Integer position : positions) {
                String fileName = key.get(0);
                Pair<String, String[]> result = dataset.stream()
                        .filter(file -> file.getKey().equals(fileName))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No matching file found"));

                String[] words = result.getValue();
                if (position + 1 < words.length) {
                    followingWords.add(words[position + 1]);
                }
            }
        }

        return followingWords;
    }

    /**
     * Finds most probable word from a list of words based on their frequency
     * @param followingWords List of words to find the most probable word from
     * @return The most probable word
     */
    private String findMostProbableWord(List<String> followingWords) {
        Map<String, Integer> wordCount = followingWords.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.summingInt(e -> 1)));

        if (!wordCount.isEmpty()) {
            int max = wordCount.values().stream().max(Comparator.naturalOrder()).orElse(0);

            return wordCount.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        } else {
            return null;
        }
    }
}