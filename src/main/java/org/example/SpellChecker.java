package org.example;

import java.util.List;

public class SpellChecker {

  private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

  private final List<Pair<String, String[]>> dataset;

  public SpellChecker(List<Pair<String, String[]>> dataset,
      CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap) {
    this.dataset = dataset;
    this.wordMap = wordMap;
  }

  String correctSpelling(String word) {
    int minDistance = Integer.MAX_VALUE;
    String correctedWord = word;

    for (String key : wordMap.keySet()) {

      int distance = findEditDistance(word, key);

      if (distance < minDistance || (distance == minDistance && key.compareTo(correctedWord) < 0)) {
        minDistance = distance;
        correctedWord = key;
      }
    }
    return correctedWord;
  }

  public int findEditDistance(String word1, String word2) {
    int length1 = word1.length();
    int length2 = word2.length();

    int[][] cost = new int[length1 + 1][length2 + 1];

    for (int i = 0; i <= length1; i++) {
      cost[i][0] = i;
    }

    for (int i = 0; i <= length2; i++) {
      cost[0][i] = i;
    }

    for (int i = 0; i < length1; i++) {
      for (int j = 0; j < length2; j++) {
        if (word1.charAt(i) == word2.charAt(j)) {
          cost[i + 1][j + 1] = cost[i][j];
        } else {
          int replace = cost[i][j];
          int delete = cost[i + 1][j];
          int insert = cost[i][j + 1];
          cost[i + 1][j + 1] = Math.min(replace, Math.min(delete, insert)) + 1;
        }
      }
    }
    return cost[length1][length2];
  }

}