package org.example;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CustomHashMap<K, V> implements Map<K, V> {

  static final float DEFAULT_LOAD_FACTOR = 0.75f;
  static final int DEFAULT_CAPACITY = 16;

  public CustomEntry[] buckets;

  private int size;

  public CustomHashMap() {
    this.buckets = new CustomEntry[DEFAULT_CAPACITY];
  }

  private int hash(K key) {
    return Math.abs(key.hashCode()) % buckets.length;
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

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

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
  }

  @Override
  public void clear() {
  }

  @Override
  public Set<K> keySet() {
    return new KeySet();
  }

  @Override
  public Collection<V> values() {
    return null;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return null;
  }

  @Override
  public boolean containsKey(Object key) {
    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    return false;
  }

  private void resize() {
    int newCapacity = (buckets.length * 2) + 1;
    CustomEntry[] oldBuckets = buckets;
    buckets = new CustomEntry[newCapacity];
    size = 0;
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


  private class KeySet extends AbstractSet<K> {

    @Override
    public Iterator<K> iterator() {
      return new KeyIterator();
    }

    @Override
    public int size() {
      return CustomHashMap.this.size;
    }
  }

  private class KeyIterator implements Iterator<K> {

    private int index = 0;
    private CustomEntry<K, V> entry = null;

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