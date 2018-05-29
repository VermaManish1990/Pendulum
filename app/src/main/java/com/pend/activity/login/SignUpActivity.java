package com.pend.activity.login;

import android.os.Bundle;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.util.NetworkUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.volley.ext.RequestManager;

public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {
        if (!NetworkUtil.isInternetConnected(this)) {
//            showSnake(getString(R.string.network_connection));
            return;
        }
//        showProgressDialog(getResources().getString(R.string.pleaseWait), false);

//        RequestManager.addRequest(new GsonObjectRequest<LoginResponse>(url, NetworkUtil.getHeaders(this), null, LoginResponse.class, new
//                VolleyErrorListener(this, actionID)) {
//
//            @Override
//            protected void deliverResponse(LoginResponse response) {
//
//            }
//        });

    }

    @Override
    public void onAuthError() {

    }
}
