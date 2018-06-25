package com.pend.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtherUtil {

    /**
     * Method to set Spannable Click or your make asterisk red also
     *
     * @param context                context
     * @param textView               text view to span
     * @param startIndex             start index to span
     * @param endIndex               end index to span
     * @param boldValue              value from 1.0 to 1.5 to  make bold else set 1.0
     * @param iClickAbleSpanCallback IClickAbleSpanCallback object
     */
    @SuppressWarnings("deprecation")
    public static void setSpannableClick(Context context, TextView textView, int startIndex, int endIndex, float boldValue, int colorId, final IClickAbleSpanCallback iClickAbleSpanCallback) {
        LoggerUtil.v(OtherUtil.class.getSimpleName(), "setSpannableClick");
        if (context == null) {
            LoggerUtil.e(OtherUtil.class.getSimpleName(), "context is null");
            return;
        } else if (textView == null) {
            LoggerUtil.e(OtherUtil.class.getSimpleName(), "TextView is null");
            return;
        }

        String str = textView.getText().toString();
        if (str.length() <= startIndex) {
            LoggerUtil.e(OtherUtil.class.getSimpleName(), "start index is out of bound");
            return;
        } else if (str.length() < startIndex) {
            LoggerUtil.e(OtherUtil.class.getSimpleName(), "end Index is out of bound");
            return;
        }


        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String startSpan = str.substring(0, startIndex);
        String middleSpan = str.substring(startIndex, endIndex);
        String endSpans = str.substring(endIndex, str.length());
        textView.setText("");

        SpannableStringBuilder spanString = new SpannableStringBuilder();
        spanString.append(startSpan);
        spanString.append(middleSpan);
        spanString.append(endSpans);
        spanString.setSpan(new RelativeSizeSpan(boldValue), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        class MyClickableSpan extends ClickableSpan {

            private MyClickableSpan() {
                super();
            }

            public void onClick(View tv) {
                if (iClickAbleSpanCallback != null) {
                    iClickAbleSpanCallback.onSpanClick();
                } else {
                    LoggerUtil.e(OtherUtil.class.getSimpleName(), "IClickAbleSpanCallback is null");
                }
            }

            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false); // set to false to remove underline
            }
        }

        spanString.setSpan(new MyClickableSpan(), startIndex, endIndex, 0);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(colorId)), startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.append(spanString);
    }

    /**
     * Callback interface for clickable interface
     */
    public interface IClickAbleSpanCallback {
        void onSpanClick();
    }

    public static String getBase64Format(String imagePath) {

        try {
            Bitmap bm = Bitmap.createScaledBitmap (BitmapFactory.decodeFile(imagePath), 150, 150, false);
//            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();

            return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method is used to replace multiple spaces with given replace string.
     *
     * @param str     str
     * @param replace replace
     * @return String
     */
    public static String replaceWithPattern(String str, String replace) {
        Pattern ptn = Pattern.compile("\\s+");
        Matcher mtch = ptn.matcher(str);
        return mtch.replaceAll(replace);
    }

    /**
     * Method is used to get screen width in pixel.
     *
     * @param context Activity context
     * @return int
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * Method is used to get screen height in pixel.
     *
     * @param context Activity context
     * @return int
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
