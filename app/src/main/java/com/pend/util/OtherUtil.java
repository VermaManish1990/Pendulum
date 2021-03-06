package com.pend.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.ErrorResponseModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Method is used to get Base64 format of Image.
     *
     * @param imagePath imagePath
     * @return String
     */
    public static String getBase64Format(String imagePath) {

     /*   try {
    //        Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imagePath), 720, 480, false);
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();

            return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
    /*    try {
            InputStream inputStream = new FileInputStream(imagePath);
            System.out.println("Image Path "+imagePath);//You can get an inputStream using any IO API
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            bytes = output.toByteArray();
            String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
            return encodedString;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }*/

        try {

            File imagefile = new File(imagePath);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(imagefile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);
            //Base64.de
            return encImage;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method is used to get Base64 format of Image.
     *
     * @param bitmap imagePath
     * @return String
     */
    public static String getBase64FormatFromBitmap(Bitmap bitmap) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
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

    /**
     * Method to show Alert Dialog with only positive button with callback
     *
     * @param message       message for alert
     * @param context       context
     * @param clickListener Callback for ok
     */
    public static void showAlertDialog(String message, Context context, DialogInterface.OnClickListener clickListener) {
        LoggerUtil.v(OtherUtil.class.getSimpleName(), "showAlertDialog");
        if (context == null || ((Activity) context).isDestroyed() || ((Activity) context).isFinishing()) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(context.getString(R.string.ok), clickListener);
        builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(false);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        if (!((Activity) context).isDestroyed() && !((Activity) context).isFinishing()) {
            builder.show();
        }
    }

    /**
     * Method to show Alert Dialog with positive and negative button with callback
     *
     * @param message             message for alert
     * @param context             context
     * @param clickListenerOk     Callback for ok
     * @param clickListenerCancel callback for cancel
     */
    public static void showAlertDialog(String message, Context context, DialogInterface.OnClickListener clickListenerOk, DialogInterface.OnClickListener clickListenerCancel) {
        LoggerUtil.v(OtherUtil.class.getSimpleName(), "showAlertDialog");
        if (context == null || ((Activity) context).isDestroyed() || ((Activity) context).isFinishing()) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(context.getString(R.string.ok), clickListenerOk);
        builder.setNegativeButton(context.getString(R.string.cancel), clickListenerCancel);
        builder.setMessage(Html.fromHtml(message));
        builder.setCancelable(false);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        if (!((Activity) context).isDestroyed() && !((Activity) context).isFinishing()) {
            builder.show();
        }
    }

    /**
     * Method is used to show error message when api fail or give some error message.
     *
     * @param context         context
     * @param serviceResponse serviceResponse
     */
    public static void showErrorMessage(Context context, Object serviceResponse) {
        String errorMessage;
        if (serviceResponse instanceof ErrorResponseModel) {
            ErrorResponseModel errorResponseModel = (ErrorResponseModel) serviceResponse;
            if (errorResponseModel.error != null && errorResponseModel.error.additionalMessage != null) {

                errorMessage = errorResponseModel.error.additionalMessage;
            } else {
                errorMessage = context.getString(R.string.server_error_from_api);
            }
        } else if (serviceResponse instanceof String) {
            errorMessage = (String) serviceResponse;
        } else {
            errorMessage = context.getString(R.string.server_error_from_api);
        }

        OtherUtil.showAlertDialog(errorMessage != null ? errorMessage : context.getString(R.string.server_error_from_api), context, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Method is used to open keyboard.
     */
    public static void openKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /**
     * Method is used to take a screen of a given view.
     *
     * @param view view
     * @return bitmap of a view.
     */
    public static Bitmap loadBitmapFromView(View view) {
        Bitmap bitmap;
        view.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
