package org.example;
import java.util.*;

/**
 * The DocumentFinder class is responsible for finding the most relevant document to a given word
 * based on its occurrences in the dataset
 */
public class DocumentFinder {

    // Dataset containing pairs of file names and their corresponding words
    private final List<Pair<String, String[]>> dataset;

    // CustomHashMap storing word occurrences and their positions in the dataset
    private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

    /**
     * Constructs a DocumentFinder with specified dataset and word map
     * @param dataset the dataset containing pairs of file names and their corresponding words
     * @param wordMap the CustomHashMap storing word occurrences and their positions in the dataset
     */
    public DocumentFinder(List<Pair<String, String[]>> dataset,
                          CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap) {
        this.dataset = dataset;
        this.wordMap = wordMap;
    }

    /**
     * Finds the most relevant document for a given word based on its occurrences in the dataset
     * @param word the word for which the most relevant document is to be found
     * @return the name of the most relevant document
     */
    String findMostRelevantDocument(String word) {
        Map<String, Integer> results = new HashMap<>();

        CustomHashMap<List<String>, List<Integer>> fileMap = wordMap.get(word);

        // verify if the word is contained in the dataset
        if (fileMap == null) {
            System.err.println("No such word in the dataset");
            return null;
        }

        for (List<String> key : fileMap.keySet()) {
            int count = fileMap.get(key).size();
            String fileName = key.get(0);

            results.merge(fileName, count, Integer::sum);
        }

        List<Pair<String, Double>> scores = calculateScores(results);

        return scores.stream()
                .max(Comparator.comparing(Pair::getValue))
                .map(Pair::getKey)
                .orElse(null);
    }

    /**
     * Calculates the TFIDF scores for each document based on the given word occurrences count
     * @param results a map containing document names and their corresponding word occurrences
     * @return a list of pairs of a document name and its corresponding TFIDF
     */
    private List<Pair<String, Double>> calculateScores(Map<String, Integer> results) {
        List<Pair<String, Double>> scores = new ArrayList<>();

        // iterate each document from the map to calculate its TFIDF
        for (Map.Entry<String, Integer> result : results.entrySet()) {
            double TF = (double) result.getValue() / dataset.stream()
                    .filter(pair -> pair.getKey().equals(result.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No matching file found"))
                    .getValue().length;

            double IDF = 1 + Math.log((double) (1 + dataset.size()) / (1 + results.size()));

            double TFIDF = TF * IDF;

            scores.add(new Pair<>(result.getKey(), TFIDF));
        }

        return scores;
    }
}