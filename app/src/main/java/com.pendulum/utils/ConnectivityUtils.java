/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.pendulum.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> <br/>
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 */
public class ConnectivityUtils {

    private static final String LOG_TAG = "ConnectivityUtils";

    /**
     * @param pContext
     * @return
     * @note android.permission.ACCESS_NETWORK_STATE is required
     */
    public static boolean isNetworkEnabled(Context pContext) {
        NetworkInfo activeNetwork = getActiveNetwork(pContext);
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * @param pContext
     * @return
     * @note android.permission.ACCESS_NETWORK_STATE is required
     */
    public static void logNetworkState(Context pContext) {
        NetworkInfo activeNetwork = getActiveNetwork(pContext);
        if (activeNetwork == null) {
            return;
        }
    }

    /**
     * @param pContext
     * @return
     * @note android.permission.ACCESS_NETWORK_STATE is required
     */
    public static NetworkInfo getActiveNetwork(Context pContext) {
        ConnectivityManager conMngr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMngr == null ? null : conMngr.getActiveNetworkInfo();
    }

    /**
     * @param pContext
     * @param pRequiredTrailingDigits
     * @return
     * @note android.permission.READ_PHONE_STATE is required
     * @note if any positive value is supplied in pRequiredTrailingDigits then
     * method will extract only digits ignoring any non digit char if
     * found in Line1Number provided by telephony manager. <br/>
     * @example if Line1Number is "+91-813-068-78-92", passing 10 as
     * pRequiredTrailingDigits will provide 8130687892;
     */
    public static String getMobileNumber(Context pContext, int pRequiredTrailingDigits) {
        TelephonyManager telephonyMngr = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
        String mobileNumber = telephonyMngr.getLine1Number();
        mobileNumber = mobileNumber == null ? "" : mobileNumber.trim();
        int mobileNumberLength = mobileNumber.length();
        if (mobileNumberLength == 0 || pRequiredTrailingDigits <= 0) {
            return mobileNumber;
        }
        for (int i = 0; i < mobileNumberLength; i++) {
            if (Character.isDigit(mobileNumber.charAt(i))) {
                continue;
            }
            mobileNumber = mobileNumber.replace("" + mobileNumber.charAt(i), "");
            mobileNumberLength = mobileNumber.length();
            i--;
        }
        if (mobileNumberLength <= pRequiredTrailingDigits) {
            return mobileNumber;
        }
        return mobileNumber.substring(mobileNumberLength - pRequiredTrailingDigits);
    }

    /**
     * @param pContext
     * @return
     */
    public static boolean isSimOperatorIndian(Context pContext) {
        TelephonyManager telephonyMngr = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
        String simIsoCode = telephonyMngr.getSimCountryIso();
        if (StringUtils.isNullOrEmpty(simIsoCode)) {
            return true;
        } else {
            return "IN".equalsIgnoreCase(simIsoCode);
        }
    }

    /**
     * @param pContext
     * @return
     * @note android.permission.READ_PHONE_STATE is required
     */
    public static String getDeviceId(Context pContext) {
        TelephonyManager telephonyManager = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}