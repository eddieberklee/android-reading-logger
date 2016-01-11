package com.compscieddy.reading_logger;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by elee on 1/10/16.
 */
public class ReadingLoggerApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Parse.enableLocalDatastore(this);
    Parse.initialize(this);
    ParseInstallation.getCurrentInstallation().saveInBackground();
  }
}
