package org.example;

import static org.example.Query.QueryType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryProcessor {

  private final CustomHashMap<String, CustomHashMap<List<String>, List<Integer>>> wordMap;

  private final List<Pair<String, String[]>> dataset;

  private final String queryFile;

  public QueryProcessor(List<Pair<String, String[]>> dataset, String queryFile) {
    this.queryFile = queryFile;
    this.dataset = dataset;
    this.wordMap = new CustomHashMap<>();
    for (Pair<String, String[]> result : dataset) {
      String fileName = result.first;
      String[] words = result.second;

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

  void processQueries() {
    DocumentFinder documentFinder = new DocumentFinder(dataset, wordMap);
    BigramFinder bigramFinder = new BigramFinder(dataset, wordMap);
    SpellChecker spellChecker = new SpellChecker(dataset, wordMap);
    List<Query> queries = new ArrayList<>();
    List<String> solutions = new ArrayList<>();
    String solution = "";
    try (BufferedReader reader = new BufferedReader(new FileReader(queryFile))) {
      String line;

      while ((line = reader.readLine()) != null) {
        Query query = getQueryType(line);
        queries.add(query);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (Query query : queries) {
      String word = spellChecker.correctSpelling(query.getWord());
      solution = switch (query.getQueryType()) {
        case MOST_PROBABLE_BIGRAM -> word + " " + bigramFinder.findMostProbableBigram(word);
        case SEARCH -> documentFinder.findMostRelevantDocument(word);
      };
      solutions.add(solution);
    }

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("solution.txt"));
      for (String str : solutions) {

        writer.write(str);
        writer.newLine();
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private Query getQueryType(String query) {

    if (query.startsWith("the most probable bigram of")) {
      return new Query(QueryType.MOST_PROBABLE_BIGRAM, query.substring(28));
    } else if (query.startsWith("search")) {
      return new Query(QueryType.SEARCH, query.substring(7));
    } else {
      throw new RuntimeException("Unknown query type: " + query);
    }
  }

}