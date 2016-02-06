package com.compscieddy.reading_logger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.model.PageLog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by elee on 2/5/16.
 */
public class PageLogArrayAdapter extends ArrayAdapter<PageLog> {

  private final LayoutInflater mInflater;
  private List<PageLog> mPageLogs;

  public PageLogArrayAdapter(Context context, List<PageLog> pageLogs) {
    super(context, 0, pageLogs);

    mPageLogs = pageLogs;
    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.item_page_log_simple, parent, false);
    }
    TextView day = (TextView) convertView.findViewById(R.id.day);
    TextView readToPage = (TextView) convertView.findViewById(R.id.read_to_page);
    TextView numPagesRead = (TextView) convertView.findViewById(R.id.num_pages_read);

    PageLog pageLog = mPageLogs.get(position);
    long timestampCreated = pageLog.getTimestampCreatedLong();
    Calendar c = Calendar.getInstance();

    Date dateCreated = new Date(timestampCreated);
    c.setTime(dateCreated);
    String dayWord = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
    dayWord = dayWord.substring(0, 3);
    String dayNumber = String.valueOf(c.get(Calendar.DAY_OF_MONTH));

    int pageNumber = pageLog.getPageNumber();
    int pagesRead = -1;

    if (position > 0) { // has a previous pagelog
      int previousPageNumber = mPageLogs.get(position - 1).getPageNumber();
      pagesRead = pageNumber - previousPageNumber;
    }

    if (pagesRead != -1) {
      numPagesRead.setText(String.valueOf(pagesRead));
    }
    day.setText(dayWord + " " + dayNumber);
    readToPage.setText(String.valueOf(pageNumber));

    return convertView;
  }
}
