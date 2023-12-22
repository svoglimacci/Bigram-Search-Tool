package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The QueryProcessor class processes queries on a dataset
 */
public class QueryProcessor {

  // Map that stores word occurrences and their positions in the dataset
  private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

  // Dataset containing pairs of file names and their corresponding words
  private final List<Pair<String, String[]>> dataset;

  // Query file path
  private final String queryFile;

  /**
   * Contructor of the class QueryProcessor
   *
   * @param dataset Dataset to be processed
   * @param queryFile Path to query file
   */
  public QueryProcessor(List<Pair<String, String[]>> dataset, String queryFile) {
    this.queryFile = queryFile;
    this.dataset = dataset;
    this.wordMap = new CustomHashMap<>();
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

    /**
     * Processes a list of queries and returns a list of solutions
     * @return a list of solutions for the processed queries
     */
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

    /**
     * Reads queries from the specified file and returns a list of Query objects
     * @return a list of Query objects
     */
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

    /**
     * Processes a given Query object and returns the solution based on the query type
     * @param query the Query object to be processed
     * @return the solution for the processed query
     */
    private String processQuery(Query query) {
        SpellChecker spellChecker = new SpellChecker(wordMap);
        String word = spellChecker.correctSpelling(query.getWord());

      return switch (query.getQueryType()) {
        case MOST_PROBABLE_BIGRAM -> {
            // For MOST_PROBABLE_BIGRAM queries, create a BigramFinder instance
            BigramFinder bigramFinder = new BigramFinder(dataset, wordMap);
          yield word + " " + bigramFinder.findMostProbableBigram(word);
        }
        case SEARCH -> {
            // For SEARCH queries, create a DocumentFinder instance
            DocumentFinder documentFinder = new DocumentFinder(dataset, wordMap);
          yield documentFinder.findMostRelevantDocument(word);
        }
      };
    }

    /**
     * Parses a query string and determines its type to create a Query object and returns it
     * @param query a query string to be parsed
     * @return a Query object representing the parsed query
     */
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