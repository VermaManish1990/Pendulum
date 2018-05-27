/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.pendulum.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;

/**
 * Utility class for UI related methods.
 */
public class UiUtils {

    /**
     * @param pContext
     */
    public static String getDeviceDensityGroupName(Context pContext) {
        int densityDpi = pContext.getResources().getDisplayMetrics().densityDpi;
        String densityGroupNameStr = "xhdpi";
        switch (densityDpi) {
            case DisplayMetrics.DENSITY_LOW: {
                densityGroupNameStr = "ldpi";
                break;
            }
            case DisplayMetrics.DENSITY_MEDIUM: {
                densityGroupNameStr = "mdpi";
                break;
            }
            case DisplayMetrics.DENSITY_HIGH: {
                densityGroupNameStr = "hdpi";
                break;
            }
            case DisplayMetrics.DENSITY_XHIGH: {
                densityGroupNameStr = "xhdpi";
                break;
            }
            case DisplayMetrics.DENSITY_XXHIGH: {
                densityGroupNameStr = "xxhdpi";
                break;
            }
        }
        return densityGroupNameStr;
    }

    /**
     * @param event
     * @return
     */
    public static String getEventDetails(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                break;
            default:
                action = "ACTION_" + event.getAction();
                break;
        }
        return action + ": " + event.getX() + "-" + event.getY();
    }

    /**
     * @param context
     * @return
     */
    public static float getPixels(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dp * dm.density;
    }

    /**
     * @param gridView
     * @param numColumns
     * @param verticalSpacing
     * @note method is not working fine
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView, int numColumns, int verticalSpacing) {
        Adapter adapter = gridView.getAdapter();
        if (adapter == null) {
            return;
        }
        int numItems = gridView.getCount();
        int numRows = (numItems + numColumns - 1) / numColumns;
        int totalHeight = 0;
        for (int row = 0; row < numRows; row++) {
            int rowHeight = 0;
            int itemHeight = 0;
            for (int column = 0; column < numColumns; column++) {
                int itemIndex = row * numColumns + column;
                if (itemIndex >= numItems) {
                    break;
                }
                View gridItem = adapter.getView(itemIndex, null, gridView);
                gridItem.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                itemHeight = gridItem.getMeasuredHeight();
                if (itemHeight > rowHeight) {
                    rowHeight = itemHeight;
                }
            }
            totalHeight += rowHeight;
            totalHeight += verticalSpacing;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = (totalHeight);
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    /**
     * method use to expand a view smoothly
     *
     * @param v
     */
    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT : (int) (targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density)));
        v.startAnimation(a);
    }

    /**
     * method use to collapse a view smoothly
     *
     * @param v
     */
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)));
        v.startAnimation(a);
    }
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}
