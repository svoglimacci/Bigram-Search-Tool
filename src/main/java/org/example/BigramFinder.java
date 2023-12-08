package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    String mostProbableWord;
    CustomHashMap<List<String>, List<Integer>> fileMap = wordMap.get(word);
    if (fileMap == null) {
      System.out.println("No such word in the dataset");
      return null;
    }

    List<String> followingWords = new ArrayList<>();

    for (List<String> key : fileMap.keySet()) {
      List<Integer> positions = fileMap.get(key);
      for (Integer position : positions) {
        String fileName = key.get(0);
        Pair<String, String[]> result = dataset.stream().filter(file -> file.first.equals(fileName))
            .findFirst().orElse(null);

        if (result != null) {
          String[] words = result.second;
          if (position + 1 < words.length) {
            followingWords.add(words[position + 1]);
          }
        }
      }
    }

    Map<String, Integer> wordCount = followingWords.stream()
        .collect(Collectors.groupingBy(e -> e, Collectors.summingInt(e -> 1)));

    if (!wordCount.isEmpty()) {
      mostProbableWord = wordCount.keySet().stream().max(Comparator.comparingLong(wordCount::get))
          .orElse(null);
    } else {
      return null;
    }

    return mostProbableWord;
  }

}