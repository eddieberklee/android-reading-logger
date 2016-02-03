package com.compscieddy.reading_logger;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.compscieddy.reading_logger.activities.ScrollingActivity;
import com.compscieddy.reading_logger.model.ParseBook;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by elee on 1/10/16.
 */
public class BookInputFragment extends DialogFragment {

  public final static String BOOK_DIALOG = "book_dialog";

  public final static String TABLE_BOOK = "ParseBook";
  public final static String KEY_TITLE = "title";

  private View mRootView;

  @Bind(R.id.book_title_input) EditText mBookTitleInput;
  @Bind(R.id.add_book_button) View mAddBookButton;
  @Bind(R.id.close_button) ImageView mCloseButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(STYLE_NO_TITLE, R.style.FloatingNoStylingTheme);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mRootView = inflater.inflate(R.layout.fragment_book_input, null);
    ButterKnife.bind(this, mRootView);

    Utils.applyColorFilter(mCloseButton.getBackground(), getResources().getColor(R.color.flatui_red_1));
    Utils.applyColorFilter(mCloseButton.getDrawable(), getResources().getColor(R.color.white));

    return mRootView;
  }

  @OnClick(R.id.close_button)
  public void close() {
    getDialog().dismiss();
  }

  @OnClick(R.id.add_book_button)
  public void addBook() {
    String title = mBookTitleInput.getText().toString();
    if (TextUtils.isEmpty(title)) {
      Utils.showToast(getActivity(), "Please enter a title...");
    } else {
      ParseObject book = new ParseBook();
      book.put(KEY_TITLE, title);
      book.saveInBackground();

      close();
      ((ScrollingActivity) getActivity()).refreshBooksList();
    }
  }
}
