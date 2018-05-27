package com.pendulum.gcm;/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 *//*


package com.com.taxi.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.com.taxi.application.BaseApplication;
import com.com.taxi.utils.StringUtils;

import java.io.IOException;

public abstract class GCMRegister {

    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String LOG_TAG = "GCMRegister";
    private static final int MAX_ATTEMPTS = 10;

    private GoogleCloudMessaging mGoogleCloudMessaging;

    private Context mContext;
    private GCMConfig mGcmConfig;
    private String regid;


    private static class GCMConfig {
        final String SENDER_ID;

        private GCMConfig(Context appContext) throws PackageManager.NameNotFoundException {

            ApplicationInfo ai = ((BaseApplication) appContext).getApplicationMetaData();
            Bundle bundle = ai.metaData;
            if (bundle == null) {
                throw new NullPointerException("Failed to load meta-data");
            }
            SENDER_ID = bundle.getString("GCM_SENDER_ID");
        }
    }


    public GCMRegister(Context context) {
        mContext = context;
        try {
            mGcmConfig = new GCMConfig(mContext);
        } catch (PackageManager.NameNotFoundException e) {
            return;
        } catch (NullPointerException e) {
            return;
        }
        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(mContext);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        }
    }


    */
/**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     *//*

    protected boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                if (mContext instanceof Activity) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) mContext,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                }

            } else {
                if (mContext instanceof Activity) {
                    //TODO: show dialog to user.
                }
            }
            return false;
        }
        return true;
    }

    */
/**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     *//*

    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(mContext);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    */
/**
     * @return Application's {@code SharedPreferences}.
     *//*

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return context.getSharedPreferences(GCMRegister.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    */
/**
     * @return Application's version code from the {@code PackageManager}.
     *//*

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private void registerInBackground() {
        new Thread() {
            public void run() {
                if (mGoogleCloudMessaging == null) {
                    mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(mContext);
                }
                long backoff = 5000;
                for (int i = 1; i <= MAX_ATTEMPTS; i++) {
                    try {


                        regid = mGoogleCloudMessaging.register(mGcmConfig.SENDER_ID);
                        if (StringUtils.isNullOrEmpty(regid)) {
                            if (i == MAX_ATTEMPTS) {
                                break;
                            }

                            try {
                                Thread.sleep(backoff);
                            } catch (InterruptedException e) {

                            }
                            // increase backoff exponentially
                            backoff *= 2;
                        }
                        // You should send the registration ID to your server over HTTP,
                        // so it can use GCM/HTTP or CCS to send messages to your app.
                        // The request to your server should be authenticated if your app
                        // is using accounts.
                        sendRegistrationIdToBackend(regid);

                        // For this demo: we don't need to send it because the device
                        // will send upstream messages to a server that echo back the
                        // message using the 'from' address in the message.

                        // Persist the registration ID - no need to register again.
                        storeRegistrationId(mContext, regid);
                    } catch (IOException ex) {

                        // If there is an error, don't just keep trying to register.
                        // exponential back-off.
                        if (i == MAX_ATTEMPTS) {
                            break;
                        }

                        try {
                            Thread.sleep(backoff);
                        } catch (InterruptedException e) {

                        }
                        // increase backoff exponentially
                        backoff *= 2;
                    }
                }
            }
        }.start();
    }

    protected abstract void sendRegistrationIdToBackend(final String regid);

    */
/**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     *//*

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }
}*/
