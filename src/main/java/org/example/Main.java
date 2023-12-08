package org.example;

import java.io.IOException;
import java.util.List;


public class Main {

  public static void main(String[] args) throws IOException {

    String datasetDir = "dataset/";

    String queryFile = "query.txt";

    TextProcessor textPreprocessor = new TextProcessor();
    List<Pair<String, String[]>> results = textPreprocessor.processFiles(datasetDir);
    QueryProcessor queryProcessor = new QueryProcessor(results, queryFile);

    queryProcessor.processQueries();


  }
}