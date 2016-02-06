package com.compscieddy.reading_logger.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.compscieddy.reading_logger.Constants;
import com.compscieddy.reading_logger.FirebaseInfo;
import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Utils;
import com.compscieddy.reading_logger.adapter.PageLogArrayAdapter;
import com.compscieddy.reading_logger.model.Book;
import com.compscieddy.reading_logger.model.PageLog;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BookActivity extends BaseActivity {

  private static final String TAG = BookActivity.class.getSimpleName();

  @Bind(R.id.book_title) TextView mBookTitle;
  @Bind(R.id.current_page_label) TextView mCurrentPageLabel;
  @Bind(R.id.max_page_label) TextView mMaxPageLabel;
  @Bind(R.id.of_label) TextView mOfLabel;
  @Bind(R.id.line_chart) LineChart mLineChart;
  @Bind(R.id.line_chart_view) LineChartView mLineChartView;
  @Bind(R.id.page_log_list_view) ListView mPageLogsListView;

  private Book mBook;
  private PageLogArrayAdapter mPageLogAdapter;
  private List<PageLog> mPageLogs = new ArrayList<PageLog>();

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

    if (extras == null) {
      Log.e(TAG, "BookActivity requires a book key be passed in through the intent extras");
      return;
    }

    String bookKey = extras.getString(Book.BOOK_KEY_EXTRA);

    FirebaseInfo.booksRef.child(bookKey).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        mBook = dataSnapshot.getValue(Book.class);
        initBook();
      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {
        Log.e(TAG, "Cancelled while trying to fetch book firebaseError:" + firebaseError);
      }
    });

    FirebaseInfo.booksRef.child(bookKey).child(Constants.FIREBASE_LOCATION_BOOK_TO_PAGE_LOG_MAPPINGS)
        .addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildAdded() for each page log item, dataSnapshot:" + dataSnapshot);
            String pageLogKey = dataSnapshot.getKey();
            FirebaseInfo.pageLogsRef.child(pageLogKey).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                PageLog pageLog = dataSnapshot.getValue(PageLog.class);
                mPageLogs.add(pageLog);
                mPageLogAdapter.notifyDataSetChanged();
                refreshChart();
              }

              @Override
              public void onCancelled(FirebaseError firebaseError) {

              }
            });
          }

          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String pageLogRemoveKey = dataSnapshot.getKey();
            PageLog pageLogRemove = null;
            for (PageLog pageLog : mPageLogs) {
              if (pageLog.getKey() == pageLogRemoveKey) {
                pageLogRemove = pageLog;
              }
            }
            if (pageLogRemove != null) {
              mPageLogs.remove(pageLogRemove);
              mPageLogAdapter.notifyDataSetChanged();
            }
          }

          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {}

          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

          @Override
          public void onCancelled(FirebaseError firebaseError) {}
        });

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

    mPageLogAdapter = new PageLogArrayAdapter(BookActivity.this, mPageLogs);
    mPageLogsListView.setAdapter(mPageLogAdapter);

    mLineChartView.setAxisBorderValues(0, 10, 1);

    LineSet dataSet = new LineSet(new String[] {
        "asdf", "fdsa", "1234"
    }, new float[] {
        1, 5, 20,
    });
    dataSet.setDotsColor(getResources().getColor(R.color.flatui_blue_1));
    dataSet.setThickness(Utils.dpToPx(2));
    dataSet.setColor(getResources().getColor(R.color.flatui_blue_2));
    dataSet.setFill(getResources().getColor(R.color.flatui_blue_1_transp_50));

//    mLineChartView.addData(dataSet);
//    mLineChartView.show();
  }

  private ArrayList<String> mXValues = new ArrayList<>();
  private ArrayList<Float> mYValues = new ArrayList<>();

  private void refreshChart() {
    for (int i = 0; i < mPageLogs.size(); i++) {
      PageLog pageLog = mPageLogs.get(i);
      int pageNumber = pageLog.getPageNumber();
      mXValues.add(String.valueOf(pageNumber));
      if (i > 0) { // has a previous pagelog
        int previousPageNumber = mPageLogs.get(i - 1).getPageNumber();
        mYValues.add((float) (pageNumber - previousPageNumber));
      } else {
        mYValues.add(-1f);
      }
    }

    /** This is so stupid - rewrite this, I'm just tryna get a motherfucker working rn */
    float[] yValues = new float[mYValues.size()];
    String[] xValues = new String[mXValues.size()];
    for (int i = 0; i < mXValues.size(); i++) {
      xValues[i] = mXValues.get(i);
    }
    for (int i = 0; i < mYValues.size(); i++) {
      yValues[i] = mYValues.get(i).floatValue();
    }

    Log.e(TAG, "Number of entries in each set x:" + xValues.length + " y:" + yValues.length);

    LineSet dataSet = new LineSet(xValues, yValues);
    dataSet.setDotsColor(getResources().getColor(R.color.flatui_blue_1));
    dataSet.setThickness(Utils.dpToPx(2));
    dataSet.setColor(getResources().getColor(R.color.flatui_blue_2));
    dataSet.setFill(getResources().getColor(R.color.flatui_blue_1_transp_50));

    ArrayList<ChartSet> fullChartSet = new ArrayList<>();
    fullChartSet.add(dataSet);
    mLineChartView.addData(fullChartSet);
    mLineChartView.getData();
    dataSet.getScreenPoints();
    mLineChartView.show();

    // todo: fix this set - this set shit is a bitch. Why can't I replace the data, why's it always gotta be adding it and shit
  }

  /** Called after mBook has been populated */
  private void initBook() {
    mBookTitle.setText(mBook.getTitle());

    Book.addGetCurrentPageNumberListener(mBook.getKey(), new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        PageLog mostRecentPageLog = dataSnapshot.getValue(PageLog.class);
        mCurrentPageLabel.setText(String.valueOf(mostRecentPageLog.getPageNumber()));
      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {
      }
    });

    int maxPage = mBook.getMaxPageNum();
    if (maxPage != 0) {
      mMaxPageLabel.setText(maxPage);
      mOfLabel.setVisibility(View.VISIBLE);
      mMaxPageLabel.setVisibility(View.VISIBLE);
    } else {
      mOfLabel.setVisibility(View.GONE);
      mMaxPageLabel.setVisibility(View.GONE);
    }
  }

}
