package com.compscieddy.reading_logger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.compscieddy.reading_logger.FirebaseInfo;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.adapter.BooksArrayAdapter;
import com.compscieddy.reading_logger.model.Book;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TesterActivity extends BaseActivity {

  @Bind(R.id.add_test_book_button) Button mAddTestBookButton;
  @Bind(R.id.add_test_book_button_week_pagelog) Button mAddTestBookWeekPageLogButton;
  @Bind(R.id.books_listview) ListView mBooksListView;

  private List<Book> mBooks = new ArrayList<>();
  private BooksArrayAdapter mBooksAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tester);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ButterKnife.bind(this);

    mBooksAdapter = new BooksArrayAdapter(TesterActivity.this, mBooks);
    mBooksListView.setAdapter(mBooksAdapter);

    setClickListeners();

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Destroy on the test data :O
    for (Book book : mBooks) {
      FirebaseInfo.booksRef.child(book.getKey()).removeValue();
    }
  }

  private void setClickListeners() {
    mAddTestBookButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Book book = Book.createNewBook(mEncodedEmail, "Test");
        mBooks.add(book);
        mBooksAdapter.notifyDataSetChanged();
      }
    });
  }

}
