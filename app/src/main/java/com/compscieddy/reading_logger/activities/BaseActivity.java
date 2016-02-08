package com.compscieddy.reading_logger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.compscieddy.reading_logger.Constants;
import com.compscieddy.reading_logger.FirebaseInfo;
import com.compscieddy.reading_logger.Util;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by elee on 2/2/16.
 */
public class BaseActivity extends AppCompatActivity {

  private static final String TAG = BaseActivity.class.getSimpleName();

  protected Firebase.AuthStateListener mAuthListener;
  protected String mEncodedEmail;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (FirebaseInfo.ref.getAuth() == null) { // user not logged in
      Log.d(TAG, "User not logged in");
      if (!(this instanceof AuthenticationActivity)) {
        Intent intent = new Intent(BaseActivity.this, AuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    } else {
      mEncodedEmail = Util.encodeEmail((String) FirebaseInfo.ref.getAuth().getProviderData().get("email"));
      Log.d(TAG, "User logged in as " + mEncodedEmail);
    }

    mAuthListener = new Firebase.AuthStateListener() {
      @Override
      public void onAuthStateChanged(AuthData authData) {
        if (authData == null) { // logged out
          Log.e(TAG, "User logged out");
          logout();
        } else {
          Log.e(TAG, "User already logged in");
        }
      }
    };

    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);

//    mEncodedEmail = sp.getString(Constants.KEY_ENCODED_EMAIL, null);

    if (!TextUtils.isEmpty(mEncodedEmail)) { // todo: this should only be called once, right?
      FirebaseInfo.initialize(mEncodedEmail);
    }

  }

  private void loginWithToken(String authToken) {
    FirebaseInfo.ref.authWithCustomToken(authToken, new Firebase.AuthResultHandler() {
      @Override
      public void onAuthenticated(AuthData authData) {
        Log.e(TAG, "Authenticated");

        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }

      @Override
      public void onAuthenticationError(FirebaseError firebaseError) {
        Log.e(TAG, "Authentication Failed!");
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (!(this instanceof AuthenticationActivity)) {
      FirebaseInfo.ref.removeAuthStateListener(mAuthListener);
    }
  }

  protected void logout() {
    FirebaseInfo.ref.unauth();

    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
    SharedPreferences.Editor spe = sp.edit();
    spe.putString(Constants.KEY_ENCODED_EMAIL, null);
    spe.putString(Constants.KEY_PROVIDER, null);
    spe.apply();

    if (!(this instanceof AuthenticationActivity)) {
      Intent intent = new Intent(BaseActivity.this, AuthenticationActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
  }
}
