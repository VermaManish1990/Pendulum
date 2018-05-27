/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.pendulum.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * class for device info specific methods
 *
 * @author sachin.gupta
 */
public class KeypadUtils {

    /**
     * shows soft keypad on screen
     *
     * @param activity
     * @param view
     */
    public static void showSoftKeypad(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * hides the soft keypad from screen
     *
     * @param pActivity
     */
    public static void hideSoftKeypad(Activity pActivity) {
        if (pActivity.getWindow() != null
                && pActivity.getWindow().getCurrentFocus() != null) {
            hideSoftKeypad(pActivity, pActivity.getWindow().getCurrentFocus());
        }
    }

    /**
     * hides the soft keypad from screen
     *
     * @param pActivity
     * @param view
     */
    public static void hideSoftKeypad(Activity pActivity, View view) {
        InputMethodManager imm = (InputMethodManager) pActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void hideKeyboardOnDialog(Activity pActivity) {
        InputMethodManager imm = (InputMethodManager) pActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pActivity.getWindow().getDecorView()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
