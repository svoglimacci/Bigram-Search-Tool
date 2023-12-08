package org.example;


public class Query {

  private final QueryType queryType;
  private final String word;
  public Query(QueryType queryType, String word) {
    this.queryType = queryType;
    this.word = word;
  }

  public QueryType getQueryType() {
    return queryType;
  }

  public String getWord() {
    return word;
  }

  public enum QueryType {
    MOST_PROBABLE_BIGRAM, SEARCH
  }
}