package com.compscieddy.reading_logger;

import android.app.Application;

import com.compscieddy.reading_logger.models.Book;
import com.compscieddy.reading_logger.models.PageLog;
import com.firebase.client.Firebase;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by elee on 1/10/16.
 */
public class ReadingLoggerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    ParseObject.registerSubclass(Book.class);
    ParseObject.registerSubclass(PageLog.class);

    Parse.enableLocalDatastore(this);
    Parse.initialize(this);
    ParseInstallation.getCurrentInstallation().saveInBackground();

    Firebase.setAndroidContext(this);
  }
}
