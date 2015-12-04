package com.compscieddy.reading_logger.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elee on 12/3/15.
 */
public class Book implements Serializable {

  public Book(String title, int currentPageNum, int dayStartedReading, int dayFinishedReading,
              ArrayList<ReadingSession> readingSessionHistory) {
    super();
    this.title = title;
    this.currentPageNum = currentPageNum;
    this.dayStartedReading = dayStartedReading;
    this.dayFinishedReading = dayFinishedReading;
    this.readingSessionHistory = readingSessionHistory;
  }

  public String title;
  public int currentPageNum;

  public int dayStartedReading; // represented as DD+MM+YYYY maybe
  public int dayFinishedReading;

  public ArrayList<ReadingSession> readingSessionHistory;

}
