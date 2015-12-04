package com.compscieddy.reading_logger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.models.Book;

public class PageNumberInputActivity extends AppCompatActivity {

  private static final String TAG = PageNumberInputActivity.class.getSimpleName();
  public static final String BOOK_EXTRA = "book_extra";
  private Book mBookReceived;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_page_number_input);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    Bundle data = getIntent().getExtras();
    if (data == null) {
      Log.e(TAG, "Bro if you don't pass in any extras you're in for a world of hurt");
    }
    mBookReceived = (Book) data.getSerializable(BOOK_EXTRA);

    init();

  }

  private void init() {
    TextView bookTitleView = (TextView) findViewById(R.id.book_title);
    bookTitleView.setText(mBookReceived.title);
  }

}
