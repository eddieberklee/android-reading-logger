package com.compscieddy.reading_logger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.models.Book;

import java.util.List;

/**
 * Created by elee on 12/3/15.
 */
public class BooksArrayAdapter extends ArrayAdapter<Book> {

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
    TextView currentPageNumView = (TextView) convertView.findViewById(R.id.item_current_page_number);

    Book currentBook = mBooksList.get(position);
    bookTitleView.setText(currentBook.title);
    currentPageNumView.setText(String.valueOf(currentBook.currentPageNum));

    return convertView;
  }
}
