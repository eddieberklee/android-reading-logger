package com.compscieddy.reading_logger;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/**
 * Created by elee on 12/3/15.
 */
public class Util {
  public static void applyColorFilter(Drawable drawable, int color) {
    applyColorFilter(drawable, color, false);
  }

  public static void applyColorFilter(Drawable drawable, int color, boolean mutate) {
    drawable.clearColorFilter();
    PorterDuff.Mode mode = (color == Color.TRANSPARENT ? PorterDuff.Mode.SRC_ATOP : PorterDuff.Mode.SRC_IN);
    if (mutate) {
      drawable.mutate().setColorFilter(color, mode);
    } else {
      drawable.setColorFilter(color, mode);
    }
  }
}
