package com.compscieddy.reading_logger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.models.Book;
import com.compscieddy.reading_logger.models.PageLog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by elee on 12/3/15.
 */
public class BooksArrayAdapter extends ArrayAdapter<Book> {

  private static final String TAG = BooksArrayAdapter.class.getSimpleName();

  Context mContext;
  List<Book> mBooksList;

  public BooksArrayAdapter(Context context, List<Book> objects) {
    super(context, 0, objects);
    mContext = context;
    mBooksList = objects;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(mContext).inflate(R.layout.item_books, parent, false);
    }
    TextView bookTitleView = (TextView) convertView.findViewById(R.id.item_book_title);
    final TextView currentPageNumView = (TextView) convertView.findViewById(R.id.item_current_page_number);
    final Book currentBook = mBooksList.get(position);
    bookTitleView.setText(currentBook.getTitle());

    ParseQuery currentPageNumQuery = currentBook.getCurrentPageNumQuery();
    currentPageNumQuery.findInBackground(new FindCallback<PageLog>() {
      @Override
      public void done(List<PageLog> objects, ParseException e) {
        if (objects.size() > 0) {
          int currentPageNum = ((PageLog) objects.get(0)).getPageNum();
          currentPageNumView.setText(String.valueOf(currentPageNum));
        } else {
          // todo: is no current page number - handle this some other way than just not populating or populating with 0
        }
      }
    });

    return convertView;
  }
}
