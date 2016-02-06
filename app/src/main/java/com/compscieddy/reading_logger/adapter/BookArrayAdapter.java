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

import com.compscieddy.reading_logger.Constants;
import com.compscieddy.reading_logger.FirebaseInfo;
import com.compscieddy.reading_logger.PageLogInputFragment;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Utils;
import com.compscieddy.reading_logger.model.Book;
import com.compscieddy.reading_logger.model.PageLog;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

/**
 * Created by elee on 12/3/15.
 */
public class BookArrayAdapter extends ArrayAdapter<Book> {

  private static final String TAG = BookArrayAdapter.class.getSimpleName();

  Activity mActivity; // Activity cause I need the .getFragmentManager()
  List<Book> mBooksList;

  public BookArrayAdapter(Activity activity, List<Book> objects) {
    super(activity, 0, objects);
    mActivity = activity;
    mBooksList = objects;
  }

  @Override
  public View getView(final int position, View convertView, final ViewGroup parent) {
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
    Utils.applyColorFilter(deleteButton.getDrawable(), mActivity.getResources().getColor(R.color.book_row_button_color));

    final View finalConvertView = convertView;
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        parent.setEnabled(false);
        // todo Delete Page Logs associated with this book first
        FirebaseInfo.booksRef.child(book.getKey()).child(Constants.FIREBASE_LOCATION_BOOK_TO_PAGE_LOG_MAPPINGS)
            .addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              }

              @Override
              public void onChildChanged(DataSnapshot dataSnapshot, String s) {
              }

              @Override
              public void onChildRemoved(DataSnapshot dataSnapshot) {
              }

              @Override
              public void onChildMoved(DataSnapshot dataSnapshot, String s) {
              }

              @Override
              public void onCancelled(FirebaseError firebaseError) {
              }
            });

        finalConvertView.animate()
            .translationX(-finalConvertView.getWidth())
            .alpha(0.3f)
            .withEndAction(new Runnable() {
              @Override
              public void run() {
                // Delete book
                FirebaseInfo.booksRef.child(book.getKey()).removeValue();
                parent.setEnabled(true);
                /** The animation and start delay is to prevent flickering because there's some slight
                  * network lag before the onChildRemoved() callback removes the list item.
                  */
                finalConvertView.animate().setStartDelay(200).translationX(0); // reset for future rows; be a good citizen
                finalConvertView.animate().setStartDelay(200).alpha(1.0f);
              }
            });
      }
    });

    Utils.applyColorFilter(newBookmarkButton.getDrawable(), mActivity.getResources().getColor(R.color.book_row_button_color));

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
        args.putString(Book.BOOK_KEY_EXTRA, book.getKey());
        pageLogInputFragment.setArguments(args);
        pageLogInputFragment.show(fragmentTransaction, PageLogInputFragment.PAGE_NUMBER_DIALOG);
      }
    });

    bookTitleView.setText(book.getTitle());

    book.addGetCurrentPageNumberListener(book.getKey(), new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
          PageLog pageLog = dataSnapshot.getValue(PageLog.class);
          currentPageNumView.setText(String.valueOf(pageLog.getPageNumber()));

          currentPageNumView.setVisibility(View.VISIBLE);
          currentPageLabel.setVisibility(View.VISIBLE);
          emptyPageLabel.setVisibility(View.GONE);
        } else {
          Log.e(TAG, "dataSnapshot getValue() is null when fetching current page number");
        }
      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {
        Log.e(TAG, "onCancelled inner firebaseError:" + firebaseError);
      }
    });

    return convertView;
  }
}
