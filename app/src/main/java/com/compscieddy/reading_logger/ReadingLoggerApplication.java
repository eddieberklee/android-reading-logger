package com.compscieddy.reading_logger;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by elee on 1/10/16.
 */
public class ReadingLoggerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Firebase.setAndroidContext(this);
//    Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
  }
}
