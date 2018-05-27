package com.pendulum.ui.widget;///*
// *
// *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
// *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
// *
// */
//
//package com.com.taxi.ui.widget;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.widget.Button;
//
//import com.bluestar.BuildConfig;
//import com.bluestar.R;
//import com.com.taxi.utils.FontUtils;
//
///**
// * @author sachin.gupta
// */
//public class CustomButton extends Button {
//
//    private static String LOG_TAG = "CustomTextView";
//
//    /**
//     * @param context
//     */
//    public CustomButton(Context context) {
//        super(context);
//    }
//
//    /**
//     * @param context
//     * @param attrs
//     */
//    public CustomButton(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    /**
//     * @param context
//     * @param attrs
//     * @param defStyle
//     */
//    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(context, attrs);
//    }
//
//    /**
//     * @param context
//     * @param attrs
//     */
//    private void init(Context context, AttributeSet attrs) {
//        int[] viewsAllAttrIdsArr = R.styleable.com_kelltontech_ui_widget_CustomTextView;
//        int fontAttributeId = R.styleable.com_kelltontech_ui_widget_CustomTextView_fontAssetName;
//        boolean isFontSet = FontUtils.setCustomFont(this, context, attrs, viewsAllAttrIdsArr, fontAttributeId);
//        if (!isFontSet && BuildConfig.DEBUG) {
//            Log.e(LOG_TAG, "Failed to set custom font.");
//        }
//    }
//}
