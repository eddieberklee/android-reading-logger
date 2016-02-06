package com.compscieddy.reading_logger.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.compscieddy.reading_logger.model.PageLog;

import java.util.List;

/**
 * Created by elee on 2/5/16.
 */
public class PageLogArrayAdapter extends ArrayAdapter<PageLog> {

  public PageLogArrayAdapter(Context context, List<PageLog> pageLogs) {
    super(context, 0, pageLogs);
    
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return super.getView(position, convertView, parent);
  }
}
