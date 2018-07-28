package com.pend.activity.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.UpdateUserSettingResponseModel;
import com.pend.models.UserProfileResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
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

    private String mPhoneNumber;
    private String mPassword;
    private String mUserEmail;
    private boolean mIsChecked;
    private boolean mIsAboutOpen;
    private boolean mIsMirrorOpen;
    private boolean mIsAccountOpen;
    private boolean mIsArenaOpen;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;
    private ImageView mIvProfile;
    private TextView mTvHome;


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

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);
        mTvHome = quarterView.findViewById(R.id.tv_home);
        mIvProfile = quarterView.findViewById(R.id.iv_profile);

        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
        mTvHome.setOnClickListener(this);

        findViewById(R.id.tv_logout).setOnClickListener(this);
        findViewById(R.id.bt_save).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        mIvAboutDropdown.setOnClickListener(this);
        mIvArenaDropdown.setOnClickListener(this);
        mIvAccountDropdown.setOnClickListener(this);
        mIvMirrorDropdown.setOnClickListener(this);

        findViewById(R.id.about_layout).setOnClickListener(this);
        findViewById(R.id.arena_layout).setOnClickListener(this);
        findViewById(R.id.mirror_layout).setOnClickListener(this);
        findViewById(R.id.account_layout).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        mIsChecked = true;
        mIsAboutOpen = false;
        mIsAccountOpen = false;
        mIsArenaOpen = false;
        mIsMirrorOpen = false;

        mIvProfile.setVisibility(View.GONE);
        mTvHome.setVisibility(View.VISIBLE);

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
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_UPDATE_USER_SEETING_CODE:
                if (status) {
                    UpdateUserSettingResponseModel updateUserSettingResponseModel = (UpdateUserSettingResponseModel) serviceResponse;
                    if (updateUserSettingResponseModel != null && updateUserSettingResponseModel.status) {
                        LoggerUtil.d(TAG, updateUserSettingResponseModel.statusCode);

                        mEtEmail.setText(updateUserSettingResponseModel.Data.userData.userEmail != null ? updateUserSettingResponseModel.Data.userData.userEmail : "");
                        mEtPhoneNumber.setText(updateUserSettingResponseModel.Data.userData.userPhone != null ? updateUserSettingResponseModel.Data.userData.userPhone : "");
                        mEtPassword.setText(updateUserSettingResponseModel.Data.userData.userPassword != null ? updateUserSettingResponseModel.Data.userData.userPassword : "");

                        mCbOpenSearch.setChecked(updateUserSettingResponseModel.Data.userData.isShowMeInOpenSearch);
                        mCbInvisibilityInReflection.setChecked(updateUserSettingResponseModel.Data.userData.isVisibleInReflection);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_logout:

                OtherUtil.showAlertDialog(getString(R.string.logout_message), this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getData(IApiEvent.REQUEST_USER_LOGOUT_CODE);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                break;

            case R.id.about_layout:
            case R.id.iv_about_dropdown:
                if (mIsAboutOpen) {
                    mIsAboutOpen = false;
//                    mIvAboutDropdown.setImageDrawable(getResources().getDrawable(R.drawable.right));
                    mTvAbout.setVisibility(View.GONE);
                    mIvAboutDropdown.setRotation(0f);
                } else {
                    mIsAboutOpen = true;
//                    mIvAboutDropdown.setImageDrawable(getResources().getDrawable(R.drawable.down));
                    mTvAbout.setVisibility(View.VISIBLE);
                    mIvAboutDropdown.setRotation(90f);
                }
                break;

            case R.id.arena_layout:
            case R.id.iv_arena_dropdown:
                if (mIsArenaOpen) {
                    mIsArenaOpen = false;
                    // mIvArenaDropdown.setImageDrawable(getResources().getDrawable(R.drawable.right));
                    mIvArenaDropdown.setRotation(0f);
                    mRlArenaView.setVisibility(View.GONE);
                } else {
                    mIsArenaOpen = true;
//                    mIvArenaDropdown.setImageDrawable(getResources().getDrawable(R.drawable.down));
                    mIvArenaDropdown.setRotation(90f);
                    mRlArenaView.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.mirror_layout:
            case R.id.iv_mirror_dropdown:
                if (mIsMirrorOpen) {
                    mIsMirrorOpen = false;
//                    mIvMirrorDropdown.setImageDrawable(getResources().getDrawable(R.drawable.right));
                    mRlMirrorView.setVisibility(View.GONE);
                    mIvMirrorDropdown.setRotation(0f);
                } else {
                    mIsMirrorOpen = true;
//                    mIvMirrorDropdown.setImageDrawable(getResources().getDrawable(R.drawable.down));
                    mIvMirrorDropdown.setRotation(90f);
                    mRlMirrorView.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.account_layout:
            case R.id.iv_account_dropdown:
                if (mIsAccountOpen) {
                    mIsAccountOpen = false;
//                    mIvAccountDropdown.setImageDrawable(getResources().getDrawable(R.drawable.right));
                    mIvAccountDropdown.setRotation(0f);
                    mRlAccountView.setVisibility(View.GONE);
                } else {
                    mIsAccountOpen = true;
//                    mIvAccountDropdown.setImageDrawable(getResources().getDrawable(R.drawable.down));
                    mRlAccountView.setVisibility(View.VISIBLE);
                    mIvAccountDropdown.setRotation(90f);
                }
                break;

            case R.id.bt_save:
                if (isAllFieldsValid()) {
                    getData(IApiEvent.REQUEST_UPDATE_USER_SEETING_CODE);
                } else {
                    Snackbar.make(mRootView, getString(R.string.Please_fill_all_fields), Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_profile:
                hideReveal();
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentMirror = new Intent(this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentContest = new Intent(this, ContestActivity.class);
                startActivity(intentContest);
                break;

            case R.id.fl_area:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.tv_home:
                hideReveal();
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                finish();
                break;

            case R.id.fl_menu_view:
                mFlMenuView.setVisibility(View.GONE);
                showReveal();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void showReveal() {
        int cx = 0;
        int cy = mRlQuarterView.getHeight();
        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mRlQuarterView, cx, cy, 0, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRlQuarterView.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void hideReveal() {

        int cx = 0;
        int cy = mRlQuarterView.getHeight();
        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mRlQuarterView, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRlQuarterView.setVisibility(View.GONE);
                mFlMenuView.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (mRlQuarterView.getVisibility() == View.VISIBLE) {
            hideReveal();
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Rect outRect = new Rect();
        mFlQuarterBlackView.getGlobalVisibleRect(outRect);

        if (mRlQuarterView.getVisibility() == View.VISIBLE && !outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
            hideReveal();
        }
        return super.dispatchTouchEvent(event);
    }
}
