package org.example;

/**
 * The Query class represents a query with a specified type and word
 */
public class Query {

  // Type of the query
  private final QueryType queryType;

  // The word of the query
  private final String word;

  /**
   * Constructs a Query with specified query type and word
   * @param queryType the type of the query
   * @param word the word of the query
   */
  public Query(QueryType queryType, String word) {
    this.queryType = queryType;
    this.word = word;
  }

  /**
   * Gets the type of the query
   * @return the type of the query
   */
  public QueryType getQueryType() {
    return queryType;
  }

  /**
   * Gets the word of the query
   * @return the word of the query
   */
  public String getWord() {
    return word;
  }

  /**
   * Enumeration representation of the types of queries
   */
  public enum QueryType {
    MOST_PROBABLE_BIGRAM, SEARCH
  }
}