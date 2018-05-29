package com.pend.activity.login;

import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.LoginResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class LoginActivity extends BaseActivity {
    private final String TAG = LoginActivity.class.getSimpleName();
    private String mUserName = "12345";
    private String mPassword = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getData(IApiEvent.REQUEST_LOGIN_CODE);
    }

    @Override
    public void updateUi(final boolean status, final int actionID, final Object serviceResponse) {

        switch (actionID) {
            case IApiEvent.REQUEST_LOGIN_CODE:
                if (status) {
                    LoginResponseModel loginResponseModel = (LoginResponseModel) serviceResponse;
                    if (loginResponseModel != null && loginResponseModel.status == 1) {
                        LoggerUtil.d(TAG, loginResponseModel.statusCode);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
//            showSnake(getString(R.string.network_connection));
            return;
        }
//        showProgressDialog(getResources().getString(R.string.pleaseWait), false);

        switch (actionID) {
            case IApiEvent.REQUEST_LOGIN_CODE:

                JsonObject requestObject = RequestPostDataUtil.loginApiRegParam(mUserName,mPassword);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<LoginResponseModel>(IWebServices.REQUEST_LOGIN_URL, NetworkUtil.getHeaders(this),
                        request, LoginResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(LoginResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }

    }

    @Override
    public void onAuthError() {

    }
}
