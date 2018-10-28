package com.pend.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class SharedPrefUtils {

    private static final String PENDULUM_SHARED_PREF = "PENDULUM_SHARED_PREF";
    private static final String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
    private static final String USER_ID = "USER_ID";
    private static final String IMAGE_URL = "IMAGE_URL";
    private static final String IS_DATA_BASE_COPIED = "IS_DATA_BASE_COPIED";
    private static final String LAT = "LAT";
    private static final String LNG = "LNG";

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
            return sharedPref.getString(USER_ID, "-1");
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

    /**
     * Method is used to get profile image url from Local Mobile Storage
     *
     * @param context application context
     * @return image url
     */
    public static String getProfileImageUrl(Context context) {
        LoggerUtil.v(SharedPrefUtils.class.getSimpleName(), "getProfileImageUrl");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PENDULUM_SHARED_PREF, Context.MODE_PRIVATE);
            return sharedPref.getString(IMAGE_URL, null);
        } catch (NullPointerException e) {
            LoggerUtil.e(SharedPrefUtils.class.getSimpleName(), "error");
            return null;
        }
    }

    /**
     * Method is used to store profile image url in Local Mobile Storage
     *
     * @param context application context
     * @param imageUrl  imageUrl
     */
    public static void setProfileImageUrl(Context context, String imageUrl) {
        LoggerUtil.v(SharedPrefUtils.class.getSimpleName(), "setProfileImageUrl");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PENDULUM_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(IMAGE_URL, imageUrl);
            editor.apply();
        } catch (NullPointerException e) {
            LoggerUtil.e(SharedPrefUtils.class.getSimpleName(), "error");
        }
    }


    /**
     * Method is used to store user location in Local Mobile Storage
     *
     * @param context application context
     * @param loc  location
     *
     */
    public static void setLocation(Context context, LatLng loc) {
        LoggerUtil.v(SharedPrefUtils.class.getSimpleName(), "setLocation");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PENDULUM_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(LAT, String.valueOf(loc.latitude));
            editor.putString(LNG, String.valueOf(loc.longitude));
            editor.apply();
        } catch (NullPointerException e) {
            LoggerUtil.e(SharedPrefUtils.class.getSimpleName(), "error");
        }
    }

    /**
     * Method is used to get location from Local Mobile Storage
     *
     * @param context application context
     * @return Location loc
     */
    public static LatLng getLocation(Context context) {
        LoggerUtil.v(SharedPrefUtils.class.getSimpleName(), "getLocation");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PENDULUM_SHARED_PREF, Context.MODE_PRIVATE);
            return new LatLng(Double.parseDouble(sharedPref.getString(LAT, "0.0")),
                    Double.parseDouble(sharedPref.getString(LNG, "0.0")));
        } catch (NullPointerException e) {
            LoggerUtil.e(SharedPrefUtils.class.getSimpleName(), "error");
            return null;
        }
    }
}
