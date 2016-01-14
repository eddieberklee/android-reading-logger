package com.compscieddy.reading_logger.models;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elee on 12/3/15.
 */
@ParseClassName("Book")
public class Book extends ParseObject implements Serializable {

  private static final String TAG = Book.class.getSimpleName();

  public static final String BOOK_ID_EXTRA = "book_id_extra"; // instead of passing ParseObject, just pass the id since Parse says they cache the object anyway
  private static final String TITLE = "title";
  private static final String MAX_PAGE_NUM = "max_page_num";

  public int currentPageNum;
  public int maxPageNum;

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
  public int getMaxPageNum() { return getInt(MAX_PAGE_NUM); }
  public void setMaxPageNum(int maxPageNum) { put(MAX_PAGE_NUM, maxPageNum); }

  public ParseQuery getCurrentPageNumQuery() {
    ParseQuery<PageLog> query = PageLog.getQuery();
    query.whereEqualTo(Book.class.getSimpleName(), this);
    query.orderByDescending("createdAt");
    query.setLimit(1); // most recent
    return query;
  }

  public void setTextViewCurrentPageNumToView(final TextView view) {
    ParseQuery<PageLog> query = getCurrentPageNumQuery();
    query.findInBackground(new FindCallback<PageLog>() {
      @Override
      public void done(List<PageLog> objects, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error getting current page number", e);
          return;
        }
        if (objects == null) return;
        if (objects.size() > 0) {
          int currentPageNum = (objects.get(0)).getPageNum();
          view.setText(String.valueOf(currentPageNum));
        }
      }
    });
  }

  public void setEditTextCurrentPageNumToView(final EditText view) {
    ParseQuery<PageLog> query = getCurrentPageNumQuery();
    query.findInBackground(new FindCallback<PageLog>() {
      @Override
      public void done(List<PageLog> objects, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error getting current page number", e);
          return;
        }
        if (objects == null) return;
        if (objects.size() > 0) {
          int currentPageNum = (objects.get(0)).getPageNum();
          Log.d(TAG, "createdAt:" + (objects.get(0)).getCreatedAt());
          view.setText(String.valueOf(currentPageNum));
          view.setSelection(String.valueOf(currentPageNum).length());
        }
      }
    });
  }

  public static ParseQuery<Book> getQuery() { return ParseQuery.getQuery(Book.class); }

}
