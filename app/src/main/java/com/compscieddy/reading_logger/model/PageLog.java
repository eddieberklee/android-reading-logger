package com.compscieddy.reading_logger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

/**
 * Created by elee on 2/3/16.
 */
public class PageLog {

  private String key;
  private int pageNumber;
  public static final String PAGE_NUMBER = "pageNumber";
  private HashMap<String, Object> timestampCreated;

  public PageLog() {
  }

  public PageLog(String key, int pageNumber, HashMap<String, Object> timestampCreated) {
    this.key = key;
    this.pageNumber = pageNumber;
    this.timestampCreated = timestampCreated;
  }

  public String getKey() {
    return key;
  }
  public int getPageNumber() {
    return pageNumber;
  }
  public HashMap<String, Object> getTimestampCreated() {
    return timestampCreated;
  }

  @JsonIgnore
  public long getTimestampCreatedLong() {
    return (long) timestampCreated.get("timestamp");
  }
}
