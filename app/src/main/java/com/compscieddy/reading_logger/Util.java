package com.compscieddy.reading_logger;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

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

  public static void showToast(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

  /** Time-Related */
  public static String militaryTimeToAMPM(int militaryTime) {
    boolean isAM = (militaryTime / 12) < 1;
    String amPM = isAM ? "am" : "pm";
    int adjustedTime = militaryTime % 12;
    if (adjustedTime == 0) {
      if (militaryTime / 12 == 1) {
        adjustedTime = 12;
      } else if (militaryTime / 12 == 1) {
        adjustedTime = 24;
      }
    }
    return String.valueOf(adjustedTime) + amPM;
  }

  public static int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }

  public static int pxToDp(int px) {
    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
  }

  /** Drawing and Path Stuff */

  /* If you're using this in an onDraw it may be a better idea to re-use the
     PathMeasure object (less memory allocations which = less GC operations) */
  /* @param distance - percentage of length as a 0-1 float */
  public static float[] getDistanceInPathCoordinates(Path path, float distanceFraction) {
    return getDistanceInPathCoordinates(path, distanceFraction, false);
  }
  public static float[] getDistanceInPathCoordinates(Path path, float distance, boolean isActualDistanceNotFraction) {
    PathMeasure pathMeasure = new PathMeasure(path, false);
    float[] pos = new float[2];
    if (isActualDistanceNotFraction) {
      pathMeasure.getPosTan(distance, pos, null);
    } else {
      pathMeasure.getPosTan(pathMeasure.getLength() * distance, pos, null);
    }
    return pos;
  }
  //public static float[] getDistanceInPathCoordinates(PathMeasure, Path, distance)

  /** Java by default has mod do -30 % 100 = -30 instead of 70. */
  public static float betterMod(float number, float modNumber) {
    return (number % modNumber + modNumber) % modNumber;
  }

  public static void setPaddingLeft(View view, int padding) {
    view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
  }
  public static void setPaddingTop(View view, int padding) {
    view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), view.getPaddingBottom());
  }
  public static void setPaddingRight(View view, int padding) {
    view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
  }
  public static void setPaddingBottom(View view, int padding) {
    view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
  }
  public static void setPaddingLeftRelative(View view, int padding) {
    view.setPaddingRelative(padding, 0, 0, 0);
  }
  public static void setPaddingTopRelative(View view, int padding) {
    view.setPaddingRelative(0, padding, 0, 0);
  }
  public static void setPaddingRightRelative(View view, int padding) {
    view.setPaddingRelative(0, 0, padding, 0);
  }
  public static void setPaddingBottomRelative(View view, int padding) {
    view.setPaddingRelative(0, 0, 0, padding);
  }

  /**
   * Return the color that is in-between 'startColor' and 'endColor' at 'progressPercentage'
   * @param progressPercentage - 0.0 is beginning, 1.0 is end. Ex: 0.5 returns the color exactly
   *                           in-between 'startColor' and 'endColor'
   */
  public static int getIntermediateColor(int startColor, int endColor, float progressPercentage) {
        /* There is a more efficient way to write this with bit-shifting I think, let me (@elee) know
           if this should be rewritten like that */
    int startRed = Color.red(startColor);
    int endRed = Color.red(endColor);
    int newRed = startRed + Math.round(progressPercentage * (endRed - startRed));

    int startGreen = Color.green(startColor);
    int endGreen = Color.green(endColor);
    int newGreen = startGreen + Math.round(progressPercentage * (endGreen - startGreen));

    int startBlue = Color.blue(startColor);
    int endBlue = Color.blue(endColor);
    int newBlue = startBlue + Math.round(progressPercentage * (endBlue - startBlue));

        /* Safety checks, just in case - could be a for loop but I didn't want to deal with storing
           these in an array and implementing a way to log the correct variable name
         */
    newRed = Math.max(0, Math.min(newRed, 255));
    newGreen = Math.max(0, Math.min(newGreen, 255));
    newBlue = Math.max(0, Math.min(newBlue, 255));

    return Color.rgb(newRed, newGreen, newBlue);
  }

  public static int setAlpha(int color, float alphaPercentage) {
    return Color.argb(
        Math.round(Color.alpha(color) * alphaPercentage),
        Color.red(color),
        Color.green(color),
        Color.blue(color));
  }

  public static RectF constrainRect(RectF rect, float amount) {
    RectF copyRect = new RectF(rect); // todo: don't initialize in onDraw() - GC hurts
    copyRect.left += amount;
    copyRect.top += amount;
    copyRect.bottom -= amount;
    copyRect.right -= amount;
    return copyRect;
  }

}
