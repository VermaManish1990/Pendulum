package com.pend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.ProfileViewPagerAdapter;
import com.pend.adapters.TimeSheetAdapter;
import com.pend.interfaces.Constants;
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

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ViewPager mViewpagerProfile;
    private TabLayout mTabLayout;
    private View mRootView;

    private UserProfileResponseModel mUserProfileResponseModel;
    private ArrayList<UserProfileResponseModel.ImageDetails> mImageDetailsList;
    private ArrayList<UserTimeSheetResponseModel.UserTimeSheetDetails> mTimeSheetDetailsList;
    private ProfileViewPagerAdapter mProfileViewPagerAdapter;
    private TextView mTvName;
    private TextView mTvAge;
    private TextView mTvCity;
    private TextView mTvToken;
    private RecyclerView mRecyclerViewTimeSheet;
    private TimeSheetAdapter mTimeSheetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_USER_PROFILE_CODE);
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
        mRecyclerViewTimeSheet = findViewById(R.id.recycler_view_time_sheet);

        findViewById(R.id.iv_setting).setOnClickListener(this);
        findViewById(R.id.iv_edit).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        mImageDetailsList = new ArrayList<>();

        mTabLayout.setupWithViewPager(mViewpagerProfile, true);
        mProfileViewPagerAdapter = new ProfileViewPagerAdapter(this, mImageDetailsList);
        mViewpagerProfile.setAdapter(mProfileViewPagerAdapter);

        mTimeSheetAdapter = new TimeSheetAdapter(this,mTimeSheetDetailsList);
        mRecyclerViewTimeSheet.setAdapter(mTimeSheetAdapter);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_USER_PROFILE_CODE:
                if (status) {
                    mUserProfileResponseModel = (UserProfileResponseModel) serviceResponse;
                    if (mUserProfileResponseModel != null && mUserProfileResponseModel.status) {
                        LoggerUtil.d(TAG, mUserProfileResponseModel.statusCode);

                        if (mUserProfileResponseModel.Data != null && mUserProfileResponseModel.Data.imageData != null) {
                            mImageDetailsList = mUserProfileResponseModel.Data.imageData;
                            mProfileViewPagerAdapter.setImageDetailsList(mImageDetailsList);
                            mProfileViewPagerAdapter.notifyDataSetChanged();
                        }

                        if (mUserProfileResponseModel.Data != null && mUserProfileResponseModel.Data.userData != null) {

                            mTvName.setText(mUserProfileResponseModel.Data.userData.userName != null ? mUserProfileResponseModel.Data.userData.userName : "");
                            mTvAge.setText(String.valueOf(mUserProfileResponseModel.Data.userData.userAge + " year " + mUserProfileResponseModel.Data.userData.userGender));
                            mTvCity.setText(mUserProfileResponseModel.Data.userData.cityName != null ? mUserProfileResponseModel.Data.userData.cityName : "");
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                getData(IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE);
                break;

            case IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE:
                if (status) {
                    UserTimeSheetResponseModel userTimeSheetResponseModel = (UserTimeSheetResponseModel) serviceResponse;
                    if (userTimeSheetResponseModel != null && userTimeSheetResponseModel.status) {
                        LoggerUtil.d(TAG, userTimeSheetResponseModel.statusCode);

                        //TODO Pagination

                        if(userTimeSheetResponseModel.Data!=null&&userTimeSheetResponseModel.Data.timeSheetData!=null){
                            mTimeSheetAdapter.setTimeSheetDetailsList(userTimeSheetResponseModel.Data.timeSheetData);
                            mTimeSheetAdapter.notifyDataSetChanged();
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
            Snackbar.make(mRootView, getString(R.string.network_connection), Snackbar.LENGTH_LONG).show();
            return;
        }
        showProgressDialog();

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

                //TODO Pagination
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:

                if (mUserProfileResponseModel != null && mUserProfileResponseModel.Data != null && mUserProfileResponseModel.Data.userData != null) {

                    Intent intentSetting = new Intent(ProfileActivity.this, SettingActivity.class);
                    intentSetting.putExtra(Constants.USER_DETAILS_KEY, mUserProfileResponseModel.Data.userData);
                    startActivity(intentSetting);
                } else {
                    Snackbar.make(mRootView, getString(R.string.user_details_not_available), Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_edit:

                if (mUserProfileResponseModel != null && mUserProfileResponseModel.Data != null) {
                    Intent intentEdit = new Intent(ProfileActivity.this, EditMyProfileActivity.class);
                    intentEdit.putExtra(Constants.USER_DATA_MODEL_KEY, mUserProfileResponseModel.Data);
                    startActivity(intentEdit);
                } else {
                    Snackbar.make(mRootView, getString(R.string.user_profile_details_not_available), Snackbar.LENGTH_LONG).show();
                }
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
