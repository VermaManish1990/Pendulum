package com.pend.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.pend.Constants;

public class SharedPrefUtils {
    private SharedPrefUtils() {
    }

    public static int getInt(Context context, final String key, int defaultValue) {
        SharedPreferences prefs = getSharedPrefrence(context);
        return prefs.getInt(key, defaultValue);
    }

    public static long getLong(Context context, final String key, long defaultValue) {
        SharedPreferences prefs = getSharedPrefrence(context);
        return prefs.getLong(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences prefs = getSharedPrefrence(context);
        prefs.edit().putLong(key, value).apply();
    }


    public static void putInt(Context context, String key, int value) {
        SharedPreferences prefs = getSharedPrefrence(context);
        prefs.edit().putInt(key, value).apply();
    }


    public static String getString(Context context, final String key, final String defaultValue) {
        SharedPreferences prefs = getSharedPrefrence(context);

        return prefs.getString(key, defaultValue);
    }

    public static void putString(Context context, String key, String value) {
        try {
            SharedPreferences prefs = getSharedPrefrence(context);
            prefs.edit().putString(key, value).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getDouble(Context context, final String key, double defaultValue) {
        SharedPreferences prefs = getSharedPrefrence(context);
        String value = prefs.getString(key, defaultValue + "");
        try {
            return Double.parseDouble(value);
        } catch (Exception x) {
            return defaultValue;
        }
    }

    public static void putDouble(Context context, String key, double value) {
        SharedPreferences prefs = getSharedPrefrence(context);
        prefs.edit().putString(key, value + "").apply();
    }

    public static boolean getBoolean(Context context, String key, final boolean defaultValue) {
        SharedPreferences prefs = getSharedPrefrence(context);
        return prefs.getBoolean(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences prefs = getSharedPrefrence(context);
        prefs.edit().putBoolean(key, value).apply();
    }


    public static void remove(Context context, String key) {
        SharedPreferences prefs = getSharedPrefrence(context);
        prefs.edit().remove(key).apply();
    }

    public static void clearAllData(Context context) {
        getSharedPrefrence(context).edit().clear().apply();
    }


    /*
     * method to get shared preference object
     * */
    private static SharedPreferences getSharedPrefrence(Context context) {
        return context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }


    public static boolean isKeyExistInPref(Context context, String key) {
        SharedPreferences prefs = getSharedPrefrence(context);
        return prefs.contains(key);
    }

    public static void logout(Context mCtx) {

    }


}
