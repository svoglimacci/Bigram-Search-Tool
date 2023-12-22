package org.example;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The TextProcessor class is responsible for preprocessing text files in such that all punctuations are replaced by a
 * space and all multiple spaces by a single space. It is also responsible for NLF text processing each text file.
 */
public class TextProcessor {

  /**
   * The processFiles method processes all text files from a given directory.
   *
   * @param dir The directory path to the text files
   * @return  List of processed files
   * @throws IOException if an input/output error occurs during file processing
   */
  public List<Pair<String, String[]>> processFiles(String dir) throws IOException {

    List<Pair<String, String[]>> results = new ArrayList<>();
    File folder = new File(dir);

    File[] listOfFiles = folder.listFiles();

    // directory folder is empty
    if (listOfFiles == null || listOfFiles.length == 0) {
      System.out.println("No files found in the directory: " + dir);
      return results;
    }

    // process each file
    for (File file : listOfFiles) {
      if (file.isFile()) {
        BufferedReader br;
        try {
          br = new BufferedReader(new FileReader(dir + "/" + file.getName()));
        } catch (FileNotFoundException e) {
          throw new RuntimeException(e);
        }
        StringBuilder word = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
          String newline = line.replaceAll("[^’'a-zA-Z0-9]", " ");
          String finalline = newline.replaceAll("\\s+", " ").trim();

          Properties props = new Properties();

          props.setProperty("annotators", "tokenize,pos,lemma");

          props.setProperty("coref.algorithm", "neural");

          StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

          CoreDocument document = new CoreDocument(finalline);

          pipeline.annotate(document);

          for (CoreLabel tok : document.tokens()) {

            String str = String.valueOf(tok.lemma());
            if (!(str.contains("'s") || str.contains("’s"))) {
              word.append(str).append(" ");
            }
          }
        }
        String str = String.valueOf(word);
        str = str.replaceAll("[^a-zA-Z0-9]", " ").replaceAll("\\s+", " ").trim().toLowerCase();
        String[] words = str.split(" ");

        Pair<String, String[]> text = new Pair<>(file.getName(), words);
        results.add(text);

      }

    }
    return results;
  }
}