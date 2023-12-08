package org.example;

import java.util.List;

public class SpellChecker {

    private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

    public SpellChecker(CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap) {
        this.wordMap = wordMap;
    }

    String correctSpelling(String word) {

      if(wordMap.containsKey(word)) {
        return word;
      }

      return wordMap.keySet().stream()
                .min((key1, key2) -> {
                    int distance1 = findEditDistance(word, key1);
                    int distance2 = findEditDistance(word, key2);
                    if (distance1 == distance2) {
                        return key1.compareTo(key2);
                    } else {
                        return Integer.compare(distance1, distance2);
                    }
                })
                .orElseThrow(() -> new RuntimeException("No matching word found"));
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