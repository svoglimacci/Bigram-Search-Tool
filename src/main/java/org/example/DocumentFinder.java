package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DocumentFinder {

  private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

  private final List<Pair<String, String[]>> dataset;

  public DocumentFinder(List<Pair<String, String[]>> dataset,
      CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap) {
    this.dataset = dataset;
    this.wordMap = wordMap;
  }

  String findMostRelevantDocument(String word) {
    List<Pair<Pair<String, Integer>, Integer>> results = new ArrayList<>();

    CustomHashMap<List<String>, List<Integer>> fileMap = wordMap.get(word);

    if (fileMap == null) {
      System.out.println("No such word in the dataset");
      return null;
    }

    for (List<String> key : fileMap.keySet()) {
      int count = fileMap.get(key).size();
      String fileName = key.get(0);
      int totalWords = Objects.requireNonNull(
          dataset.stream().filter(result -> result.first.equals(fileName)).findFirst()
              .orElse(null)).second.length;

      if (results.stream().anyMatch(result -> result.first.first.equals(fileName))) {
        results.stream().filter(r -> r.first.first.equals(fileName)).findFirst()
            .ifPresent(result -> result.second += count);
      } else {
        results.add(new Pair<>(new Pair<>(fileName, totalWords), count));
      }
    }

    List<Pair<String, Double>> scores = new ArrayList<>();

    for (Pair<Pair<String, Integer>, Integer> result : results) {

      double TF = (double) result.second / result.first.second;

      double IDF = 1 + Math.log((double) (1 + dataset.size()) / (1 + results.size()));

      double TFIDF = TF * IDF;

      scores.add(new Pair<>(result.first.first, TFIDF));
    }

    scores.sort(Comparator.comparingDouble(o -> o.second));
    scores.sort(Comparator.comparing(o -> o.first));

    return scores.get(scores.size() - 1).first;
  }

}