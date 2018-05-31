package com.pend.activity.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.ProfileViewPagerAdapter;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.UserProfileResponseModel;
import com.pend.models.UserTimeSheetResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ViewPager mViewpagerProfile;
    private TabLayout mTabLayout;
    private View mRootView;

    private UserProfileResponseModel mUserProfileResponseModel;
    private ArrayList<UserProfileResponseModel.ImageDetails> mImageDetailsList;
    private ProfileViewPagerAdapter mProfileViewPagerAdapter;
    private TextView mTvName;
    private TextView mTvAge;
    private TextView mTvCity;
    private TextView mTvToken;
    private ImageView mIvSetting;
    private ImageView mIvEdit;
    private RecyclerView mRecyclerViewTimeSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mViewpagerProfile = findViewById(R.id.viewpager_profile);
        mTabLayout = findViewById(R.id.tab_layout);
        mTvName = findViewById(R.id.tv_name);
        mTvAge = findViewById(R.id.tv_age);
        mTvCity = findViewById(R.id.tv_city);
        mTvToken = findViewById(R.id.tv_token);
        mIvSetting = findViewById(R.id.iv_setting);
        mIvEdit = findViewById(R.id.iv_edit);
        mRecyclerViewTimeSheet = findViewById(R.id.recycler_view_time_sheet);
    }

    @Override
    protected void setInitialData() {

        mImageDetailsList = new ArrayList<>();

        mTabLayout.setupWithViewPager(mViewpagerProfile, true);
        mProfileViewPagerAdapter = new ProfileViewPagerAdapter(this, mImageDetailsList);
        mViewpagerProfile.setAdapter(mProfileViewPagerAdapter);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_USER_PROFILE_CODE:
                if (status) {
                    UserProfileResponseModel userProfileResponseModel = (UserProfileResponseModel) serviceResponse;
                    if (userProfileResponseModel != null && userProfileResponseModel.status) {
                        LoggerUtil.d(TAG, userProfileResponseModel.statusCode);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }


            case IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE:
                if (status) {
                    UserTimeSheetResponseModel userTimeSheetResponseModel = (UserTimeSheetResponseModel) serviceResponse;
                    if (userTimeSheetResponseModel != null && userTimeSheetResponseModel.status) {
                        LoggerUtil.d(TAG, userTimeSheetResponseModel.statusCode);

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
            case IApiEvent.REQUEST_GET_USER_PROFILE_CODE:

                RequestManager.addRequest(new GsonObjectRequest<UserProfileResponseModel>(IWebServices.REQUEST_GET_USER_PROFILE_URL,
                        NetworkUtil.getHeadersWithUserId(this), null, UserProfileResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(UserProfileResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE:

                RequestManager.addRequest(new GsonObjectRequest<UserTimeSheetResponseModel>(IWebServices.REQUEST_GET_USER_TIME_SHEET_URL,
                        NetworkUtil.getHeadersWithUserIdAndPageNumber(this, "1"),
                        null, UserTimeSheetResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(UserTimeSheetResponseModel response) {
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
