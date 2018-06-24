package com.pend.activity.mirror;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.ReflectionMirrorAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetReflectionUsersResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class ReflectionsActivity extends BaseActivity {

    private static final String TAG = ReflectionsActivity.class.getSimpleName();
    private ArrayList<GetReflectionUsersResponseModel.GetReflectionUsersDetails> mUserDataList;
    private View mRootView;
    private GridView mGridViewReflection;
    private int mPageNumber;
    private int mMirrorId;
    private TextView mTvDataNotAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflections);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.MIRROR_ID_KEY)) {
                mMirrorId = localBundle.getInt(Constants.MIRROR_ID_KEY, 0);
            }
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_REFLECTION_USERS_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mTvDataNotAvailable = findViewById(R.id.tv_data_not_available);
        mGridViewReflection = findViewById(R.id.grid_view_reflection);
    }

    @Override
    protected void setInitialData() {

        mPageNumber = 1;
        mUserDataList = new ArrayList<>();

        mGridViewReflection.setAdapter(new ReflectionMirrorAdapter(this, mUserDataList));

        /*
          On Click event for Single Gridview Item
          */
        mGridViewReflection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_REFLECTION_USERS_CODE:
                if (status) {
                    GetReflectionUsersResponseModel reflectionUsersResponseModel = (GetReflectionUsersResponseModel) serviceResponse;
                    if (reflectionUsersResponseModel != null && reflectionUsersResponseModel.status) {
                        LoggerUtil.d(TAG, reflectionUsersResponseModel.statusCode);

                        if (reflectionUsersResponseModel.Data != null && reflectionUsersResponseModel.Data.usersData != null) {

                            mTvDataNotAvailable.setVisibility(View.GONE);
                            mGridViewReflection.setVisibility(View.VISIBLE);

                            ReflectionMirrorAdapter reflectionMirrorAdapter = (ReflectionMirrorAdapter) mGridViewReflection.getAdapter();
                            mUserDataList.addAll(reflectionUsersResponseModel.Data.usersData);
                            reflectionMirrorAdapter.setUserDataList(mUserDataList);
                            reflectionMirrorAdapter.notifyDataSetChanged();
                        } else {
                            mTvDataNotAvailable.setVisibility(View.VISIBLE);
                            mGridViewReflection.setVisibility(View.GONE);
                        }

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
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }
        showProgressDialog();

        switch (actionID) {
            case IApiEvent.REQUEST_GET_REFLECTION_USERS_CODE:

                String reflectionUserUrl = IWebServices.REQUEST_GET_REFLECTION_USERS_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this) + "&" +
                        Constants.PARAM_MIRROR_ID + "=" + mMirrorId + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                RequestManager.addRequest(new GsonObjectRequest<GetReflectionUsersResponseModel>(reflectionUserUrl, NetworkUtil.getHeaders(this),
                        null, GetReflectionUsersResponseModel.class, new VolleyErrorListener(this, actionID)) {

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
