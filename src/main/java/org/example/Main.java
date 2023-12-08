package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Main {

  public static void main(String[] args) throws IOException {

    String datasetDir = "dataset/";

    String queryFile = "query.txt";

    String outputFile = "solution.txt";

    TextProcessor textPreprocessor = new TextProcessor();
    List<Pair<String, String[]>> results = textPreprocessor.processFiles(datasetDir);
    QueryProcessor queryProcessor = new QueryProcessor(results, queryFile);

    List<String> solutions = queryProcessor.processQueries();

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
      for (String str : solutions) {

        writer.write(str);
        writer.newLine();
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }
}