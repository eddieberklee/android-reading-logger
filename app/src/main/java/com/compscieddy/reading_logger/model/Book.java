package com.compscieddy.reading_logger.model;

import com.compscieddy.reading_logger.Constants;
import com.compscieddy.reading_logger.FirebaseInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by elee on 2/1/16.
 */
@JsonIgnoreProperties({
    "pageLogMappings"
})
public class Book {

  public static final String BOOK_KEY_EXTRA = "book_id_extra"; // instead of passing ParseObject, just pass the id since Parse says they cache the object anyway
  private String key;
  private String owner;
  private String title;
  private int currentPageNum; // todo: may not be worth it to have a field, just query for it instead
  private int maxPageNum;
  private HashMap<String, Object> timestampBookAdded;
  private HashMap<String, Object> timestampStartedReading; // maybe represent it as DD+MM+YYYY
  private HashMap<String, Object> timestampFinishedReading;

  public Book() {}

  public Book(String key, String owner, String title) {
    this.key = key;
    this.owner = owner;
    this.title = title;
  }

  public String getKey() {
    return key;
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

  public HashMap<String, Object> getTimestampBookAdded() {
    return timestampBookAdded;
  }

  public HashMap<String, Object> getTimestampStartedReading() {
    return timestampStartedReading;
  }

  public HashMap<String, Object> getTimestampFinishedReading() {
    return timestampFinishedReading;
  }


  public static void addCurrentPageNumberListener(String bookKey, final ValueEventListener listener) {
    FirebaseInfo.booksRef.child(bookKey).child(Constants.FIREBASE_LOCATION_BOOK_TO_PAGE_LOG_MAPPINGS)
        .orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();
        if (key != null) {
          FirebaseInfo.pageLogsRef.child(key).addListenerForSingleValueEvent(listener);
        }
      }

      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {

      }

      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {

      }
    });
  }


}
