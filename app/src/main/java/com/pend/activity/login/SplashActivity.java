package com.pend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.home.HomeActivity;
import com.pend.interfaces.Constants;
import com.pend.util.NetworkUtil;
import com.pend.util.SharedPrefUtils;

public class SplashActivity extends BaseActivity {


    private View mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        NetworkUtil.getDeviceId(this);

        mRootView = findViewById(R.id.root_view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLogin = SharedPrefUtils.isUserLoggedIn(SplashActivity.this);
                if (isLogin) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }

        }, Constants.SPLASH_TIME_OUT);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }

    @Override
    public void onAuthError() {

    }
}
