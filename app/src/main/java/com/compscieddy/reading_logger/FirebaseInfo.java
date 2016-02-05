package com.compscieddy.reading_logger;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;

/**
 * Created by elee on 2/1/16.
 */
public class FirebaseInfo {

  private static final String TAG = FirebaseInfo.class.getSimpleName();

  public static final Firebase ref;
  public static Firebase userRef;
  public static Firebase booksRef;
  public static Firebase userBooksMappingsRef;
  public static Firebase pageLogsRef;

  static {
    ref = new Firebase(Constants.FIREBASE_URL);
    booksRef = new Firebase(Constants.FIREBASE_URL_BOOKS);
    pageLogsRef = new Firebase(Constants.FIREBASE_URL_PAGELOGS);
  }

  public static void initialize(String encodedEmail) {
    userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(encodedEmail);
    userBooksMappingsRef = userRef.child(Constants.FIREBASE_LOCATION_USER_TO_BOOK_MAPPINGS);


    booksRef.addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.e(TAG, "onChildRemoved() dataSnapshot: " + dataSnapshot);
        // Delete the user mapping as well as the pagelogs
        String bookKey = dataSnapshot.getKey();
        HashMap<String, Object> deletedBookMap = (HashMap<String, Object>) dataSnapshot.getValue();
        HashMap<String, Object> pageLogKeys = (HashMap<String, Object>) deletedBookMap.get(Constants.FIREBASE_LOCATION_BOOK_TO_PAGE_LOG_MAPPINGS);
        if (pageLogKeys != null) {
          for (String pageLogKey : pageLogKeys.keySet()) {
            // Delete PageLog
            FirebaseInfo.pageLogsRef.child(pageLogKey).removeValue();
          }
        }
        // Delete User-to-Book Mapping
        FirebaseInfo.userRef.child(Constants.FIREBASE_LOCATION_USER_TO_BOOK_MAPPINGS)
          .child(bookKey).removeValue();
      }
      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
      @Override
      public void onCancelled(FirebaseError firebaseError) {}
    });


  }

}
