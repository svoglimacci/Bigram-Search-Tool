package org.example;

//custom Pair class
public class Pair<T, U> {

  public final T first;
  public U second;

  public Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }
}