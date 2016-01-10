package com.compscieddy.reading_logger.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.compscieddy.reading_logger.PageNumberInputFragment;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.adapter.BooksArrayAdapter;
import com.compscieddy.reading_logger.models.Book;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

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
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    init();

    mBooksList = new ArrayList<>();
    mBooksList.add(new Book(
        "Alchemist",
        13,
        19,
        29,
        null
    ));
    mBooksList.add(new Book(
        "Passionate Programming",
        23,
        99,
        30,
        null
    ));
    mBooksList.add(new Book(
        "Ruby on Rails for Dummies",
        57,
        28,
        63,
        null
    ));
    mBooksAdapter = new BooksArrayAdapter(ScrollingActivity.this, mBooksList);
    mBooksListView.setAdapter(mBooksAdapter);

    mBooksListView.setOnItemClickListener(mBooksListOnItemClickListener);
  }

  AdapterView.OnItemClickListener mBooksListOnItemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
      Fragment previousFragment = getFragmentManager().findFragmentByTag(PageNumberInputFragment.PAGE_NUMBER_DIALOG);
      if (previousFragment != null) {
        fragmentTransaction.remove(previousFragment);
      }
      fragmentTransaction.addToBackStack(null);

      PageNumberInputFragment pageNumberInputFragment = new PageNumberInputFragment();
      Bundle args = new Bundle();
      args.putSerializable(PageNumberInputFragment.BOOK_EXTRA, mBooksList.get(position));
      pageNumberInputFragment.setArguments(args);
      pageNumberInputFragment.show(fragmentTransaction, PageNumberInputFragment.PAGE_NUMBER_DIALOG);
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
