package org.example;

import java.util.Map;

public class WordMapEntry implements Map.Entry<String, FileMap> {
    private final String key;
    private final FileMap value;


    public WordMapEntry(String key, FileMap value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String getKey() {
      return key;
    }

    @Override
    public FileMap getValue() {
      return value;
    }

    @Override
    public FileMap setValue(FileMap value) {
      throw new UnsupportedOperationException("setValue is not supported");
    }

}