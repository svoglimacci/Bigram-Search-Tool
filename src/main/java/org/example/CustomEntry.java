package org.example;

import java.util.Map;

public class CustomEntry<K, V> implements Map.Entry<K, V> {

  public final K key;
  public V value;
  public CustomEntry<K, V> next;

  public CustomEntry(K key, V value, CustomEntry<K, V> next) {
    this.key = key;
    this.value = value;
    this.next = next;
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public V setValue(V value) {
    V oldValue = this.value;
    this.value = value;
    return oldValue;
  }
}