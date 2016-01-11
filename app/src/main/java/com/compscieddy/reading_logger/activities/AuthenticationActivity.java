package com.compscieddy.reading_logger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.compscieddy.reading_logger.R;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthenticationActivity extends AppCompatActivity {

  @Bind(R.id.signup_login_button) View mSignupLoginButton;
  @Bind(R.id.email_input) EditText mEmailInput;
  @Bind(R.id.password_input) EditText mPasswordInput;

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


  }
}
