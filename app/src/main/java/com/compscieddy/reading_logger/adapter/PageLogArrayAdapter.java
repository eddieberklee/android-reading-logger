package com.compscieddy.reading_logger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.model.PageLog;

import java.util.List;

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
    int pageNumber = pageLog.getPageNumber();
    int pagesRead = -1;

    if (position > 0) { // has a previous pagelog
      int previousPageNumber = mPageLogs.get(position - 1).getPageNumber();
      pagesRead = pageNumber - previousPageNumber;
    }

    if (pagesRead != -1) {
      numPagesRead.setText(String.valueOf(pagesRead));
    }
    day.setText(String.valueOf(timestampCreated));
    readToPage.setText(String.valueOf(pageNumber));

    return convertView;
  }
}
