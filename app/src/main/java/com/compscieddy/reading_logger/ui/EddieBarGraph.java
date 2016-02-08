package com.compscieddy.reading_logger.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.compscieddy.reading_logger.R;
import com.compscieddy.reading_logger.Util;

import java.util.List;

/**
 * Created by elee on 2/6/16.
 */
public class EddieBarGraph extends FrameLayout {

  private List<String> xValues;
  private List<Float> yValues;

  public EddieBarGraph(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    int axisThickness = Util.dpToPx(3);

    View xAxis = new View(context);
    xAxis.setBackgroundColor(getResources().getColor(R.color.black));
    addView(xAxis, axisThickness, ViewGroup.LayoutParams.MATCH_PARENT);

    View yAxis = new View(context);
    yAxis.setBackgroundColor(getResources().getColor(R.color.black));
    addView(yAxis, ViewGroup.LayoutParams.MATCH_PARENT, axisThickness);
  }

  public void setData(List<String> xValues, List<Float> yValues) {
    this.xValues = xValues;
    this.yValues = yValues;
    invalidate();
  }

}
