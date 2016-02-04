package com.compscieddy.reading_logger;

import com.firebase.client.Firebase;

/**
 * Created by elee on 2/1/16.
 */
public class FirebaseInfo {

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
  }

}
