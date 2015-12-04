package com.compscieddy.reading_logger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Util;
import com.compscieddy.reading_logger.models.Book;

public class PageNumberInputActivity extends AppCompatActivity {

  private static final String TAG = PageNumberInputActivity.class.getSimpleName();
  public static final String BOOK_EXTRA = "book_extra";
  private Book mBookReceived;
  private View mNumberButton1, mNumberButton2, mNumberButton3, mNumberButton4, mNumberButton5,
      mNumberButton6, mNumberButton7, mNumberButton8, mNumberButton9, mNumberButton0, mNumberButtonDelete;
  private TextView mBookTitleView;
  private EditText mPageNumberInput;
  private ImageView mCloseButton;
  private ImageView mCheckButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_page_number_input);
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

    Bundle data = getIntent().getExtras();
    if (data == null) {
      Log.e(TAG, "Bro if you don't pass in any extras you're in for a world of hurt");
    }
    mBookReceived = (Book) data.getSerializable(BOOK_EXTRA);

    init();
    setListeners();

    Util.applyColorFilter(mCheckButton.getDrawable(), getResources().getColor(android.R.color.white));
    Util.applyColorFilter(mCheckButton.getBackground(), getResources().getColor(R.color.flatui_green_1));
    Util.applyColorFilter(mCloseButton.getDrawable(), getResources().getColor(android.R.color.white));
    Util.applyColorFilter(mCloseButton.getBackground(), getResources().getColor(R.color.flatui_red_1));

    mBookTitleView.setText(mBookReceived.title);

  }

  private void init() {
    mBookTitleView = (TextView) findViewById(R.id.book_title);
    mPageNumberInput = (EditText) findViewById(R.id.page_number_input);

    mNumberButton1 = findViewById(R.id.number_button_1);
    mNumberButton2 = findViewById(R.id.number_button_2);
    mNumberButton3 = findViewById(R.id.number_button_3);
    mNumberButton4 = findViewById(R.id.number_button_4);
    mNumberButton5 = findViewById(R.id.number_button_5);
    mNumberButton6 = findViewById(R.id.number_button_6);
    mNumberButton7 = findViewById(R.id.number_button_7);
    mNumberButton8 = findViewById(R.id.number_button_8);
    mNumberButton9 = findViewById(R.id.number_button_9);
    mNumberButton0 = findViewById(R.id.number_button_0);
    mNumberButtonDelete = findViewById(R.id.number_button_delete);

    mCheckButton = (ImageView) findViewById(R.id.check_button);
    mCloseButton = (ImageView) findViewById(R.id.close_button);
  }

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
        finish();
      }
    });
  }

}
