package com.compscieddy.reading_logger.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.compscieddy.reading_logger.PageLogInputFragment;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Util;
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

  Activity mActivity; // Activity cause I need the .getFragmentManager()
  List<Book> mBooksList;

  public BooksArrayAdapter(Activity activity, List<Book> objects) {
    super(activity, 0, objects);
    mActivity = activity;
    mBooksList = objects;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_books, parent, false);
    }
    final Book book = mBooksList.get(position);

    TextView bookTitleView = (TextView) convertView.findViewById(R.id.item_book_title);
    final TextView currentPageNumView = (TextView) convertView.findViewById(R.id.item_current_page_number);
    final TextView currentPageLabel = (TextView) convertView.findViewById(R.id.current_page_label);
    final TextView emptyPageLabel = (TextView) convertView.findViewById(R.id.empty_page_label);
    ImageView newBookmarkButton = (ImageView) convertView.findViewById(R.id.new_bookmark_button);
    ImageView deleteButton = (ImageView) convertView.findViewById(R.id.delete_button);
    Util.applyColorFilter(deleteButton.getDrawable(), mActivity.getResources().getColor(R.color.book_row_button_color));

    final View finalConvertView = convertView;
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        book.deleteInBackground();
        finalConvertView.animate()
            .translationX(-finalConvertView.getWidth())
            .alpha(0.3f)
            .withEndAction(new Runnable() {
              @Override
              public void run() {
                finalConvertView.setTranslationX(0); // reset for future rows; be a good citizen
                finalConvertView.setAlpha(1.0f);
                mBooksList.remove(position);
                notifyDataSetChanged();;
              }
            });
      }
    });

    Util.applyColorFilter(newBookmarkButton.getDrawable(), mActivity.getResources().getColor(R.color.book_row_button_color));

    newBookmarkButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentTransaction fragmentTransaction = mActivity.getFragmentManager().beginTransaction();
        Fragment previousFragment = mActivity.getFragmentManager().findFragmentByTag(PageLogInputFragment.PAGE_NUMBER_DIALOG);
        if (previousFragment != null) {
          fragmentTransaction.remove(previousFragment);
        }
        fragmentTransaction.addToBackStack(null);

        PageLogInputFragment pageLogInputFragment = new PageLogInputFragment();
        Bundle args = new Bundle();
        args.putString(Book.BOOK_ID_EXTRA, book.getObjectId());
        pageLogInputFragment.setArguments(args);
        pageLogInputFragment.show(fragmentTransaction, PageLogInputFragment.PAGE_NUMBER_DIALOG);
      }
    });

    bookTitleView.setText(book.getTitle());

    ParseQuery currentPageNumQuery = book.getCurrentPageNumQuery();
    currentPageNumQuery.findInBackground(new FindCallback<PageLog>() {
      @Override
      public void done(List<PageLog> objects, ParseException e) {
        if (e == null) {
          if (objects != null && objects.size() > 0) {
            int currentPageNum = (objects.get(0)).getPageNum();
            currentPageNumView.setText(String.valueOf(currentPageNum));

            currentPageNumView.setVisibility(View.VISIBLE);
            currentPageLabel.setVisibility(View.VISIBLE);
            emptyPageLabel.setVisibility(View.GONE);
          } else {
            // todo: is no current page number - handle this some other way than just not populating or populating with 0
            currentPageNumView.setVisibility(View.GONE);
            currentPageLabel.setVisibility(View.GONE);
            emptyPageLabel.setVisibility(View.VISIBLE);
          }
        } else {
          Log.e(TAG, "Error getting current page number", e);
        }
      }
    });

    return convertView;
  }
}
