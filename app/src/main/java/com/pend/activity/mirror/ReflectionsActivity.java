package com.pend.activity.mirror;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.ReflectionMirrorAdapter;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetReflectionUsersResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class ReflectionsActivity extends BaseActivity {

    private static final String TAG = ReflectionsActivity.class.getSimpleName();
    private View mRootView;
    private GridView mGridViewReflection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflections);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mGridViewReflection = findViewById(R.id.grid_view_reflection);
    }

    @Override
    protected void setInitialData() {

        ArrayList<GetReflectionUsersResponseModel.GetReflectionUsersDetails> mirrorList = new ArrayList<>();
        mGridViewReflection.setAdapter(new ReflectionMirrorAdapter(this, mirrorList));

        /*
          On Click event for Single Gridview Item
          */
        mGridViewReflection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        });
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_REFLECTION_USERS_CODE:
                if (status) {
                    GetReflectionUsersResponseModel getReflectionUsersResponseModel = (GetReflectionUsersResponseModel) serviceResponse;
                    if (getReflectionUsersResponseModel != null && getReflectionUsersResponseModel.status) {
                        LoggerUtil.d(TAG, getReflectionUsersResponseModel.statusCode);

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
            case IApiEvent.REQUEST_GET_REFLECTION_USERS_CODE:

                RequestManager.addRequest(new GsonObjectRequest<GetReflectionUsersResponseModel>(IWebServices.REQUEST_GET_REFLECTION_USERS_URL,
                        NetworkUtil.getHeaders(this), null, GetReflectionUsersResponseModel.class,
                        new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetReflectionUsersResponseModel response) {
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
