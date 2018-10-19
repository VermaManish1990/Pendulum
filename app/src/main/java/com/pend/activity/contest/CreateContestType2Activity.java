package com.pend.activity.contest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.adapters.SearchInNewsFeedAdapter;
import com.pend.arena.view.ArenaActivity;
import com.pend.fragments.ContestSearchDialogFragment;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.CreateContestResponseModel;
import com.pend.models.SearchInNewsFeedResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ToastUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class CreateContestType2Activity extends BaseActivity implements View.OnClickListener, SearchInNewsFeedAdapter.IMirrorSearchAdapterCallBack, TextWatcher {

    private static final String TAG = CreateContestType2Activity.class.getSimpleName();
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;
    private ImageView mIvProfile;
    private View mRootView;
    private EditText mEtQuestion;
    private EditText mEtFirst;
    private EditText mEtSecond;
    private EditText mEtThird;
    private int mMirrorId1;
    private int mMirrorId2;
    private int mMirrorId3;
    private ContestSearchDialogFragment mContestSearchDialogFragment;
    private int mMirrorNumber;
    private String mMirrorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contest_type2);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mEtQuestion = findViewById(R.id.et_question);
        mEtFirst = findViewById(R.id.et_first);
        mEtSecond = findViewById(R.id.et_second);
        mEtThird = findViewById(R.id.et_third);

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

        ((ImageView) quarterView.findViewById(R.id.iv_contest)).setImageDrawable(getResources().getDrawable(R.drawable.home));
        ((TextView) quarterView.findViewById(R.id.tv_contest)).setText(String.valueOf(getResources().getString(R.string.home)));
        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        mIvProfile = quarterView.findViewById(R.id.iv_profile);
        quarterView.findViewById(R.id.fl_arena).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
        mIvProfile.setOnClickListener(this);

        findViewById(R.id.iv_search1).setOnClickListener(this);
        findViewById(R.id.iv_search2).setOnClickListener(this);
        findViewById(R.id.iv_search3).setOnClickListener(this);
        findViewById(R.id.bt_create_contest).setOnClickListener(this);

        mEtFirst.addTextChangedListener(this);
        mEtSecond.addTextChangedListener(this);
        mEtThird.addTextChangedListener(this);
    }

    @Override
    protected void setInitialData() {

        mMirrorId1 = -1;
        mMirrorId2 = -1;
        mMirrorId3 = -1;
        mMirrorName = "";
        String imageUrl = SharedPrefUtils.getProfileImageUrl(this);

        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.with(this)
                    .load(imageUrl)
                    .into(mIvProfile);
        }

        mEtFirst.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if (mEtFirst.getText().toString().trim().length() > 0)
                    onSearchClick(1, mEtFirst.getText().toString());

                return true;
            }
            return false;
        });

        mEtSecond.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if (mEtSecond.getText().toString().trim().length() > 0)
                    onSearchClick(2, mEtSecond.getText().toString());

                return true;
            }
            return false;
        });

        mEtThird.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                if (mEtThird.getText().toString().trim().length() > 0)
                    onSearchClick(3, mEtThird.getText().toString());

                return true;
            }
            return false;
        });
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_CREATE_CONTEST_CODE:
                if (status) {
                    CreateContestResponseModel responseModel = (CreateContestResponseModel) serviceResponse;
                    if (responseModel != null && responseModel.status) {
                        LoggerUtil.d(TAG, responseModel.statusCode);

                        //TODO UPDATE DATA
                        ToastUtils.showToast(CreateContestType2Activity.this, "Update Successfully");
                        finish();
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
    public void getData(int actionID) {
        if (!NetworkUtil.isInternetConnected(this)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }
        showProgressDialog();

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(CreateContestType2Activity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject jsonObject;
        String request;

        switch (actionID) {
            case IApiEvent.REQUEST_CREATE_CONTEST_CODE:

                if (mMirrorId1 != -1 && mMirrorId2 != -1) {

                    jsonObject = RequestPostDataUtil.createContestReqParam(userId, 2, 0, mEtQuestion.getText().toString(),
                            mMirrorId1, mMirrorId2, mMirrorId3, mEtFirst.getText().toString(), mEtSecond.getText().toString(), mEtThird.getText().toString(),
                            "");
                    request = jsonObject.toString();

                    if (mEtThird.getText().toString().trim().length() == 0) {

                        RequestManager.addRequest(new GsonObjectRequest<CreateContestResponseModel>(IWebServices.REQUEST_CREATE_CONTEST_URL, NetworkUtil.getHeaders(this),
                                request, CreateContestResponseModel.class, new VolleyErrorListener(this, actionID)) {

                            @Override
                            protected void deliverResponse(CreateContestResponseModel response) {
                                updateUi(true, actionID, response);
                            }
                        });
                    } else if (mMirrorId3 != -1) {
                        RequestManager.addRequest(new GsonObjectRequest<CreateContestResponseModel>(IWebServices.REQUEST_CREATE_CONTEST_URL, NetworkUtil.getHeaders(this),
                                request, CreateContestResponseModel.class, new VolleyErrorListener(this, actionID)) {

                            @Override
                            protected void deliverResponse(CreateContestResponseModel response) {
                                updateUi(true, actionID, response);
                            }
                        });
                    } else {
                        removeProgressDialog();
                        Snackbar.make(mRootView, getString(R.string.please_enter_valid_mirror_name), Snackbar.LENGTH_LONG).show();
                    }

                } else {
                    removeProgressDialog();
                    Snackbar.make(mRootView, getString(R.string.please_enter_valid_mirror_name), Snackbar.LENGTH_LONG).show();
                }
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @Override
    public void onAuthError() {

    }

    /**
     * Method is used to open dialog for search result list.
     */
    private void onSearchClick(int mirrorNumber, String searchText) {
        mMirrorNumber = mirrorNumber;
        mContestSearchDialogFragment = ContestSearchDialogFragment.newInstance(searchText);
        mContestSearchDialogFragment.show(getSupportFragmentManager(), "ContestSearchDialogFragment");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_create_contest:

                if (mEtFirst.getText().toString().trim().length() > 0 && mEtSecond.getText().toString().trim().length() > 0) {

                    getData(IApiEvent.REQUEST_CREATE_CONTEST_CODE);
                } else {
                    Snackbar.make(mRootView, getString(R.string.please_fill_at_least_2_option_field), Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_search1:
                if (mEtFirst.getText().toString().trim().length() > 0)
                    onSearchClick(1, mEtFirst.getText().toString());
                break;

            case R.id.iv_search2:
                if (mEtSecond.getText().toString().trim().length() > 0)
                    onSearchClick(2, mEtSecond.getText().toString());
                break;

            case R.id.iv_search3:
                if (mEtThird.getText().toString().trim().length() > 0)
                    onSearchClick(3, mEtThird.getText().toString());
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
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                finish();
                break;

            case R.id.fl_arena:
                hideReveal();
                Intent intentArena = new Intent(this, ArenaActivity.class);
                startActivity(intentArena);
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

    @Override
    public void onMirrorClick(SearchInNewsFeedResponseModel.MirrorDetails mirrorDetails) {

        mMirrorName = mirrorDetails.mirrorName;

        switch (mMirrorNumber) {
            case 1:
                mMirrorId1 = mirrorDetails.mirrorID;
                mEtFirst.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");
                break;

            case 2:
                mMirrorId2 = mirrorDetails.mirrorID;
                mEtSecond.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");
                break;

            case 3:
                mMirrorId3 = mirrorDetails.mirrorID;
                mEtThird.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");
                break;
        }
        mContestSearchDialogFragment.dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(!mMirrorName.equals(s.toString())){

            if (mEtFirst.hasFocus()) {
                mMirrorId1 = -1;
            } else if (mEtSecond.hasFocus()) {
                mMirrorId2 = -1;
            } else if (mEtThird.hasFocus()) {
                mMirrorId3 = -1;
            }
        }
        mMirrorName = "";
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
