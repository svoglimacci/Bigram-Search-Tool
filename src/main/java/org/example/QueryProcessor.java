package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class QueryProcessor {

    private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;
    private final List<Pair<String, String[]>> dataset;
    private final String queryFile;

    public QueryProcessor(List<Pair<String, String[]>> dataset, String queryFile) {
        this.queryFile = queryFile;
        this.dataset = dataset;
        this.wordMap = new CustomHashMap<>();
        fillWordMap();
    }

    private void fillWordMap() {
        for (Pair<String, String[]> result : dataset) {
            String fileName = result.getKey();
            String[] words = result.getValue();

            int position = 0;

            for (String word : words) {
                if (!word.isEmpty()) {
                    CustomHashMap<List<String>, List<Integer>> fileMap = wordMap.get(word);
                    if (fileMap == null) {
                        fileMap = new CustomHashMap<>();
                        wordMap.put(word, fileMap);
                    }
                    List<Integer> positions = fileMap.computeIfAbsent(Collections.singletonList(fileName),
                            k -> new ArrayList<>());
                    positions.add(position);
                    position++;
                }
            }
        }
    }

    public List<String> processQueries() {
        List<Query> queries = readQueriesFromFile();
        List<String> solutions = new ArrayList<>();

        for (Query query : queries) {
            String solution = processQuery(query);
            if (solution != null) {
                solutions.add(solution);
            }
        }

        return solutions;
    }

    private List<Query> readQueriesFromFile() {
        List<Query> queries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(queryFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                Query query = getQueryType(line);
                queries.add(query);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return queries;
    }

    private String processQuery(Query query) {
        SpellChecker spellChecker = new SpellChecker(wordMap);
        String word = spellChecker.correctSpelling(query.getWord());

      return switch (query.getQueryType()) {
        case MOST_PROBABLE_BIGRAM -> {
          BigramFinder bigramFinder = new BigramFinder(dataset, wordMap);
          yield word + " " + bigramFinder.findMostProbableBigram(word);
        }
        case SEARCH -> {
          DocumentFinder documentFinder = new DocumentFinder(dataset, wordMap);
          yield documentFinder.findMostRelevantDocument(word);
        }
      };
    }

    private Query getQueryType(String query) {
        if (query.startsWith("the most probable bigram of")) {
            return new Query(Query.QueryType.MOST_PROBABLE_BIGRAM, query.substring(28));
        } else if (query.startsWith("search")) {
            return new Query(Query.QueryType.SEARCH, query.substring(7));
        } else {
            throw new RuntimeException("Unknown query type: " + query);
        }
    }
}