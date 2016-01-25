package com.compscieddy.reading_logger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Util;
import com.compscieddy.reading_logger.models.Book;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.github.mikephil.charting.charts.LineChart;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

  private static final String TAG = BookActivity.class.getSimpleName();

  @Bind(R.id.book_title) TextView mBookTitle;
  @Bind(R.id.current_page_label) TextView mCurrentPageLabel;
  @Bind(R.id.max_page_label) TextView mMaxPageLabel;
  @Bind(R.id.of_label) TextView mOfLabel;
  @Bind(R.id.line_chart) LineChart mLineChart;
  @Bind(R.id.line_chart_view) LineChartView mLineChartView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_book);
    ButterKnife.bind(this);

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

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String bookId = extras.getString(Book.BOOK_ID_EXTRA);
      ParseQuery<Book> query = Book.getQuery();
      query.getInBackground(bookId, new GetCallback<Book>() {
        @Override
        public void done(Book book, ParseException e) {
          if (e != null) {
            Log.e(TAG, "Error getting Book object", e);
            return;
          }
          if (book == null) return;
          mBookTitle.setText(book.getTitle());
          book.setTextViewCurrentPageNumToView(mCurrentPageLabel);
          int maxPage = book.getMaxPageNum();
          if (maxPage != 0) {
            mMaxPageLabel.setText(maxPage);
            mOfLabel.setVisibility(View.VISIBLE);
            mMaxPageLabel.setVisibility(View.VISIBLE);
          } else {
            mOfLabel.setVisibility(View.GONE);
            mMaxPageLabel.setVisibility(View.GONE);
          }
        }
      });
    }

    /*ArrayList<Entry> valsComp1 = new ArrayList<>();
    Entry c1e1 = new Entry(100.0f, 0);
    valsComp1.add(c1e1);
    Entry c1e2 = new Entry(50.0f, 2);
    valsComp1.add(c1e2);
    LineDataSet setComp1 = new LineDataSet(valsComp1, "Label 1");
    ArrayList<LineDataSet> dataSets = new ArrayList<>();
    dataSets.add(setComp1);
    ArrayList<String> xVals = new ArrayList<String>();
    xVals.add("1.Q"); xVals.add("2.Q"); xVals.add("3.Q"); xVals.add("4.Q");
    LineData lineData = new LineData(xVals, dataSets);
    mLineChart.setData(lineData);*/

    mLineChartView.setAxisBorderValues(0, 30, 10);

    LineSet dataSet = new LineSet(new String[] {
        "asdf", "fdsa", "1234"
    }, new float[] {
        1, 5, 20,
    });
    dataSet.setDotsColor(getResources().getColor(R.color.flatui_blue_1));
    dataSet.setThickness(Util.dpToPx(2));
    dataSet.setColor(getResources().getColor(R.color.flatui_blue_2));
    dataSet.setFill(getResources().getColor(R.color.flatui_blue_1_transp_50));

    mLineChartView.addData(dataSet);
    mLineChartView.show();
  }

}
