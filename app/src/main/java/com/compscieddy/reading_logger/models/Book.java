package com.compscieddy.reading_logger.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elee on 12/3/15.
 */
@ParseClassName("Book")
public class Book extends ParseObject implements Serializable {

  private static final String TAG = Book.class.getSimpleName();
  private static final String TITLE = "title";

  public int currentPageNum;
  public int dayStartedReading; // represented as DD+MM+YYYY maybe
  public int dayFinishedReading;
  public ArrayList<ReadingSession> readingSessionHistory;

  public Book() {
    super();
  }

  public Book(String title, int currentPageNum, int dayStartedReading, int dayFinishedReading,
              ArrayList<ReadingSession> readingSessionHistory) {
    super();
    this.currentPageNum = currentPageNum;
    this.dayStartedReading = dayStartedReading;
    this.dayFinishedReading = dayFinishedReading;
    this.readingSessionHistory = readingSessionHistory;
  }

  public String getTitle() { return getString(TITLE); }
  public void setTitle(String title) { put(TITLE, title); }

  public ParseQuery getCurrentPageNumQuery() {
    ParseQuery<PageLog> query = PageLog.getQuery();
    query.whereEqualTo(Book.class.getSimpleName(), this);
    query.orderByDescending("createdAt");
    query.setLimit(1); // most recent
    return query;
  }

  public static ParseQuery<Book> getQuery() { return ParseQuery.getQuery(Book.class); }

}
