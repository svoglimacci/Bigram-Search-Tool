package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class BigramFinder {

    private final List<Pair<String, String[]>> dataset;
    private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

    public BigramFinder(List<Pair<String, String[]>> dataset,
                        CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap) {
        this.dataset = dataset;
        this.wordMap = wordMap;
    }

    String findMostProbableBigram(String word) {
        CustomHashMap<List<String>, List<Integer>> fileMap = wordMap.get(word);
        if (fileMap == null) {
            System.err.println("No such word in the dataset");
            return null;
        }

        List<String> followingWords = findFollowingWords(fileMap);

        return findMostProbableWord(followingWords);
    }

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