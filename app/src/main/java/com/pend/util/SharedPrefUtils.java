package com.pend.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

    private static final String PENDULUM_SHARED_PREF = "PENDULUM_SHARED_PREF";
    private static final String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
    private static final String USER_ID = "USER_ID";
    private static final String IS_DATA_BASE_COPIED = "IS_DATA_BASE_COPIED";

    /**
     * Method to get is user logged in or not
     *
     * @param context application context
     * @return is user logged in or not if null then false
     */
    public static boolean isUserLoggedIn(Context context) {
        LoggerUtil.v(SharedPrefUtils.class.getSimpleName(), "isUserLoggedIn");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PENDULUM_SHARED_PREF, Context.MODE_PRIVATE);
            return sharedPref.getBoolean(IS_USER_LOGGED_IN, false);
        } catch (NullPointerException e) {
            LoggerUtil.e(SharedPrefUtils.class.getSimpleName(), "error");
            return false;
        }
    }

    /**
     * Method to set is user logged in or not
     *
     * @param context    application context
     * @param isLoggedIn state of user
     */
    public static void setUserLoggedIn(Context context, boolean isLoggedIn) {
        LoggerUtil.v(SharedPrefUtils.class.getSimpleName(), "setUserLoggedIn");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PENDULUM_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(IS_USER_LOGGED_IN, isLoggedIn);
            editor.apply();
        } catch (NullPointerException e) {
            LoggerUtil.e(SharedPrefUtils.class.getSimpleName(), "error");
        }
    }

    /**
     * Method is used to get userId from Local Mobile Storage
     *
     * @param context application context
     * @return userId
     */
    public static String getUserId(Context context) {
        LoggerUtil.v(SharedPrefUtils.class.getSimpleName(), "getUserId");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PENDULUM_SHARED_PREF, Context.MODE_PRIVATE);
            return sharedPref.getString(USER_ID, null);
        } catch (NullPointerException e) {
            LoggerUtil.e(SharedPrefUtils.class.getSimpleName(), "error");
            return null;
        }
    }

    /**
     * Method is used to store userId in Local Mobile Storage
     *
     * @param context application context
     * @param userId  userId
     */
    public static void setUserId(Context context, String userId) {
        LoggerUtil.v(SharedPrefUtils.class.getSimpleName(), "setUserId");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PENDULUM_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(USER_ID, userId);
            editor.apply();
        } catch (NullPointerException e) {
            LoggerUtil.e(SharedPrefUtils.class.getSimpleName(), "error");
        }
    }

}
