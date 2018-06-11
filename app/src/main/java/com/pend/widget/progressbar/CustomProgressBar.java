package com.pend.widget.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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
            for (int i = 0; i < mProgressItemsList.size(); i++) {
                ProgressItem progressItem = mProgressItemsList.get(i);
                Paint progressPaint = new Paint();
                progressPaint.setColor(getResources().getColor(
                        progressItem.color));

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
                if (i == mProgressItemsList.size() - 1
                        && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth;
                }
                RectF progressRect = new RectF();
                progressRect.set(lastProgressX, thumboffset / 2,
                        progressItemRight, progressBarHeight - thumboffset / 2);

                if (((int) (progressItem.progressItemPercentage)) == 100) {
                    canvas.drawRoundRect(progressRect, 30, 30, progressPaint);
                    super.onDraw(canvas);
                    return;
                }

                if (leftCurveRequired == CURVE_REQUIRED) {
                    leftCurveRequired = CURVE_NOT_REQUIRED;
                    Path path = RoundedRect(lastProgressX, thumboffset / 2, progressItemRight, progressBarHeight - thumboffset / 2, 30, 30, LEFT);
                    canvas.drawPath(path, progressPaint);
                } else if (rightCurvedRequired == CURVE_REQUIRED) {
                    Path path = RoundedRect(lastProgressX, thumboffset / 2, progressItemRight, progressBarHeight - thumboffset / 2, 30, 30, RIGHT);
                    canvas.drawPath(path, progressPaint);
                    super.onDraw(canvas);
                    return;
                } else {
                    canvas.drawRect(progressRect, progressPaint);
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
