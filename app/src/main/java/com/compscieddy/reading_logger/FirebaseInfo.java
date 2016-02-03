package com.compscieddy.reading_logger;

import com.firebase.client.Firebase;

/**
 * Created by elee on 2/1/16.
 */
public class FirebaseInfo {

  public static final Firebase ref;

  static {
    ref = new Firebase(Constants.FIREBASE_URL);
  }

}
