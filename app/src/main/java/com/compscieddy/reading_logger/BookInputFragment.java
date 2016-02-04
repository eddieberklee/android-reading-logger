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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

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

    Utils.applyColorFilter(mCloseButton.getBackground(), getResources().getColor(R.color.flatui_red_1));
    Utils.applyColorFilter(mCloseButton.getDrawable(), getResources().getColor(R.color.white));

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
      Utils.showToast(getActivity(), "Please enter a title...");
    } else {
      Firebase userBooksRef = new Firebase(Constants.FIREBASE_URL_BOOKS);

      Firebase newBookRef = userBooksRef.push();
      String newBookKey = newBookRef.getKey();

      Book book = new Book(newBookKey, mEncodedEmail, title);
      HashMap<String, Object> bookMap = (HashMap<String, Object>) new ObjectMapper().convertValue(book, Map.class);

      newBookRef.setValue(bookMap);

      HashMap<String, Object> bookIdMap = new HashMap<>();
      bookIdMap.put(Constants.FIREBASE_LOCATION_USER_TO_BOOK_MAPPINGS, newBookKey);
      HashMap<String, Object> userBookIdMap = new HashMap<>();
      userBookIdMap.put(newBookKey, true);
      FirebaseInfo.userRef.child(Constants.FIREBASE_LOCATION_USER_TO_BOOK_MAPPINGS)
        .updateChildren(userBookIdMap);

      closeFragment();
    }
  }
}
