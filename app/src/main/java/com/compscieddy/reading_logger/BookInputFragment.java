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

import com.compscieddy.reading_logger.model.Book;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by elee on 1/10/16.
 */
public class BookInputFragment extends DialogFragment {

  private static final String TAG = BookInputFragment.class.getSimpleName();

  public final static String BOOK_DIALOG = "book_dialog";

  private View mRootView;
  private String mEncodedEmail;

  @Bind(R.id.book_title_input) EditText mBookTitleInput;
  @Bind(R.id.add_book_button) View mAddBookButton;
  @Bind(R.id.close_button) ImageView mCloseButton;

  public static BookInputFragment newInstance(String encodedEmail) {
    BookInputFragment fragment = new BookInputFragment();
    Bundle bundle = new Bundle();
    bundle.putString(Constants.KEY_ENCODED_EMAIL, encodedEmail);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(STYLE_NO_TITLE, R.style.FloatingNoStylingTheme);
    mEncodedEmail = getArguments().getString(Constants.KEY_ENCODED_EMAIL);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mRootView = inflater.inflate(R.layout.fragment_book_input, null);
    ButterKnife.bind(this, mRootView);

    Util.applyColorFilter(mCloseButton.getBackground(), getResources().getColor(R.color.flatui_red_1));
    Util.applyColorFilter(mCloseButton.getDrawable(), getResources().getColor(R.color.white));

    return mRootView;
  }

  @OnClick(R.id.close_button)
  public void closeFragment() {
    getDialog().dismiss();
  }

  @OnClick(R.id.add_book_button)
  public void addBook() {
    String title = mBookTitleInput.getText().toString();
    if (TextUtils.isEmpty(title)) {
      Util.showToast(getActivity(), "Please enter a title...");
    } else {
      Book.createNewBook(mEncodedEmail, title);
      closeFragment();
    }
  }

}
