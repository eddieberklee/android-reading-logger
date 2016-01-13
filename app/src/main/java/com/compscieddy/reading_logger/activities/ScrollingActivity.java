package com.compscieddy.reading_logger.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.compscieddy.reading_logger.BookInputFragment;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.adapter.BooksArrayAdapter;
import com.compscieddy.reading_logger.models.Book;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

  private static final String TAG = ScrollingActivity.class.getSimpleName();

  ListView mBooksListView;
  BooksArrayAdapter mBooksAdapter;
  List<Book> mBooksList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment oldFragment = getFragmentManager().findFragmentByTag(BookInputFragment.BOOK_DIALOG);
        if (oldFragment != null) {
          transaction.remove(oldFragment);
        }
        transaction.addToBackStack(null);

        BookInputFragment bookInputFragment = new BookInputFragment();
        bookInputFragment.show(transaction, BookInputFragment.BOOK_DIALOG);
      }
    });

    init();

    mBooksList = new ArrayList<>();
    Book.getQuery().findInBackground(new FindCallback<Book>() {
      @Override
      public void done(List<Book> objects, ParseException e) {
        if (e == null) {
          mBooksList = new ArrayList<>(objects);
          mBooksAdapter = new BooksArrayAdapter(ScrollingActivity.this, mBooksList);
          mBooksListView.setAdapter(mBooksAdapter);
        } else {
          Log.d(TAG, "Error getting all books", e);
        }
      }
    });

    mBooksListView.setOnItemClickListener(mBooksListOnItemClickListener);
  }

  public void refreshBooksList() {
    Book.getQuery().findInBackground(new FindCallback<Book>() {
      @Override
      public void done(List<Book> objects, ParseException e) {
        if (e == null) {
          mBooksList = new ArrayList<>(objects);
          mBooksAdapter = new BooksArrayAdapter(ScrollingActivity.this, mBooksList);
          mBooksListView.setAdapter(mBooksAdapter);
        } else {
          Log.d(TAG, "Error getting all books", e);
        }
      }
    });
  }

  AdapterView.OnItemClickListener mBooksListOnItemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      // todo: open a separate screen for each book
    }
  };

  private void init() {
    mBooksListView = (ListView) findViewById(R.id.books_listview);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_scrolling, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
