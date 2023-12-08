package org.example;
import java.util.*;

public class DocumentFinder {

    private final List<Pair<String, String[]>> dataset;
    private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

    public DocumentFinder(List<Pair<String, String[]>> dataset,
                          CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap) {
        this.dataset = dataset;
        this.wordMap = wordMap;
    }

    String findMostRelevantDocument(String word) {
    Map<String, Integer> results = new HashMap<>();

    CustomHashMap<List<String>, List<Integer>> fileMap = wordMap.get(word);
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

    private List<Pair<String, Double>> calculateScores(Map<String, Integer> results) {
    List<Pair<String, Double>> scores = new ArrayList<>();

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