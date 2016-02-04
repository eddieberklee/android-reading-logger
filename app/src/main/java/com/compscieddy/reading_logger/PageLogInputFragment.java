package com.compscieddy.reading_logger;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.compscieddy.reading_logger.model.Book;
import com.compscieddy.reading_logger.model.PageLog;
import com.compscieddy.reading_logger.model.ParseBook;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elee on 1/9/16.
 */
public class PageLogInputFragment extends DialogFragment {

  private static final String TAG = PageLogInputFragment.class.getSimpleName();

  public static final String PAGE_NUMBER_DIALOG = "page_number_dialog";
  private ParseBook mBook;
  private View mRootView;

  @Bind(R.id.number_button_1) View mNumberButton1;
  @Bind(R.id.number_button_2) View mNumberButton2;
  @Bind(R.id.number_button_3) View mNumberButton3;
  @Bind(R.id.number_button_4) View mNumberButton4;
  @Bind(R.id.number_button_5) View mNumberButton5;
  @Bind(R.id.number_button_6) View mNumberButton6;
  @Bind(R.id.number_button_7) View mNumberButton7;
  @Bind(R.id.number_button_8) View mNumberButton8;
  @Bind(R.id.number_button_9) View mNumberButton9;
  @Bind(R.id.number_button_0) View mNumberButton0;
  @Bind(R.id.number_button_delete) View mNumberButtonDelete;
  @Bind(R.id.book_title) TextView mBookTitleView;
  @Bind(R.id.page_number_input) EditText mPageNumberInput;
  @Bind(R.id.close_button) ImageView mCloseButton;
  @Bind(R.id.check_button) ImageView mCheckButton;

  private View.OnClickListener mNumberButtonClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      int num = -1;
      int viewId = v.getId();
      if (viewId == mNumberButton1.getId()) {
        num = 1;
      } else if (viewId == mNumberButton2.getId()) {
        num = 2;
      } else if (viewId == mNumberButton3.getId()) {
        num = 3;
      } else if (viewId == mNumberButton4.getId()) {
        num = 4;
      } else if (viewId == mNumberButton5.getId()) {
        num = 5;
      } else if (viewId == mNumberButton6.getId()) {
        num = 6;
      } else if (viewId == mNumberButton7.getId()) {
        num = 7;
      } else if (viewId == mNumberButton8.getId()) {
        num = 8;
      } else if (viewId == mNumberButton9.getId()) {
        num = 9;
      } else if (viewId == mNumberButton0.getId()) {
        num = 0;
      }

      if (viewId == mNumberButtonDelete.getId()) {
        Editable pageNumberText = mPageNumberInput.getText();
        int endingIndex = pageNumberText.length() - 1;
        if (endingIndex >= 0) {
          mPageNumberInput.setText(pageNumberText.subSequence(0, endingIndex).toString()); // inclusive af
        }
      } else {
        mPageNumberInput.setText(mPageNumberInput.getText() + String.valueOf(num));
      }
      mPageNumberInput.setSelection(mPageNumberInput.getText().length());
    }
  };
  private String mBookKey;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(STYLE_NO_TITLE, R.style.FloatingNoStylingTheme);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mRootView = inflater.inflate(R.layout.fragment_page_log_input, null);
    ButterKnife.bind(this, mRootView);

    Bundle args = getArguments();
    mBookKey = args.getString(Book.BOOK_KEY_EXTRA);

    // todo: set mPageNumberInput to the current page number (firebase query needs to be complex, there's no field)

    Book.addCurrentPageNumberListener(mBookKey, new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
          PageLog pageLog = dataSnapshot.getValue(PageLog.class);
          mPageNumberInput.setText(pageLog.getPageNumber());
        }
      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {

      }
    });

    ParseQuery<ParseBook> query = ParseBook.getQuery();
    query.getInBackground(mBookKey, new GetCallback<ParseBook>() {
      @Override
      public void done(ParseBook book, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Error getting ParseBook object", e);
          return;
        }
        if (book == null) return;
        mBook = book;
        mBook.setEditTextCurrentPageNumToView(mPageNumberInput);
        mBookTitleView.setText(mBook.getTitle());
      }
    });

    Utils.applyColorFilter(mCheckButton.getDrawable(), getResources().getColor(android.R.color.white));
    Utils.applyColorFilter(mCheckButton.getBackground(), getResources().getColor(R.color.flatui_green_1));
    Utils.applyColorFilter(mCloseButton.getDrawable(), getResources().getColor(android.R.color.white));
    Utils.applyColorFilter(mCloseButton.getBackground(), getResources().getColor(R.color.flatui_red_1));

    setListeners();

    return mRootView;
  }

  private void setListeners() {
    mNumberButton1.setOnClickListener(mNumberButtonClickListener);
    mNumberButton2.setOnClickListener(mNumberButtonClickListener);
    mNumberButton3.setOnClickListener(mNumberButtonClickListener);
    mNumberButton4.setOnClickListener(mNumberButtonClickListener);
    mNumberButton5.setOnClickListener(mNumberButtonClickListener);
    mNumberButton6.setOnClickListener(mNumberButtonClickListener);
    mNumberButton7.setOnClickListener(mNumberButtonClickListener);
    mNumberButton8.setOnClickListener(mNumberButtonClickListener);
    mNumberButton9.setOnClickListener(mNumberButtonClickListener);
    mNumberButton0.setOnClickListener(mNumberButtonClickListener);
    mNumberButtonDelete.setOnClickListener(mNumberButtonClickListener);
    mCloseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        closeFragment();
      }
    });
    mCheckButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        HashMap<String, Object> timestamp = new HashMap<>();
        timestamp.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

        int pageNum = Integer.parseInt(mPageNumberInput.getText().toString());

        Firebase newPageLogRef = FirebaseInfo.pageLogsRef.push();
        String pageLogKey = newPageLogRef.getKey();

        PageLog pageLog = new PageLog(pageLogKey, pageNum, timestamp);
        newPageLogRef.setValue(pageLog);

        HashMap<String, Object> bookPageMapping = new HashMap<>();
        bookPageMapping.put(pageLogKey, true);
        FirebaseInfo.booksRef.child(mBookKey).child(Constants.FIREBASE_LOCATION_BOOK_TO_PAGE_LOG_MAPPINGS)
            .updateChildren(bookPageMapping);

        // todo should add a callback and show progressbar - or maybe something else if user shouldn't be kept waiting
        closeFragment();
      }
    });
  }

  private void closeFragment() {
    getDialog().dismiss();
  }

}
