package org.example;

import java.util.List;
import java.util.Map;

public class FileMapEntry implements Map.Entry<List<String>, List<Integer>> {
  private final List<String> key;
  private final List<Integer> value;

  public FileMapEntry(List<String> key, List<Integer> value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public List<String> getKey() {
    return key;
  }

  @Override
  public List<Integer> getValue() {
    return value;
  }

  @Override
  public List<Integer> setValue(List<Integer> value) {
    throw new UnsupportedOperationException("setValue is not supported");
  }
}