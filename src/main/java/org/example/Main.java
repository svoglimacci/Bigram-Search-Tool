package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    private static final String DATASET_DIR = "dataset/";
    private static final String QUERY_FILE = "query.txt";
    private static final String OUTPUT_FILE = "solution.txt";

    public static void main(String[] args) throws IOException {
        TextProcessor textPreprocessor = new TextProcessor();
        List<Pair<String, String[]>> results = textPreprocessor.processFiles(DATASET_DIR);
        QueryProcessor queryProcessor = new QueryProcessor(results, QUERY_FILE);

        List<String> solutions = queryProcessor.processQueries();

        writeSolutionsToFile(solutions);
    }

    private static void writeSolutionsToFile(List<String> solutions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            for (String solution : solutions) {
                writer.write(solution);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write solutions to file", e);
        }
    }
}