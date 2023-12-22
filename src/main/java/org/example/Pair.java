package org.example;

/**
 * The Pair class represents a pair of a key and a value
 * @param <K> The type of the key
 * @param <V> The type of the value
 */
public final class Pair<K, V> {

  // the key of the pair
  private final K key;

  // the value of the pair
  private final V value;

  /**
   * Constructs a Pair with specified key and value
   * @param key the key of the pair
   * @param value the value of the pair
   */
  public Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Gets the key of the pair
   * @return the key of the pair
   */
  public K getKey() {
    return key;
  }

  /**
   * Gets the value of the pair
   * @return the value of the pair
   */
  public V getValue() {
    return value;
  }
}