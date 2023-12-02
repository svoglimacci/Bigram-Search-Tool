package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileMap implements Map<List<String>, List<Integer>> {


  private final ArrayList<FileMapEntry> entries = new ArrayList<>();

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
  public List<Integer> get(Object key) {
    return entries.stream()
      .filter(entry -> entry.getKey().equals(key))
      .findFirst()
      .map(Entry::getValue)
      .orElse(null);
  }

  @Override
  public List<Integer> put(List<String> key, List<Integer> value) {
    FileMapEntry entry = new FileMapEntry(key, value);
    entries.add(entry);
    return value;
  }

  @Override
  public List<Integer> remove(Object key) {
    List<Integer> valueToRemove = get(key);
    entries.removeIf(entry -> entry.getKey().equals(key));
    return valueToRemove;
  }

  @Override
  public void putAll(Map<? extends List<String>, ? extends List<Integer>> m) {
    for (Entry<? extends List<String>, ? extends List<Integer>> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void clear() {
    entries.clear();
  }

  @Override
  public Set<List<String>> keySet() {
    return entries.stream()
      .map(Entry::getKey)
      .collect(Collectors.toSet());
  }

  @Override
  public Collection<List<Integer>> values() {
    return entries.stream()
      .map(Entry::getValue)
      .collect(Collectors.toList());
  }

  @Override
  public Set<Entry<List<String>, List<Integer>>> entrySet() {
    return entries.stream()
      .map(entry -> (Entry<List<String>, List<Integer>>) entry)
      .collect(Collectors.toSet());
  }





}