package org.example;

import java.util.List;

/**
 * The SpellChecker class uses a custom word map to allow spelling corrections based on edit distance
 */
public class SpellChecker {

    // the custom word map storing word occurrences and their positions
    private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

    /**
     * Constructs a SpellChecker with a specified word map
     * @param wordMap the word map containing word occurrences and their positions
     */
    public SpellChecker(CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap) {
        this.wordMap = wordMap;
    }

    /**
     * Corrects the spelling of a word using the word map
     * @param word the word to be corrected
     * @return the corrected word
     */
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

    /**
     * Calculates the edit distance between two words using dynamic programming
     * @param word1 the first word
     * @param word2 the second word
     * @return the edit distance between the two words
     */
    public int findEditDistance(String word1, String word2) {
        int length1 = word1.length();
        int length2 = word2.length();

        // Creates 2D array to store the edit distances
        int[][] cost = new int[length1 + 1][length2 + 1];

        for (int i = 0; i <= length1; i++) {
            cost[i][0] = i;
        }

        for (int j = 0; j <= length2; j++) {
            cost[0][j] = j;
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