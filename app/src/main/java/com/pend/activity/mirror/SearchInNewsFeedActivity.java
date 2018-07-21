package com.pend.activity.mirror;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.fragments.ContestSearchFragment;
import com.pend.fragments.CreateMirrorDialogFragment;
import com.pend.fragments.MirrorSearchFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IMirrorFragmentCallBack;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetFollowingMirrorResponseModel;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class SearchInNewsFeedActivity extends BaseActivity implements View.OnClickListener, TextWatcher, IMirrorFragmentCallBack {

    private static final String TAG = SearchInNewsFeedActivity.class.getSimpleName();
    private static final int TRENDING_MIRROR = 0;
    private static final int FOLLOWING_MIRROR = 1;
    private static final int INTRODUCED_MIRROR = 2;
    private View mRootView;
    private Button mBtMirror;
    private Button mBtContest;
    private EditText mEtSearch;
    private String mSearchText;
    private boolean mIsMirror = true;
    private int mCurrentItem;
    private ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mMirrorList;
    private int mPageNumber;
    private MirrorSearchFragment mMirrorSearchFragment;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_news_feed);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {

            if (localBundle.containsKey(Constants.MIRROR_FRAGMENT_POSITION)) {
                mCurrentItem = localBundle.getInt(Constants.MIRROR_FRAGMENT_POSITION, -1);
            }
        }

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mBtMirror = findViewById(R.id.bt_mirror);
        mBtContest = findViewById(R.id.bt_contest);
        View customSearchView = findViewById(R.id.custom_search_view);
        mEtSearch = customSearchView.findViewById(R.id.et_search);

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        quarterView.findViewById(R.id.iv_profile).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);

        mBtMirror.setOnClickListener(this);
        mBtContest.setOnClickListener(this);
        mEtSearch.addTextChangedListener(this);
    }

    @Override
    protected void setInitialData() {

        mPageNumber = 1;
        mMirrorList = new ArrayList<>();
        mBtMirror.setTextColor(getResources().getColor(R.color.white));
        mBtMirror.setBackground(getResources().getDrawable(R.drawable.custom_blue_button));

        mMirrorSearchFragment = MirrorSearchFragment.newInstance(mMirrorList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, mMirrorSearchFragment);
        transaction.commit();
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_TRENDING_CODE:
                if (status) {
                    GetTrendingAndIntroducedMirrorResponseModel trendingAndIntroducedMirrorResponseModel = (GetTrendingAndIntroducedMirrorResponseModel) serviceResponse;
                    if (trendingAndIntroducedMirrorResponseModel != null && trendingAndIntroducedMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, trendingAndIntroducedMirrorResponseModel.statusCode);

                        if (trendingAndIntroducedMirrorResponseModel.Data != null && trendingAndIntroducedMirrorResponseModel.Data.mirrorList != null) {

//                            mIsHasNextPage = !trendingAndIntroducedMirrorResponseModel.Data.hasNextPage;

                            mMirrorList.clear();
                            mMirrorList.addAll(trendingAndIntroducedMirrorResponseModel.Data.mirrorList);
                            mMirrorSearchFragment.setSearchDataList(mMirrorList);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    mMirrorList.clear();
                    mMirrorSearchFragment.setSearchDataList(mMirrorList);
                }
                break;

            case IApiEvent.REQUEST_GET_INTRODUCED_CODE:
                if (status) {
                    GetTrendingAndIntroducedMirrorResponseModel trendingAndIntroducedMirrorResponseModel = (GetTrendingAndIntroducedMirrorResponseModel) serviceResponse;
                    if (trendingAndIntroducedMirrorResponseModel != null && trendingAndIntroducedMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, trendingAndIntroducedMirrorResponseModel.statusCode);

                        if (trendingAndIntroducedMirrorResponseModel.Data != null && trendingAndIntroducedMirrorResponseModel.Data.mirrorList != null) {

//                            mIsHasNextPage = !trendingAndIntroducedMirrorResponseModel.Data.hasNextPage;

                            mMirrorList.clear();
                            mMirrorList.addAll(trendingAndIntroducedMirrorResponseModel.Data.mirrorList);
                            mMirrorSearchFragment.setSearchDataList(mMirrorList);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    mMirrorList.clear();
                    mMirrorSearchFragment.setSearchDataList(mMirrorList);
                }
                break;

            case IApiEvent.REQUEST_GET_FOLLOWING_CODE:
                if (status) {
                    GetFollowingMirrorResponseModel followingMirrorResponseModel = (GetFollowingMirrorResponseModel) serviceResponse;
                    if (followingMirrorResponseModel != null && followingMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, followingMirrorResponseModel.statusCode);

                        if (followingMirrorResponseModel.Data != null && followingMirrorResponseModel.Data.mirrorList != null) {

//                            mIsHasNextPage = !trendingAndIntroducedMirrorResponseModel.Data.hasNextPage;
                            mMirrorList.clear();

                            for (GetFollowingMirrorResponseModel.GetFollowingMirrorDetails followingMirrorDetails : followingMirrorResponseModel.Data.mirrorList) {
                                mMirrorList.add(
                                        new GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails(followingMirrorDetails.mirrorID
                                                , followingMirrorDetails.mirrorName, followingMirrorDetails.imageURL, followingMirrorDetails.mirrorInfo
                                                , followingMirrorDetails.mirrorWikiLink));
                            }

                            mMirrorSearchFragment.setSearchDataList(mMirrorList);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    mMirrorList.clear();
                    mMirrorSearchFragment.setSearchDataList(mMirrorList);
                }
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }

//        removeProgressDialog();
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!NetworkUtil.isInternetConnected(this)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }
//        showProgressDialog();
        switch (actionID) {
            case IApiEvent.REQUEST_GET_TRENDING_CODE:

                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    String trendingMirrorUrl = IWebServices.REQUEST_GET_TRENDING_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber)
                            + "&" + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;

                    RequestManager.addRequest(new GsonObjectRequest<GetTrendingAndIntroducedMirrorResponseModel>(trendingMirrorUrl,
                            NetworkUtil.getHeaders(this), null, GetTrendingAndIntroducedMirrorResponseModel.class,
                            new VolleyErrorListener(this, actionID)) {

                        @Override
                        protected void deliverResponse(GetTrendingAndIntroducedMirrorResponseModel response) {
                            updateUi(true, actionID, response);
                        }
                    });
                }
                break;

            case IApiEvent.REQUEST_GET_INTRODUCED_CODE:

                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    String introducedUrl = IWebServices.REQUEST_GET_INTRODUCED_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber)
                            + "&" + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;

                    RequestManager.addRequest(new GsonObjectRequest<GetTrendingAndIntroducedMirrorResponseModel>(introducedUrl,
                            NetworkUtil.getHeaders(this), null, GetTrendingAndIntroducedMirrorResponseModel.class,
                            new VolleyErrorListener(this, actionID)) {

                        @Override
                        protected void deliverResponse(GetTrendingAndIntroducedMirrorResponseModel response) {
                            updateUi(true, actionID, response);
                        }
                    });
                }
                break;

            case IApiEvent.REQUEST_GET_FOLLOWING_CODE:

                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    String followingUrl = IWebServices.REQUEST_GET_FOLLOWING_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber)
                            + "&" + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;

                    RequestManager.addRequest(new GsonObjectRequest<GetFollowingMirrorResponseModel>(followingUrl,
                            NetworkUtil.getHeaders(this), null, GetFollowingMirrorResponseModel.class,
                            new VolleyErrorListener(this, actionID)) {

                        @Override
                        protected void deliverResponse(GetFollowingMirrorResponseModel response) {
                            updateUi(true, actionID, response);
                        }
                    });
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mEtSearch.hasFocus()) {
            mSearchText = mEtSearch.getText().toString().trim();
        }

        switch (mCurrentItem) {
            case TRENDING_MIRROR:
                getData(IApiEvent.REQUEST_GET_TRENDING_CODE);
                break;

            case FOLLOWING_MIRROR:
                getData(IApiEvent.REQUEST_GET_FOLLOWING_CODE);
                break;

            case INTRODUCED_MIRROR:
                getData(IApiEvent.REQUEST_GET_INTRODUCED_CODE);
                break;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_mirror:

                mIsMirror = true;

                mBtMirror.setTextColor(getResources().getColor(R.color.white));
                mBtMirror.setBackground(getResources().getDrawable(R.drawable.custom_blue_button));

                mBtContest.setTextColor(getResources().getColor(R.color.black));
                mBtContest.setBackground(getResources().getDrawable(R.drawable.custom_blue_border));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fl_container, mMirrorSearchFragment);
                transaction.commit();
                break;

            case R.id.bt_contest:

                mIsMirror = false;

                mBtContest.setTextColor(getResources().getColor(R.color.white));
                mBtContest.setBackground(getResources().getDrawable(R.drawable.custom_blue_button));

                mBtMirror.setTextColor(getResources().getColor(R.color.black));
                mBtMirror.setBackground(getResources().getDrawable(R.drawable.custom_blue_border));

                ContestSearchFragment contestSearchFragment = new ContestSearchFragment();
                FragmentTransaction transactionContest = getSupportFragmentManager().beginTransaction();
                transactionContest.replace(R.id.fl_container, contestSearchFragment);
                transactionContest.commit();
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
    public void onCreateMirrorClick() {
        DialogFragment createMirrorDialogFragment = new CreateMirrorDialogFragment();
        createMirrorDialogFragment.show(getSupportFragmentManager(), "CreateMirrorDialogFragment");
    }
}
