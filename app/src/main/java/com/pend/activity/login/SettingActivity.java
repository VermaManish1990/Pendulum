package com.pend.activity.login;

import android.os.Bundle;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.UpdateUserSettingResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class SettingActivity extends BaseActivity {

    private final String TAG = SettingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUI();
    }

    @Override
    protected void initUI() {

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_USER_LOGOUT_CODE:
                if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }


            case IApiEvent.REQUEST_UPDATE_USER_SEETING_CODE:
                if (status) {
                    UpdateUserSettingResponseModel updateUserSettingResponseModel = (UpdateUserSettingResponseModel) serviceResponse;
                    if (updateUserSettingResponseModel != null && updateUserSettingResponseModel.status) {
                        LoggerUtil.d(TAG, updateUserSettingResponseModel.statusCode);

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

        JsonObject requestObject;
        String request;

        switch (actionID) {
            case IApiEvent.REQUEST_USER_LOGOUT_CODE:

                requestObject = RequestPostDataUtil.logoutOrOnlineUserApiRegParam(12345);
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<BaseResponseModel>(IWebServices.REQUEST_USER_LOGOUT_URL, NetworkUtil.getHeaders(this),
                        request, BaseResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(BaseResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_UPDATE_USER_SEETING_CODE:

                requestObject = RequestPostDataUtil.updateUserSettingApiRegParam(12345, "", "", "",
                        true, true);
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<UpdateUserSettingResponseModel>(IWebServices.REQUEST_UPDATE_USER_SEETING_URL, NetworkUtil.getHeaders(this),
                        request, UpdateUserSettingResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(UpdateUserSettingResponseModel response) {
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
