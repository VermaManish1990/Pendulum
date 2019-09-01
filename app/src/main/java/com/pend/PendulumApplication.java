package com.pend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.pend.interfaces.Constants;
import com.pendulum.application.BaseApplication;
import com.pendulum.volley.ext.RequestManager;

import static com.pend.util.NetworkUtil.getMd5Encryption;

public class PendulumApplication extends BaseApplication {

    public static String deviceId = "";
    public static String securityKey = "";

    @Override
    public void onCreate() {
        super.onCreate();

        deviceId = getDeviceId();
        securityKey = getSecurityKey();
    }

    @Override
    protected void initialize() {
        RequestManager.initializeWith(this.getApplicationContext(), new RequestManager.Config(getString(R.string.data_data_DSD_pics), 5242880, 4));
    }

    public final String getSecurityKey() {
        String securityKey = getDeviceId() + Constants.SECURITY_CONSTANT;

        String eString  =getMd5Encryption(securityKey);
        Log.e("securityKey",eString);
        return  eString;
    }

    public String getDeviceId() {
        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("deviceId",deviceId);
        return deviceId;
    }
}
