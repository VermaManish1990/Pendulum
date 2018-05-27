
/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.pendulum.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.Hashtable;

/**
 * @author sachin.gupta
 */
public class FontUtils {

    private static final Hashtable<String, SoftReference<Typeface>> mFontCache;

    static {
        mFontCache = new Hashtable<String, SoftReference<Typeface>>();
    }

    /**
     * @param pContext
     * @param pFontFilePathInAssets
     * @return
     */
    public static Typeface getFont(Context pContext, String pFontFilePathInAssets) {
        synchronized (mFontCache) {
            if (mFontCache.get(pFontFilePathInAssets) != null) {
                SoftReference<Typeface> ref = mFontCache.get(pFontFilePathInAssets);
                if (ref.get() != null) {
                    return ref.get();
                }
            }

            Typeface typeface = Typeface.createFromAsset(pContext.getAssets(), pFontFilePathInAssets);
            mFontCache.put(pFontFilePathInAssets, new SoftReference<Typeface>(typeface));
            return typeface;
        }
    }

    /**
     * @param pContext
     * @param pFontFileNameInAssets
     * @return true if font is applied, false otherwise
     */
    public static boolean changeDeviceTypeface(Context pContext, String pStaticFieldName, String pFontFileNameInAssets) {
        try {
            Field StaticField = Typeface.class.getDeclaredField(pStaticFieldName);
            StaticField.setAccessible(true);
            Typeface newTypeface = getFont(pContext, pFontFileNameInAssets);
            StaticField.set(null, newTypeface);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param pView
     * @param pContext
     * @param pAppliedAttrSet
     * @param pViewsAllAttrIdsArr
     * @param pFontAttributeId
     * @return true if font is applied, false otherwise
     */
    public static boolean setCustomFont(View pView, Context pContext, AttributeSet pAppliedAttrSet, int[] pViewsAllAttrIdsArr, int pFontAttributeId) {
        TypedArray typedArr = pContext.obtainStyledAttributes(pAppliedAttrSet, pViewsAllAttrIdsArr);
        String fontFileNameInAssets = typedArr.getString(pFontAttributeId);
        boolean isFontSet = setCustomFont(pView, pContext, fontFileNameInAssets);
        typedArr.recycle();
        return isFontSet;
    }

    /**
     * @param pView
     * @param pContext
     * @param pFontFileNameInAssets
     * @return
     */
    public static boolean setCustomFont(View pView, Context pContext, String pFontFileNameInAssets) {
        if (TextUtils.isEmpty(pFontFileNameInAssets)) {
            return false;
        }
        try {
            Typeface tf = getFont(pContext, pFontFileNameInAssets);
            if (pView instanceof TextView) {
                ((TextView) pView).setTypeface(tf);
            } else {
                ((Button) pView).setTypeface(tf);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static SpannableStringBuilder getMandatoryFieldSpan(String label,
                                                               int redColor) {
        SpannableStringBuilder span = new SpannableStringBuilder();
        span.append(label);
        int start = span.length();
        span.append("*");
        span.setSpan(new ForegroundColorSpan(redColor), start, span.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(Typeface.BOLD), start,
                span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
}
