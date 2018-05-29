package com.pend.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import com.pend.R;

public class RoundedCornerImageView extends android.support.v7.widget.AppCompatImageView {

    public static final String TAG = RoundedCornerImageView.class.getSimpleName();

    private int mResource = 0;

    private static final ScaleType[] sScaleTypeArray = {
        ScaleType.MATRIX,
        ScaleType.FIT_XY,
        ScaleType.FIT_START,
        ScaleType.FIT_CENTER,
        ScaleType.FIT_END,
        ScaleType.CENTER,
        ScaleType.CENTER_CROP,
        ScaleType.CENTER_INSIDE
    };

    // Set default scale type to FIT_CENTER, which is default scale type of
    // original ImageView.
    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    private float mLeftTopCornerRadius = 0.0f;
    private float mRightTopCornerRadius = 0.0f;
    private float mLeftBottomCornerRadius = 0.0f;
    private float mRightBottomCornerRadius = 0.0f;

    private float mBorderWidth = 0.0f;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private ColorStateList mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);

    private boolean isOval = false;

    private Drawable mDrawable;

    private float[] mRadii = new float[] { 0, 0, 0, 0, 0, 0, 0, 0 };

    public RoundedCornerImageView(Context context) {
        super(context);
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundedImageView, defStyle, 0);

        final int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(sScaleTypeArray[index]);
        }

        mLeftTopCornerRadius = a.getDimensionPixelSize(
                R.styleable.RoundedImageView_fk_left_top_corner_radius, 0);
        mRightTopCornerRadius = a.getDimensionPixelSize(
                R.styleable.RoundedImageView_fk_right_top_corner_radius, 0);
        mLeftBottomCornerRadius = a.getDimensionPixelSize(
                R.styleable.RoundedImageView_fk_left_bottom_corner_radius, 0);
        mRightBottomCornerRadius = a.getDimensionPixelSize(
                R.styleable.RoundedImageView_fk_right_bottom_corner_radius, 0);
        
        if (mLeftTopCornerRadius < 0.0f || mRightTopCornerRadius < 0.0f
                || mLeftBottomCornerRadius < 0.0f || mRightBottomCornerRadius < 0.0f) {
            throw new IllegalArgumentException("radius values cannot be negative.");
        }
        
        mRadii = new float[] { 
                mLeftTopCornerRadius, mLeftTopCornerRadius,
                mRightTopCornerRadius, mRightTopCornerRadius, 
                mRightBottomCornerRadius, mRightBottomCornerRadius, 
                mLeftBottomCornerRadius, mLeftBottomCornerRadius };

        mBorderWidth = a.getDimensionPixelSize(
                R.styleable.RoundedImageView_fk_border_width, 0);
        if (mBorderWidth < 0) {
            throw new IllegalArgumentException("border width cannot be negative.");
        }

        mBorderColor = a
                .getColorStateList(R.styleable.RoundedImageView_fk_border_color);
        if (mBorderColor == null) {
            mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
        }

        isOval = a.getBoolean(R.styleable.RoundedImageView_fk_oval, false);
        a.recycle();
        
        updateDrawable();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        mScaleType = scaleType;
        updateDrawable();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        mResource = 0;
        mDrawable = SelectableRoundedCornerDrawable.fromDrawable(drawable, getResources());
        super.setImageDrawable(mDrawable);
        updateDrawable();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mResource = 0;
        mDrawable = SelectableRoundedCornerDrawable.fromBitmap(bm, getResources());
        super.setImageDrawable(mDrawable);
        updateDrawable();
    }

    @Override
    public void setImageResource(int resId) {
        if (mResource != resId) {
            mResource = resId;
            mDrawable = resolveResource();
            super.setImageDrawable(mDrawable);
            updateDrawable();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    @SuppressWarnings("deprecation")
    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }

        Drawable d = null;

        if (mResource != 0) {
            try {
                d = rsrc.getDrawable(mResource);
            } catch (NotFoundException e) {
                Log.w(TAG, "Unable to find resource: " + mResource, e);
                // Don't try again.
                mResource = 0;
            }
        }
        return SelectableRoundedCornerDrawable.fromDrawable(d, getResources());
    }

    private void updateDrawable() {
        if (mDrawable == null) {
            return;
        }

        ((SelectableRoundedCornerDrawable) mDrawable.getCurrent()).setScaleType(mScaleType);
        ((SelectableRoundedCornerDrawable) mDrawable.getCurrent()).setCornerRadii(mRadii);
        ((SelectableRoundedCornerDrawable) mDrawable.getCurrent()).setBorderWidth(mBorderWidth);
        ((SelectableRoundedCornerDrawable) mDrawable.getCurrent()).setBorderColor(mBorderColor);
        ((SelectableRoundedCornerDrawable) mDrawable.getCurrent()).setOval(isOval);
    }

      private static class SelectableRoundedCornerDrawable extends Drawable {

        private static final String TAG = SelectableRoundedCornerDrawable.class.getSimpleName();
        private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

        private RectF mBounds = new RectF();
        private RectF mBorderBounds = new RectF();

        private final RectF mBitmapRect = new RectF();
        private final int mBitmapWidth;
        private final int mBitmapHeight;

        private final Paint mBitmapPaint;
        private final Paint mBorderPaint;

        private BitmapShader mBitmapShader;

        private float[] mRadii = new float[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        private float[] mBorderRadii = new float[] { 0, 0, 0, 0, 0, 0, 0, 0 };

        private boolean mOval = false;

        private float mBorderWidth = 0;
        private ColorStateList mBorderColor = ColorStateList.valueOf(DEFAULT_BORDER_COLOR);
        private ScaleType mScaleType = ScaleType.FIT_CENTER;

        private Path mPath = new Path();
        private Bitmap mBitmap;
        private boolean mBoundsConfigured = false;

        SelectableRoundedCornerDrawable(Bitmap bitmap, Resources r) {
            mBitmap = bitmap;
            mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            mBitmapWidth = bitmap.getScaledWidth(r.getDisplayMetrics());
            mBitmapHeight = bitmap.getScaledHeight(r.getDisplayMetrics());

            mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBitmapPaint.setStyle(Paint.Style.FILL);
            mBitmapPaint.setShader(mBitmapShader);

            mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setColor(mBorderColor.getColorForState(getState(), DEFAULT_BORDER_COLOR));
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }

        static SelectableRoundedCornerDrawable fromBitmap(Bitmap bitmap, Resources r) {
            if (bitmap != null) {
                return new SelectableRoundedCornerDrawable(bitmap, r);
            } else {
                return null;
            }
        }

        static Drawable fromDrawable(Drawable drawable, Resources r) {
            if (drawable != null) {
                if (drawable instanceof SelectableRoundedCornerDrawable) {
                    return drawable;
                } else if (drawable instanceof LayerDrawable) {
                    LayerDrawable ld = (LayerDrawable) drawable;
                    final int num = ld.getNumberOfLayers();
                    for (int i = 0; i < num; i++) {
                        Drawable d = ld.getDrawable(i);
                        ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d, r));
                    }
                    return ld;
                }

                Bitmap bm = drawableToBitmap(drawable);
                if (bm != null) {
                    return new SelectableRoundedCornerDrawable(bm, r);
                } else {
                    Log.w(TAG, "Failed to create bitmap from drawable!");
                }
            }
            return drawable;
        }

        static Bitmap drawableToBitmap(Drawable drawable) {
            if (drawable == null) {
                return null;
            }

            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            Bitmap bitmap;
            int width = Math.max(drawable.getIntrinsicWidth(), 2);
            int height = Math.max(drawable.getIntrinsicHeight(), 2);
            try {
                bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                bitmap = null;
            }
            return bitmap;
        }

        @Override
        public boolean isStateful() {
            return mBorderColor.isStateful();
        }

        @Override
        protected boolean onStateChange(int[] state) {
            int newColor = mBorderColor.getColorForState(state, 0);
            if (mBorderPaint.getColor() != newColor) {
                mBorderPaint.setColor(newColor);
                return true;
            } else {
                return super.onStateChange(state);
            }
        }

        @SuppressWarnings("deprecation")
        private void configureBounds(Canvas canvas) {
            Rect clipBounds = canvas.getClipBounds();
            Matrix canvasMatrix = canvas.getMatrix();

            if (ScaleType.CENTER == mScaleType) {
                mBounds.set(clipBounds);
            } else if (ScaleType.CENTER_CROP == mScaleType) {
                applyScaleToRadii(canvasMatrix);
                mBounds.set(clipBounds);
            } else if (ScaleType.FIT_XY == mScaleType) {
                Matrix m = new Matrix();
                m.setRectToRect(mBitmapRect, new RectF(clipBounds), Matrix.ScaleToFit.FILL);
                mBitmapShader.setLocalMatrix(m);
                mBounds.set(clipBounds);
            } else if (ScaleType.FIT_START == mScaleType || ScaleType.FIT_END == mScaleType
                    || ScaleType.FIT_CENTER == mScaleType || ScaleType.CENTER_INSIDE == mScaleType) {
                applyScaleToRadii(canvasMatrix);
                mBounds.set(mBitmapRect);
            } else if (ScaleType.MATRIX == mScaleType) {
                applyScaleToRadii(canvasMatrix);
                mBounds.set(mBitmapRect);
            }
        }

        private void applyScaleToRadii(Matrix m) {
            float[] values = new float[9];
            m.getValues(values);
            for (int i = 0; i < mRadii.length; i++) {
                mRadii[i] = mRadii[i] / values[0];
            }
        }

        @SuppressWarnings("deprecation")
        private void adjustCanvasForBorder(Canvas canvas) {
            Matrix canvasMatrix = canvas.getMatrix();
            final float[] values = new float[9];
            canvasMatrix.getValues(values);

            final float scaleFactorX = values[0];
            final float scaleFactorY = values[4];
            final float translateX = values[2];
            final float translateY = values[5];

            final float newScaleX = mBounds.width()
                    / (mBounds.width() + mBorderWidth + mBorderWidth);
            final float newScaleY = mBounds.height()
                    / (mBounds.height() + mBorderWidth + mBorderWidth);

            canvas.scale(newScaleX, newScaleY);
            if (ScaleType.FIT_START == mScaleType || ScaleType.FIT_END == mScaleType
                    || ScaleType.FIT_XY == mScaleType || ScaleType.FIT_CENTER == mScaleType
                    || ScaleType.CENTER_INSIDE == mScaleType || ScaleType.MATRIX == mScaleType) {
                canvas.translate(mBorderWidth, mBorderWidth);
            } else if (ScaleType.CENTER == mScaleType || ScaleType.CENTER_CROP == mScaleType) {
                canvas.translate(
                        -translateX / (newScaleX * scaleFactorX), 
                        -translateY / (newScaleY * scaleFactorY));
                canvas.translate(-(mBounds.left - mBorderWidth), -(mBounds.top - mBorderWidth));
            } 
        }

        @SuppressWarnings("deprecation")
        private void adjustBorderWidthAndBorderBounds(Canvas canvas) {
            Matrix canvasMatrix = canvas.getMatrix();
            final float[] values = new float[9];
            canvasMatrix.getValues(values);

            final float scaleFactor = values[0];

            float viewWidth = mBounds.width() * scaleFactor;
            mBorderWidth = (mBorderWidth * mBounds.width()) / (viewWidth - (2 * mBorderWidth));
            mBorderPaint.setStrokeWidth(mBorderWidth);

            mBorderBounds.set(mBounds);
            mBorderBounds.inset(- mBorderWidth / 2, - mBorderWidth / 2);
        }

        private void setBorderRadii() {
            for (int i = 0; i < mRadii.length; i++) {
                if (mRadii[i] > 0) {
                    mBorderRadii[i] = mRadii[i];
                    mRadii[i] = mRadii[i] - mBorderWidth;
                }
            }
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            if (!mBoundsConfigured) {
                configureBounds(canvas);
                if (mBorderWidth > 0) {
                    adjustBorderWidthAndBorderBounds(canvas);
                    setBorderRadii();
                }
                mBoundsConfigured = true;
            }

            if (mOval) {
                if (mBorderWidth > 0) {
                    adjustCanvasForBorder(canvas);
                    mPath.addOval(mBounds, Path.Direction.CW);
                    canvas.drawPath(mPath, mBitmapPaint);
                    mPath.reset();
                    mPath.addOval(mBorderBounds, Path.Direction.CW);
                    canvas.drawPath(mPath, mBorderPaint);
                } else {
                    mPath.addOval(mBounds, Path.Direction.CW);
                    canvas.drawPath(mPath, mBitmapPaint);
                }
            } else {
                if (mBorderWidth > 0) {
                    adjustCanvasForBorder(canvas);
                    mPath.addRoundRect(mBounds, mRadii, Path.Direction.CW);
                    canvas.drawPath(mPath, mBitmapPaint);
                    mPath.reset();
                    mPath.addRoundRect(mBorderBounds, mBorderRadii, Path.Direction.CW);
                    canvas.drawPath(mPath, mBorderPaint);
                } else {
                    mPath.addRoundRect(mBounds, mRadii, Path.Direction.CW);
                    canvas.drawPath(mPath, mBitmapPaint);
                }
            }
            canvas.restore();
        }

        void setCornerRadii(float[] radii) {
            if (radii == null)
                return;

            if (radii.length != 8) {
                throw new ArrayIndexOutOfBoundsException("radii[] needs 8 values");
            }

            System.arraycopy(radii, 0, mRadii, 0, radii.length);
        }

        @Override
        public int getOpacity() {
            return (mBitmap == null || mBitmap.hasAlpha() || mBitmapPaint.getAlpha() < 255) ? PixelFormat.TRANSLUCENT
                    : PixelFormat.OPAQUE;
        }

        @Override
        public void setAlpha(int alpha) {
            mBitmapPaint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mBitmapPaint.setColorFilter(cf);
            invalidateSelf();
        }

        @Override
        @SuppressWarnings("deprecation")
        public void setDither(boolean dither) {
            mBitmapPaint.setDither(dither);
            invalidateSelf();
        }

        @Override
        public void setFilterBitmap(boolean filter) {
            mBitmapPaint.setFilterBitmap(filter);
            invalidateSelf();
        }

        @Override
        public int getIntrinsicWidth() {
            return mBitmapWidth;
        }

        @Override
        public int getIntrinsicHeight() {
            return mBitmapHeight;
        }

        void setBorderWidth(float width) {
            mBorderWidth = width;
            mBorderPaint.setStrokeWidth(width);
        }

        void setBorderColor(ColorStateList colors) {
            if (colors == null) {
                mBorderWidth = 0;
                mBorderColor = ColorStateList.valueOf(Color.TRANSPARENT);
                mBorderPaint.setColor(Color.TRANSPARENT);
            } else {
                mBorderColor = colors;
                mBorderPaint.setColor(mBorderColor.getColorForState(getState(),
                        DEFAULT_BORDER_COLOR));
            }
        }

        void setOval(boolean oval) {
            mOval = oval;
        }

        public ScaleType getScaleType() {
            return mScaleType;
        }

        public void setScaleType(ScaleType scaleType) {
            if (scaleType == null) {
                return;
            }
            mScaleType = scaleType;
        }
    }

}