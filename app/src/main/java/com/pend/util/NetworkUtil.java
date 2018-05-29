package com.pend.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.pend.interfaces.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class NetworkUtil {

    public static final String getSecurityKey(Context context) {
        String deviceId = "";
        String securityKey = getDeviceId(context) + Constants.SECURITY_CONSTANT;
        return getMd5Encryption(securityKey);
    }

    public static final HashMap<String, String> getHeaders(Context context) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.PARAM_SECURITY_KEY, getSecurityKey(context));
        headers.put(Constants.PARAM_DEVICE_ID, getDeviceId(context));
        return headers;
    }

    public static String getDeviceId(Context mContext) {
        String deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    /**
     * method to check whether internet connection is available or not
     *
     * @param context
     * @return
     */
    public static boolean isInternetConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connectivity != null) {
                networkInfo = connectivity.getActiveNetworkInfo();
            }
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static final String getMd5Encryption(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
