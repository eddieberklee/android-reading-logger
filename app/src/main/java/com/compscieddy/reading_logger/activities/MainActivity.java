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
import com.compscieddy.reading_logger.FirebaseInfo;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.adapter.BooksArrayAdapter;
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

        BookInputFragment bookInputFragment = BookInputFragment.newInstance(mEncodedEmail);
        bookInputFragment.show(transaction, BookInputFragment.BOOK_DIALOG);
      }
    });

    init();

    mBooksList = new ArrayList<>();
    mBooksAdapter = new BooksArrayAdapter(MainActivity.this, mBooksList);
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
        Book removedBook = dataSnapshot.getValue(Book.class);
        mBooksList.remove(removedBook);
        mBooksAdapter.notifyDataSetChanged();
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
      logout();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
