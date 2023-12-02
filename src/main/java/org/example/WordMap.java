package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
public class WordMap implements Map<String, FileMap> {

  private int capacity = 16;

private static final double LOAD_FACTOR = 0.75;



  private List<WordMapEntry> entries = new ArrayList<>();

  @Override
  public int size() {
    return entries.size();
  }

  @Override
  public boolean isEmpty() {
    return entries.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return entries.stream().anyMatch(entry -> entry.getKey().equals(key));
  }

  @Override
  public boolean containsValue(Object value) {
    return entries.stream().anyMatch(entry -> entry.getValue().equals(value));
  }

@Override
public FileMap get(Object key) {
    return entries.stream()
            .filter(entry -> entry.getKey().equals(key))
            .findFirst()
            .map(Entry::getValue)
            .orElse(null);
}

  @Override
  public FileMap put(String key, FileMap value) {
    WordMapEntry entry = new WordMapEntry(key, value);
    entries.add(entry);
    checkAndResize();
    //System.out.println("new capacity and load factor: " + capacity + " " + ((double) size() / capacity));
    return value;
  }

  @Override
  public FileMap remove(Object key) {
    FileMap valueToRemove = get(key);
    entries.removeIf(entry -> entry.getKey().equals(key));
    return valueToRemove;
  }

  @Override
  public void putAll(Map<? extends String, ? extends FileMap> m) {
    for (Entry<? extends String, ? extends FileMap> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void clear() {
    entries.clear();
  }

  @Override
  public Set<String> keySet() {
    return entries.stream()
      .map(Entry::getKey)
      .collect(Collectors.toSet());
  }

  @Override
  public Collection<FileMap> values() {
    return entries.stream()
      .map(Entry::getValue)
      .collect(Collectors.toSet());
  }

  @Override
  public Set<Entry<String, FileMap>> entrySet() {
    return entries.stream()
      .map(entry -> (Entry<String, FileMap>) entry)
      .collect(Collectors.toSet());
  }


        private void checkAndResize() {
        double loadFactor = (double) size() / capacity;
          if (loadFactor > LOAD_FACTOR) {
            int newCapacity = capacity * 2 + 1;
            List<WordMapEntry> newEntries = new ArrayList<>(newCapacity);
            newEntries.addAll(entries);
            capacity = newCapacity;
            entries = newEntries;
        }


    }









}