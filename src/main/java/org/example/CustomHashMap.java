package org.example;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Custom implementation of a HashMap, loosely based on the original Java HashMap implementation
 * (reference: https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/HashMap.java
 * @param <K> The type of the key
 * @param <V> The type of the value
 */
public class CustomHashMap<K, V> implements Map<K, V> {

  // Default load factor
  static final float DEFAULT_LOAD_FACTOR = 0.75f;

  // Default map size
  static final int DEFAULT_CAPACITY = 16;

  // Array of entry buckets
  public CustomEntry[] buckets;

  // Current map size
  private int size;

  /**
   * Constructs CustomHashMap with default map size
   */
  public CustomHashMap() {
    this.buckets = new CustomEntry[DEFAULT_CAPACITY];
  }

  /**
   * Calculates the hash code for a specified key
   * @param key The key to be hash coded
   * @return The hash code for the specified key
   */
  private int hash(K key) {
    return Math.abs(key.hashCode()) % buckets.length;
  }

  /**
   * Returns the number of key-value mappings in the CustomHashMap
   * @return Number of key-value mappings in the CustomHashMap
   */
  @Override
  public int size() {
    return this.size;
  }

    /**
     * Returns true if this CustomHashMap contains no key-value mappings
     * @return true if the CustomHashMap contains no key-value mappings, false otherwise
     */
    @Override
    public boolean isEmpty() {
      return this.size == 0;
    }

  /**
   * Links the specified value with the specified key in the CustomHashMap
   * If the map already contains a mapping for the key, the old value is replaced
   * @param key key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with the specified key
   */
  @Override
  public V put(K key, V value) {

    int index = hash(key);
    CustomEntry entry = buckets[index];

    while (entry != null) {
      if (entry.key.equals(key)) {
        entry.value = value;
        return value;
      }
      entry = entry.next;
    }

    entry = new CustomEntry<>(key, value, buckets[index]);
    buckets[index] = entry;
    size++;

    if ((float) size / buckets.length >= DEFAULT_LOAD_FACTOR) {
      resize();
    }

    return value;
  }

  /**
   * Returns the value to which the specified key is mapped or null if the map has no mapping for the key
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped, or null if the map contains no mapping for the key
   */
  public V get(Object key) {
    int index = hash((K) key);
    CustomEntry entry = buckets[index];

    while (entry != null) {
      if (entry.key.equals(key)) {
        return (V) entry.value;
      }
      entry = entry.next;
    }

    return null;
  }

  /**
   * Removes the mapping for the specified key from the CustomHashMap if it exists
   * @param key key whose mapping is to be removed from the map
   * @return the previous value associated with the specified key
   */
  @Override
  public V remove(Object key) {
    int index = hash((K) key);
    CustomEntry entry = buckets[index];
    if (entry == null) {
      return null;
    }
    if (entry.key.equals(key)) {
      buckets[index] = entry.next;
      size--;
      return (V) entry.value;
    }
    while (entry.next != null) {
      if (entry.next.key.equals(key)) {
        V value = (V) entry.next.value;
        entry.next = entry.next.next;
        size--;
        return value;
      }
      entry = entry.next;
    }
    return null;
  }

  /**
   * This method is not currently implemented.
   *
   * @throws UnsupportedOperationException if the method is called
   */
  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /**
   * This method is not currently implemented.
   *
   * @throws UnsupportedOperationException if the method is called
   */
  @Override
  public void clear() {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /**
   * Returns a set of the keys contained in the CustomHashMap
   * @return a set of keys
   * @see KeySet
   */
  @Override
  public Set<K> keySet() {
    return new KeySet();
  }

  /**
   * This method is not currently implemented.
   *
   * @throws UnsupportedOperationException if the method is called
   */
  @Override
  public Collection<V> values() {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /**
   * This method is not currently implemented.
   *
   * @throws UnsupportedOperationException if the method is called
   */
  @Override
  public Set<Entry<K, V>> entrySet() {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /**
   * This method is not currently implemented.
   *
   * @throws UnsupportedOperationException if the method is called
   */
  @Override
  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  /**
   * This method is not currently implemented.
   *
   * @throws UnsupportedOperationException if the method is called
   */
  @Override
  public boolean containsValue(Object value) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  /**
   * Resizes the array of buckets when the load factor is exceeded.
   * This method is called to maintain an appropriate balance between the number of buckets and the number of mappings
   * in the CustomHashMap.
   */
  private void resize() {
    // Calculates new capacity of the array of buckets
    int newCapacity = (buckets.length * 2) + 1;

    // Store the references to the current buckets array
    CustomEntry[] oldBuckets = buckets;

    // Create new bucket array with augmented capacity
    buckets = new CustomEntry[newCapacity];

    // Reset size to 0
    size = 0;

    // Add each entry from old buckets array to resized buckets array
    for (CustomEntry entry : oldBuckets) {
      if (entry != null) {
        put((K) entry.key, (V) entry.value);
        while (entry.next != null) {
          entry = entry.next;
          put((K) entry.key, (V) entry.value);
        }
      }
    }
  }

  /**
   * Returns a string representation of the CustomHashMap.
   * @return a string representation of the CustomHashMap.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (CustomEntry bucket : buckets) {
      if (bucket != null) {
        sb.append(" ").append(bucket.key).append("=").append(bucket.value).append(" ");
        while (bucket.next != null) {
          bucket = bucket.next;
          sb.append(bucket.key).append("=").append(bucket.value);
        }
      }
    }
    sb.append("]\n");
    return sb.toString();

  }

  /**
   * A view of the keys of the CustomHashMap.
   */
  private class KeySet extends AbstractSet<K> {

    /**
     * Returns an iterator over the keys in the CustomHashMap
     * @return an iterator over the keys in the CustomHashMap
     */
    @Override
    public Iterator<K> iterator() {
      return new KeyIterator();
    }

    /**
     * Returns the number of keys in the CustomHashMap.
     * @return the number of keys in the CustomHashMap.
     */
    @Override
    public int size() {
      return CustomHashMap.this.size;
    }
  }

  /**
   * An iterator over the keys in the CustomHashMap.
   */
  private class KeyIterator implements Iterator<K> {

    // Initial index in the buckets array
    private int index = 0;

    // Current entry in the iteration
    private CustomEntry<K, V> entry = null;

    /**
     * Returns true if the current iteration has a following entry
     * @return true if the current iteration has a following entry
     */
    @Override
    public boolean hasNext() {
      if (entry != null && entry.next != null) {
        return true;
      }
      for (int i = index; i < buckets.length; i++) {
        if (buckets[i] != null) {
          return true;
        }
      }

      return false;
    }

    /**
     * Returns the next key in the iteration
     * @return the next key in the iteration
     */
    @Override
    public K next() {
      if (entry != null && entry.next != null) {
        entry = entry.next;
      } else {
        while (index < buckets.length && buckets[index] == null) {
          index++;
        }
        entry = buckets[index];
        index++;
      }

      return entry.key;
    }

  }

}