package com.pend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.UpdateUserSettingResponseModel;
import com.pend.models.UserProfileResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = SettingActivity.class.getSimpleName();
    private View mRootView;
    private View mRlAccountView;
    private View mRlArenaView;
    private View mRlMirrorView;

    private TextView mTvLogout;
    private TextView mTvAbout;

    private ImageView mIvAboutDropdown;
    private ImageView mIvArenaDropdown;
    private ImageView mIvAccountDropdown;
    private ImageView mIvMirrorDropdown;

    private EditText mEtPassword;
    private EditText mEtPhoneNumber;
    private EditText mEtEmail;

    private CheckBox mCbOpenSearch;
    private CheckBox mCbInvisibilityInReflection;
    private boolean mIsChecked = true;
    private String mPhoneNumber;
    private String mPassword;
    private String mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRlAccountView = findViewById(R.id.rl_account_view);
        mRlArenaView = findViewById(R.id.rl_arena_view);
        mRlMirrorView = findViewById(R.id.rl_mirror_view);

        mIvAboutDropdown = findViewById(R.id.iv_about_dropdown);
        mIvArenaDropdown = findViewById(R.id.iv_arena_dropdown);
        mIvAccountDropdown = findViewById(R.id.iv_account_dropdown);
        mIvMirrorDropdown = findViewById(R.id.iv_mirror_dropdown);

        mEtPassword = findViewById(R.id.et_password);
        mEtPhoneNumber = findViewById(R.id.et_phone_number);
        mEtEmail = findViewById(R.id.et_email);

        mCbOpenSearch = findViewById(R.id.cb_open_search);
        mCbInvisibilityInReflection = findViewById(R.id.cb_invisibility_in_reflection);

        mTvAbout = findViewById(R.id.tv_about);
        findViewById(R.id.tv_logout).setOnClickListener(this);
        findViewById(R.id.bt_save).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        mIvAboutDropdown.setOnClickListener(this);
        mIvArenaDropdown.setOnClickListener(this);
        mIvAccountDropdown.setOnClickListener(this);
        mIvMirrorDropdown.setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.USER_DETAILS_KEY)) {
                UserProfileResponseModel.UserProfileDetails userProfileDetails = (UserProfileResponseModel.UserProfileDetails) localBundle.getSerializable(
                        Constants.USER_DETAILS_KEY);

                if (userProfileDetails != null) {
                    mEtEmail.setText(userProfileDetails.userEmail != null ? userProfileDetails.userEmail : "");
                    mEtPhoneNumber.setText(userProfileDetails.userPhone != null ? userProfileDetails.userPhone : "");

                    mCbOpenSearch.setChecked(userProfileDetails.isShowMeInOpenSearch);
                    mCbInvisibilityInReflection.setChecked(userProfileDetails.isVisibleInReflection);
                }
            }
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_USER_LOGOUT_CODE:
                if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                        SharedPrefUtils.setUserLoggedIn(SettingActivity.this, false);
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);

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

                       /* mEtEmail.setText(updateUserSettingResponseModel.Data.userData.userEmail != null ? updateUserSettingResponseModel.Data.userData.userEmail : "");
                        mEtPhoneNumber.setText(updateUserSettingResponseModel.Data.userData.userPhone != null ? updateUserSettingResponseModel.Data.userData.userPhone : "");
                        mEtPassword.setText(updateUserSettingResponseModel.Data.userData.userPassword != null ? updateUserSettingResponseModel.Data.userData.userPassword : "");

                        mCbOpenSearch.setChecked(updateUserSettingResponseModel.Data.userData.isShowMeInOpenSearch);
                        mCbInvisibilityInReflection.setChecked(updateUserSettingResponseModel.Data.userData.isVisibleInReflection);*/

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
            Snackbar.make(mRootView, getString(R.string.network_connection), Snackbar.LENGTH_LONG);
            return;
        }
        showProgressDialog();

        JsonObject requestObject;
        String request;

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(SettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_USER_LOGOUT_CODE:

                requestObject = RequestPostDataUtil.logoutOrOnlineUserApiRegParam(userId);
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

                requestObject = RequestPostDataUtil.updateUserSettingApiRegParam(userId, mUserEmail, mPhoneNumber, mPassword,
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_logout:
                getData(IApiEvent.REQUEST_USER_LOGOUT_CODE);
                break;

            case R.id.iv_about_dropdown:
                break;

            case R.id.iv_arena_dropdown:
                break;

            case R.id.iv_mirror_dropdown:
                break;

            case R.id.iv_account_dropdown:
                break;

            case R.id.bt_save:
                break;

            case R.id.iv_back:
                if (isAllFieldsValid()) {
                    getData(IApiEvent.REQUEST_UPDATE_USER_SEETING_CODE);
                } else {
                    Snackbar.make(mRootView, R.string.Please_fill_all_fields, Snackbar.LENGTH_LONG);
                }
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    /**
     * Method is used to check validation for all fields.
     *
     * @return true if all validation is correct , otherwise false.
     */
    private boolean isAllFieldsValid() {

        mIsChecked = true;
        checkValidationForMobileNumber();
        checkValidationForEmail();
        checkValidationForPassword();
        return mIsChecked;
    }

    /**
     * This method will check validation for Mobile Number.
     */
    private void checkValidationForMobileNumber() {
        if (mEtPhoneNumber.getText().toString().trim().length() == 0) {
            mIsChecked = false;
        } else if (mEtPhoneNumber.getText().toString().trim().length() < 10) {
            mIsChecked = false;
        } else {
            mPhoneNumber = mEtPhoneNumber.getText().toString().trim();
        }
    }

    /**
     * This method will check validation for Email Id.
     */
    private void checkValidationForEmail() {
        checkValidationForMobileNumber();
        if (mEtEmail.getText().toString().trim().length() == 0) {
            mIsChecked = false;
        } else {
            mUserEmail = mEtEmail.getText().toString().trim();
        }
    }

    /**
     * This method will check validation for Name.
     */
    private void checkValidationForPassword() {
        if (mEtPassword.getText().toString().trim().length() == 0) {
            mIsChecked = false;
        } else {
            mPassword = mEtPassword.getText().toString().trim();
        }
    }

}
