package com.compscieddy.reading_logger.model;

/**
 * Created by elee on 2/1/16.
 */
public class Book {

  public String owner;
  public String title;
  public int currentPageNum;
  public int maxPageNum;
  public long timestampBookAdded;
  public int timestampStartedReading; // maybe represent it as DD+MM+YYYY
  public int timestampFinishedReading;

  public Book() {}

  public Book(String owner, String title) {
    this.owner = owner;
    this.title = title;
  }

  public String getOwner() {
    return owner;
  }
  public String getTitle() {
    return title;
  }
  public int getCurrentPageNum() {
    return currentPageNum;
  }
  public int getMaxPageNum() {
    return maxPageNum;
  }
  public int getTimestampStartedReading() {
    return timestampStartedReading;
  }
  public int getTimestampFinishedReading() {
    return timestampFinishedReading;
  }


}
