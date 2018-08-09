package com.pend.widget.progressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;

import java.util.ArrayList;

public class CustomProgressBar extends android.support.v7.widget.AppCompatSeekBar {

    private ArrayList<ProgressItem> mProgressItemsList;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int CURVE_NOT_USED = 0;
    private static final int CURVE_REQUIRED = 1;
    private static final int CURVE_NOT_REQUIRED = 2;


    public CustomProgressBar(Context context) {
        super(context);
        mProgressItemsList = new ArrayList<>();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressItemsList = new ArrayList<>();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mProgressItemsList = new ArrayList<>();
    }

    public void initData(ArrayList<ProgressItem> progressItemsList) {
        this.mProgressItemsList = progressItemsList;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        if (mProgressItemsList.size() > 0) {
            int progressBarWidth = getWidth();
            int progressBarHeight = getHeight();
            int thumboffset = getThumbOffset();
            int lastProgressX = 0;
            int progressItemWidth, progressItemRight;
            int leftCurveRequired = 0;
            int rightCurvedRequired = 0;
            int totalPercentage = 0;
            int xPos;
            int yPos;

            for (int i = 0; i < mProgressItemsList.size(); i++) {
                ProgressItem progressItem = mProgressItemsList.get(i);

                @SuppressLint("DrawAllocation") Paint progressPaint = new Paint();
                progressPaint.setColor(progressItem.color);

                progressItemWidth = (int) (progressItem.progressItemPercentage
                        * progressBarWidth / 100);

                progressItemRight = lastProgressX + progressItemWidth;

                totalPercentage = totalPercentage + ((int) progressItem.progressItemPercentage);

                if (leftCurveRequired == CURVE_NOT_USED && ((int) progressItem.progressItemPercentage) > 0)
                    leftCurveRequired = CURVE_REQUIRED;

                if (rightCurvedRequired == CURVE_NOT_USED && ((int) progressItem.progressItemPercentage) > 0 && totalPercentage == 100) {
                    rightCurvedRequired = CURVE_REQUIRED;
                }

                // for last item give right to progress item to the width
                if (i == mProgressItemsList.size() - 1 && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth;
                }
                @SuppressLint("DrawAllocation") RectF progressRect = new RectF();
                progressRect.set(lastProgressX, thumboffset / 2,
                        progressItemRight, progressBarHeight - thumboffset / 2);

                // when one item is available
                if (((int) (progressItem.progressItemPercentage)) == 100) {
                    canvas.drawRoundRect(progressRect, 10, 10, progressPaint);

                    //set text on canvas
                    if (((int) (progressItem.progressItemPercentage)) > 0) {
                        drawText(canvas, String.valueOf((int) (progressItem.progressItemPercentage) + "%"), lastProgressX + (progressItemWidth / 2));
                    }

                    super.onDraw(canvas);
                    return;
                }

                if (leftCurveRequired == CURVE_REQUIRED) {
                    leftCurveRequired = CURVE_NOT_REQUIRED;
                    Path path = RoundedRect(lastProgressX, thumboffset / 2, progressItemRight, progressBarHeight - thumboffset / 2, 10, 10, LEFT);
                    canvas.drawPath(path, progressPaint);

                    //set text on canvas
                    if (((int) (progressItem.progressItemPercentage)) > 0) {
                        drawText(canvas, String.valueOf((int) (progressItem.progressItemPercentage) + "%"), lastProgressX + (progressItemWidth / 2));
                    }

                } else if (rightCurvedRequired == CURVE_REQUIRED) {
                    Path path = RoundedRect(lastProgressX, thumboffset / 2, progressItemRight, progressBarHeight - thumboffset / 2, 10, 10, RIGHT);
                    canvas.drawPath(path, progressPaint);

                    //set text on canvas
                    if (((int) (progressItem.progressItemPercentage)) > 0) {
                        drawText(canvas, String.valueOf((int) (progressItem.progressItemPercentage) + "%"), lastProgressX + (progressItemWidth / 2));
                    }

                    super.onDraw(canvas);
                    return;

                } else {
                    canvas.drawRect(progressRect, progressPaint);

                    //set text on canvas
                    if (((int) (progressItem.progressItemPercentage)) > 0) {
                        drawText(canvas, String.valueOf((int) (progressItem.progressItemPercentage) + "%"), lastProgressX + (progressItemWidth / 2));
                    }
                }

                lastProgressX = progressItemRight;
            }
//			Resources res = getResources();
//			Paint progressPaint = new Paint();
//			Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.back);
//			canvas.drawBitmap(bitmap, 0, 0, progressPaint);

            super.onDraw(canvas);
        }

    }

    /**
     * Method is used to draw text on canvas
     *
     * @param canvas canvas
     * @param text   text
     * @param xPos   lastProgressX + (progressItemWidth / 2)
     */
    private void drawText(Canvas canvas, String text, int xPos) {
        @SuppressLint("DrawAllocation") Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create("Arial", Typeface.NORMAL));
        textPaint.setTextSize(18);

        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
        canvas.drawText(text, xPos, yPos, textPaint);
    }

    /**
     * Method is used to draw round rect with choice either left round or right round.
     *
     * @param left        left
     * @param top         top
     * @param right       right
     * @param bottom      bottom
     * @param rx          rx
     * @param ry          ry
     * @param leftOrRight if leftOrRight value is LEFT then round only left portion else right portion.
     * @return Path
     */
    static public Path RoundedRect(float left, float top, float right, float bottom, float rx, float ry, int leftOrRight) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        if (leftOrRight == LEFT) {
            // left side
            path.moveTo(right, top + ry);
            path.rLineTo(0, -ry);//top-right corner
            path.rLineTo(-widthMinusCorners, 0);
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
            path.rLineTo(0, heightMinusCorners);
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.rLineTo(0, -ry); //bottom-right corner
            path.rLineTo(0, -heightMinusCorners);
        } else {
            // right side
            path.moveTo(right, top + ry);
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
            path.rLineTo(-width, 0);
            path.rLineTo(0, ry); //top-left corner
            path.rLineTo(0, heightMinusCorners);
            path.rLineTo(0, ry);//bottom-left corner
            path.rLineTo(width, 0);
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
            path.rLineTo(0, -heightMinusCorners);
        }
        path.close();//Given close, last lineto can be removed.

        return path;
    }

}
