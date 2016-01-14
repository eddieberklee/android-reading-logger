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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

  private static final String TAG = BookActivity.class.getSimpleName();

  @Bind(R.id.book_title) TextView mBookTitle;
  @Bind(R.id.current_page_label) TextView mCurrentPageLabel;
  @Bind(R.id.max_page_label) TextView mMaxPageLabel;
  @Bind(R.id.of_label) TextView mOfLabel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_book);
    ButterKnife.bind(this);

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

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String bookId = extras.getString(Book.BOOK_ID_EXTRA);
      ParseQuery<Book> query = Book.getQuery();
      query.getInBackground(bookId, new GetCallback<Book>() {
        @Override
        public void done(Book book, ParseException e) {
          if (e != null) {
            Log.e(TAG, "Error getting Book object", e);
            return;
          }
          if (book == null) return;
          mBookTitle.setText(book.getTitle());
          book.setTextViewCurrentPageNumToView(mCurrentPageLabel);
          int maxPage = book.getMaxPageNum();
          if (maxPage != 0) {
            mMaxPageLabel.setText(maxPage);
            mOfLabel.setVisibility(View.VISIBLE);
            mMaxPageLabel.setVisibility(View.VISIBLE);
          } else {
            mOfLabel.setVisibility(View.GONE);
            mMaxPageLabel.setVisibility(View.GONE);
          }
        }
      });
    }
  }

}
