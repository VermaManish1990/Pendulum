package com.pend;

import com.pendulum.application.BaseApplication;
import com.pendulum.volley.ext.RequestManager;

public class PendulumApplication extends BaseApplication {
    @Override
    protected void initialize() {
        RequestManager.initializeWith(this.getApplicationContext(), new RequestManager.Config(getString(R.string.data_data_DSD_pics), 5242880, 4));
    }
}
