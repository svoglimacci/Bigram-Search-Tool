package org.example;

import java.util.Map;

/**
 * The CustomEntry class is a custom implementation of Map.Entry that represents a key-value pair
 * @param <K> Type of the key
 * @param <V> Type of the value
 */
public class CustomEntry<K, V> implements Map.Entry<K, V> {

  // The entry key
  public final K key;

  // The entry value
  public V value;

  // Reference to the next entry
  public CustomEntry<K, V> next;

  /**
   * Constructs CustomEntry with specified key, value and next entry
   * @param key The key of the entry
   * @param value The value of the entry
   * @param next Reference to the next entry
   */
  public CustomEntry(K key, V value, CustomEntry<K, V> next) {
    this.key = key;
    this.value = value;
    this.next = next;
  }

  /**
   * Gets key of the entry
   * @return The key of the entry
   */
  @Override
  public K getKey() {
    return key;
  }

  /**
   * Gets value of the entry
   * @return The value of the entry
   */
  @Override
  public V getValue() {
    return value;
  }

  /**
   * Sets value of the entry and returns the old value
   * @param value New value to be stored in this entry
   * @return The old value of the entry
   */
  @Override
  public V setValue(V value) {
    V oldValue = this.value;
    this.value = value;
    return oldValue;
  }
}