package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The Main class is responsible for text processing and query execution
 */
public class Main {

    // Directory of the dataset
    private static final String DATASET_DIR = "dataset/";

    // File containing the queries
    private static final String QUERY_FILE = "query.txt";

    // File in which the solutions are written
    private static final String OUTPUT_FILE = "solution.txt";

    /**
     * Processes text files in the dataset, executes queries and writes the solutions in an output file
     * @param args
     * @throws IOException if an I/O error occurs during file processing
     */
    public static void main(String[] args) throws IOException {
        TextProcessor textPreprocessor = new TextProcessor();
        List<Pair<String, String[]>> results = textPreprocessor.processFiles(DATASET_DIR);
        QueryProcessor queryProcessor = new QueryProcessor(results, QUERY_FILE);

        List<String> solutions = queryProcessor.processQueries();

        writeSolutionsToFile(solutions);
    }

    /**
     * Writes a list of solutions to a file
     * @param solutions List of solutions to be written
     */
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