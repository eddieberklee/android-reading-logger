package com.compscieddy.reading_logger.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.compscieddy.reading_logger.BookInputFragment;
import com.compscieddy.reading_logger.FirebaseInfo;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.adapter.BookArrayAdapter;
import com.compscieddy.reading_logger.model.Book;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  ListView mBooksListView;
  BookArrayAdapter mBooksAdapter;
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

        BookInputFragment bookInputFragment = BookInputFragment.newInstance(mEncodedEmail);
        bookInputFragment.show(transaction, BookInputFragment.BOOK_DIALOG);
      }
    });

    init();

    mBooksList = new ArrayList<>();
    mBooksAdapter = new BookArrayAdapter(MainActivity.this, mBooksList);
    mBooksListView.setAdapter(mBooksAdapter);

    FirebaseInfo.userBooksMappingsRef.addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.e(TAG, "onChildAdded for userBooks");
        String bookKey = dataSnapshot.getKey();
        FirebaseInfo.booksRef.child(bookKey).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            Book book = dataSnapshot.getValue(Book.class);
            if (book == null) {
              // TODO: there is some mismatch - book must have been deleted without an entry from this mapping being deleted
              return;
            }
            Log.e(TAG, " book.getTitle(): " + book.getTitle() + " book.getOwner(): " + book.getOwner());
            mBooksList.add(book);
            mBooksAdapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(FirebaseError firebaseError) {

          }
        });
      }

      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {
        /** The data consistency is handled by FirebaseInfo (like deleting a book's corresponding userToBook mappings and associated PageLogs)
          * but the up-to-date visual changes that need to be made are up to each individual activity/fragment/etc.
          */
        String removedBookKey = dataSnapshot.getKey();
        Book bookToRemove = null;
        for (Book book : mBooksList) {
          if (TextUtils.equals(book.getKey(), removedBookKey)) {
            bookToRemove = book;
          }
        }
        if (bookToRemove != null) {
          mBooksList.remove(bookToRemove);
          mBooksAdapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {

      }
    });

    mBooksListView.setOnItemClickListener(mBooksListOnItemClickListener);
  }

  AdapterView.OnItemClickListener mBooksListOnItemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      if (mBooksList.size() > 0) {
        Book book = mBooksList.get(position);
        Intent intent = new Intent(MainActivity.this, BookActivity.class);
        intent.putExtra(Book.BOOK_KEY_EXTRA, book.getKey());
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
    getMenuInflater().inflate(R.menu.menu_main, menu);
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
      logout();
      return true;
    } else if (id == R.id.action_tester) {
      startActivity(new Intent(MainActivity.this, TesterActivity.class));
    }
    return super.onOptionsItemSelected(item);
  }
}
