package com.compscieddy.reading_logger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Util;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthenticationActivity extends AppCompatActivity {

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

    if (ParseUser.getCurrentUser() != null) {
      Intent intent = new Intent(AuthenticationActivity.this, ScrollingActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
    }

    mEmailInput.setText("test@test.com");
    mPasswordInput.setText("password"); // pre-fill cause no need to make my sad #stillsingle existential-crisis-laden life harder than it already is

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
                  Util.showToast(AuthenticationActivity.this, "Email was recognized, but wrong password. Try again.");
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
          Util.showToast(AuthenticationActivity.this, "Signup failed. Try again."); // TODO: be more descriptive :)
        }
      }

    });
  }

}
