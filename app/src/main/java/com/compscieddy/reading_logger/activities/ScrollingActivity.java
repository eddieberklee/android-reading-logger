package com.compscieddy.reading_logger.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.compscieddy.reading_logger.BookInputFragment;
import com.compscieddy.reading_logger.Constants;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Utils;
import com.compscieddy.reading_logger.adapter.BooksArrayAdapter;
import com.compscieddy.reading_logger.model.Book;
import com.compscieddy.reading_logger.model.ParseBook;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends BaseActivity {

  private static final String TAG = ScrollingActivity.class.getSimpleName();

  ListView mBooksListView;
  BooksArrayAdapter mBooksAdapter;
  List<ParseBook> mBooksList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scrolling);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Firebase ref = new Firebase(Constants.FIREBASE_URL);
    Book newBook = new Book("anonymousOwner", "Book Title");
    ref.child("books").setValue(newBook);

    ref.child("books").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Book book = dataSnapshot.getValue(Book.class);
        Utils.showToast(ScrollingActivity.this, book.getTitle());
      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {

      }
    });

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
    ParseBook.getQuery().findInBackground(new FindCallback<ParseBook>() {
      @Override
      public void done(List<ParseBook> objects, ParseException e) {
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
    ParseBook.getQuery().findInBackground(new FindCallback<ParseBook>() {
      @Override
      public void done(List<ParseBook> objects, ParseException e) {
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
      if (mBooksList.size() > 0) {
        ParseBook book = mBooksList.get(position);
        Intent intent = new Intent(ScrollingActivity.this, BookActivity.class);
        intent.putExtra(ParseBook.BOOK_ID_EXTRA, book.getObjectId());
        startActivity(intent);
      }

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
    } else if (id == R.id.action_logout) {
      ParseUser.logOutInBackground(new LogOutCallback() {
        @Override
        public void done(ParseException e) {
          Intent intent = new Intent(ScrollingActivity.this, AuthenticationActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        }
      });
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
