package com.compscieddy.reading_logger;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.compscieddy.reading_logger.activities.ScrollingActivity;
import com.compscieddy.reading_logger.models.Book;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by elee on 1/10/16.
 */
public class BookInputFragment extends DialogFragment {

  public final static String BOOK_DIALOG = "book_dialog";

  public final static String TABLE_BOOK = "Book";
  public final static String KEY_TITLE = "title";

  private View mRootView;

  @Bind(R.id.book_title_input) EditText mBookTitleInput;
  @Bind(R.id.add_book_button) View mAddBookButton;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mRootView = inflater.inflate(R.layout.fragment_book_input, null);
    ButterKnife.bind(this, mRootView);
    return mRootView;
  }

  @OnClick(R.id.add_book_button)
  public void addBook() {
    String title = mBookTitleInput.getText().toString();
    if (TextUtils.isEmpty(title)) {
      Util.showToast(getActivity(), "Please enter a title...");
    } else {
      ParseObject book = new Book();
      book.put(KEY_TITLE, title);
      book.saveInBackground();

      getDialog().dismiss();
      ((ScrollingActivity) getActivity()).refreshBooksList();
    }
  }
}
