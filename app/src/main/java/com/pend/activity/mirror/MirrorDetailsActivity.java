package com.pend.activity.mirror;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetMirrorDetailsResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class MirrorDetailsActivity extends BaseActivity {

    private static final String TAG = MirrorDetailsActivity.class.getSimpleName();
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror_details);

        initUI();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE:
                if (status) {
                    GetMirrorDetailsResponseModel mirrorDetailsResponseModel = (GetMirrorDetailsResponseModel) serviceResponse;
                    if (mirrorDetailsResponseModel != null && mirrorDetailsResponseModel.status) {
                        LoggerUtil.d(TAG, mirrorDetailsResponseModel.statusCode);

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
        removeProgressDialog();
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return;
        }
        showProgressDialog();

        switch (actionID) {
            case IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE:

                String mirrorDetailsUrl = IWebServices.REQUEST_GET_MIRROR_DETAILS_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
                        + "&" + Constants.PARAM_MIRROR_ID + "=" + String.valueOf("66");
                RequestManager.addRequest(new GsonObjectRequest<GetMirrorDetailsResponseModel>(mirrorDetailsUrl, NetworkUtil.getHeaders(this),
                        null, GetMirrorDetailsResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetMirrorDetailsResponseModel response) {
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
