package com.compscieddy.reading_logger;

import android.app.Application;

import com.compscieddy.reading_logger.model.ParseBook;
import com.compscieddy.reading_logger.model.ParsePageLog;
import com.firebase.client.Firebase;
import com.firebase.client.Logger;
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

    ParseObject.registerSubclass(ParseBook.class);
    ParseObject.registerSubclass(ParsePageLog.class);

    Parse.enableLocalDatastore(this);
    Parse.initialize(this);
    ParseInstallation.getCurrentInstallation().saveInBackground();

    Firebase.setAndroidContext(this);
    Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
  }
}
