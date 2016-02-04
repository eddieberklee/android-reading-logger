package com.compscieddy.reading_logger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.compscieddy.reading_logger.Constants;
import com.compscieddy.reading_logger.FirebaseInfo;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Utils;
import com.compscieddy.reading_logger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthenticationActivity extends BaseActivity {

  private static final String TAG = AuthenticationActivity.class.getSimpleName();

  @Bind(R.id.signup_login_button) View mAuthenticationButton;
  @Bind(R.id.email_input) EditText mEmailInput;
  @Bind(R.id.password_input) EditText mPasswordInput;
  @Bind(R.id.progress_bar) ProgressBar mProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authentication);
    ButterKnife.bind(this);

    if (FirebaseInfo.ref.getAuth() != null) { // already logged in
      Intent intent = new Intent(AuthenticationActivity.this, ScrollingActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    }

    // pre-fill cause no need to make my sad #stillsingle existential-crisis-laden life harder than it already is
    mEmailInput.setText("test@test.com");
    mPasswordInput.setText("password");

    mAuthenticationButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mProgressBar.setVisibility(View.VISIBLE);
        final String email = mEmailInput.getText().toString();
        final String password = mPasswordInput.getText().toString();
        ParseUser.logInInBackground(email, password, new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (e == null) {
              Intent intent = new Intent(AuthenticationActivity.this, ScrollingActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
            } else {
              int exceptionCode = e.getCode();
              switch (exceptionCode) {
                case ParseException.USERNAME_TAKEN:
                  Log.e(TAG, "Username/email exists but the password must be wrong", e);
                  Utils.showToast(AuthenticationActivity.this, "Email was recognized, but wrong password. Try again.");
                  break;
                case ParseException.OBJECT_NOT_FOUND:
                case ParseException.EMAIL_NOT_FOUND:
                  Log.d(TAG, "Username/email doesn't exist so going to try and signup the user instead.", e);
                  signupUser(email, password);
                  break;
                default:
                  Log.e(TAG, "ParseException while logging in (exception code: " + exceptionCode + ")", e);
                  break;
              }
            }
          }
        });
      }
    });

    mAuthenticationButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String email = mEmailInput.getText().toString();
        final String password = mPasswordInput.getText().toString();
        if (!Utils.isEmailValid(email)) {
          // todo: special handling for invalid email address
        }

        FirebaseInfo.ref.createUser(
            email,
            password,
            new Firebase.ValueResultHandler<Map<String, Object>>() {
              @Override
              public void onSuccess(Map<String, Object> result) {
                Utils.showToast(AuthenticationActivity.this, "Hi " + email + "! :)");

                final String encodedEmail = Utils.encodeEmail(email);

                HashMap<String, Object> timestampJoined = new HashMap<>();
                timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                User newUser = new User(encodedEmail, timestampJoined);
                HashMap<String, Object> newUserMap = (HashMap<String, Object>) new ObjectMapper().convertValue(newUser, Map.class);

                HashMap<String, Object> userAndUidMapping = new HashMap<>();
                userAndUidMapping.put("/" + Constants.FIREBASE_LOCATION_USERS + "/" + encodedEmail,
                    newUserMap);

                final String authUserId = (String) result.get("uid");
                userAndUidMapping.put("/" + Constants.FIREBASE_LOCATION_UID_MAPPINGS + "/" + authUserId,
                    encodedEmail);

                FirebaseInfo.ref.updateChildren(userAndUidMapping, new Firebase.CompletionListener() {
                  @Override
                  public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                  }
                });

                Intent intent = new Intent(AuthenticationActivity.this, ScrollingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
              }

              @Override
              public void onError(FirebaseError firebaseError) {
                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                  Utils.showToast(AuthenticationActivity.this, "Email already exists - trying to log you in instead");
                  loginUser(email, password);
                } else {
                  Log.e(TAG, "firebaseError: " + firebaseError);
                }
              }
            }
        );

        loginUser(email, password);
      }
    });

  }

  private void loginUser(final String email, String password) {
    FirebaseInfo.ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
      @Override
      public void onAuthenticated(final AuthData authData) {
        String encodedEmail = Utils.encodeEmail(email);

        if (authData != null) {
          SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AuthenticationActivity.this);
          sp.edit().putString(Constants.KEY_ENCODED_EMAIL, encodedEmail).apply();
        }

        Firebase userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(encodedEmail);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(TAG, "Logged in to email:" + email);
            User user = dataSnapshot.getValue(User.class);
            Log.d(TAG, "user:" + user);

            Intent intent = new Intent(AuthenticationActivity.this, ScrollingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          }
          @Override
          public void onCancelled(FirebaseError firebaseError) {

          }
        });

      }
      @Override
      public void onAuthenticationError(FirebaseError firebaseError) {

      }
    });
  }

  private void signupUser(String email, String password) {
    mProgressBar.setVisibility(View.VISIBLE);
    ParseUser newUser = new ParseUser();
    newUser.setUsername(email);
    newUser.setEmail(email);
    newUser.setPassword(password);
    newUser.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (e == null) {
          Intent intent = new Intent(AuthenticationActivity.this, ScrollingActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
        } else {
          int exceptionCode = e.getCode();
          Log.e(TAG, "ParseException while signing up in (exception code: " + exceptionCode + ")", e);
          Utils.showToast(AuthenticationActivity.this, "Signup failed. Try again."); // TODO: be more descriptive :)
        }
      }

    });
  }

}
