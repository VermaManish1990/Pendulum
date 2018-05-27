package com.pend.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pend.BaseActivity;

import java.util.HashMap;

import pendulum.com.pend.R;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {
       /* if (!ConnectivityUtils.isNetworkEnabled(this)) {
            showSnake(getString(R.string.network_connection));
            return;
        }
        showProgressDialog(getResources().getString(R.string.pleaseWait), false);
        String deviceId = SharedPrefsUtils.getSharedPrefString(this, Constant.PREF_FILE_NAME, ApiConstant.PARAM_DEVICE_ID, "");
        String deviceToken = SharedPrefsUtils.getSharedPrefString(this, Constant.PREF_FILE_NAME, ApiConstant.PARAM_DEVICE_TOKEN, "");
        String url = ApiConstant.URL_LOGIN + ApiConstant.PARAM_DEVICE_ID + "=" + deviceId + "&" + ApiConstant.PARAM_DEVICE_TOKEN
                + "=" + deviceToken + "&" + ApiConstant.PARAM_DEVICE_TYPE + "=" + Constant.deviceType + "&" + ApiConstant.PARAM_EMAIL
                + "=" + mEmail.getText().toString() + "&" + ApiConstant.PARAM_PASSWORD + "=" + mPassword.getText().toString();
        RequestManager.addRequest(new GsonObjectRequest<LoginResponse>(url, new HashMap<String, String>(), null, LoginResponse.class, new
                VolleyErrorListener(this, actionID)) {

            @Override
            protected void deliverResponse(LoginResponse response) {
                if (response != null) {
                    if (response.getSuccess() == 1) {
                        updateUi(true, actionID, response);
                    } else {
                        updateUi(false, actionID, response.getMessage());
                    }
                } else {
                    removeProgressDialog();
                }
            }
        });
    }*/
    }

    @Override
    public void onAuthError() {

    }
}
