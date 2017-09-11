package ca.six.ui.others.colormask;

import android.graphics.Color;

public class ColorsUtil {
  public static boolean isLight(int color) {
    return Math.sqrt(
        Color.red(color) * Color.red(color) * 0.241 +
            Color.green(color) * Color.green(color) * 0.691 +
            Color.blue(color) * Color.blue(color) * 0.068) > 130;
  }

  public static int getBaseColor(int color) {
    if (isLight(color)) {
      return Color.BLACK;
    }
    return Color.WHITE;
  }
}