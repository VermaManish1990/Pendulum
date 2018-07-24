package com.pend.activity.mirror;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.login.EditMyProfileActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.adapters.SearchMirrorAdapter;
import com.pend.fragments.CreateMirrorDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.CreateMirrorResponseModel;
import com.pend.models.SearchMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class SearchMirrorListingActivity extends BaseActivity implements View.OnClickListener, SearchMirrorAdapter.ISearchMirrorAdapterCallBack {

    private static final String TAG = SearchMirrorListingActivity.class.getSimpleName();
    private View mRootView;
    private RecyclerView mRecyclerViewSearch;
    private String mSearchText;
    private ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> mSearchDataList;
    private SearchMirrorResponseModel.SearchMirrorDetails mMirrorDetails;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_mirror_listing);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.SEARCH_TEXT_KEY)) {
                mSearchText = localBundle.getString(Constants.SEARCH_TEXT_KEY, null);
            }
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_SEARCH_MIRROR_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRecyclerViewSearch = findViewById(R.id.recycler_view_search);

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        quarterView.findViewById(R.id.iv_profile).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);


        findViewById(R.id.tv_create_mirror).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {
        mSearchDataList = new ArrayList<>();

        mRecyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewSearch.setAdapter(new SearchMirrorAdapter(this, mSearchDataList));

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_MIRROR_CODE:
                if (status) {
                    SearchMirrorResponseModel searchMirrorResponseModel = (SearchMirrorResponseModel) serviceResponse;
                    if (searchMirrorResponseModel != null && searchMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, searchMirrorResponseModel.statusCode);

                        if (searchMirrorResponseModel.Data != null && searchMirrorResponseModel.Data.searchData != null) {
                            SearchMirrorAdapter searchMirrorAdapter = (SearchMirrorAdapter) mRecyclerViewSearch.getAdapter();
                            mSearchDataList.addAll(searchMirrorResponseModel.Data.searchData);
                            searchMirrorAdapter.setSearchDataList(mSearchDataList);
                            searchMirrorAdapter.notifyDataSetChanged();
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this,serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_CREATE_MIRROR_CODE:
                if (status) {
                    CreateMirrorResponseModel createMirrorResponseModel = (CreateMirrorResponseModel) serviceResponse;
                    if (createMirrorResponseModel != null && createMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, createMirrorResponseModel.statusCode);

                        if (createMirrorResponseModel.Data != null && createMirrorResponseModel.Data.mirrorData != null) {
                            Snackbar.make(mRootView, R.string.mirror_created_successfully, Snackbar.LENGTH_LONG).show();
                            Intent intent = new Intent(SearchMirrorListingActivity.this,MirrorDetailsActivity.class);
                            intent.putExtra(Constants.MIRROR_ID_KEY, createMirrorResponseModel.Data.mirrorData.mirrorID);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this,serviceResponse);
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
        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(SearchMirrorListingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }


        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_MIRROR_CODE:

                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    String searchMirrorUrl = IWebServices.REQUEST_SEARCH_MIRROR_URL + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;
                    RequestManager.addRequest(new GsonObjectRequest<SearchMirrorResponseModel>(searchMirrorUrl, NetworkUtil.getHeaders(this),
                            null, SearchMirrorResponseModel.class, new VolleyErrorListener(this, actionID)) {

                        @Override
                        protected void deliverResponse(SearchMirrorResponseModel response) {
                            updateUi(true, actionID, response);

                        }
                    });
                } else {
                    LoggerUtil.d(TAG, getString(R.string.search_text_is_not_valid));
                }
                break;

            case IApiEvent.REQUEST_CREATE_MIRROR_CODE:

                JsonObject requestObject = RequestPostDataUtil.createMirrorApiRegParam(userId, String.valueOf(mMirrorDetails.mirrorUniqueID), mMirrorDetails.mirrorName,
                        mMirrorDetails.imageUrl, mMirrorDetails.mirrorInfo, mMirrorDetails.mirrorWikiLink);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<CreateMirrorResponseModel>(IWebServices.REQUEST_CREATE_MIRROR_URL, NetworkUtil.getHeaders(this),
                        request, CreateMirrorResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(CreateMirrorResponseModel response) {
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
            case R.id.tv_create_mirror:
                DialogFragment createMirrorDialogFragment = new CreateMirrorDialogFragment();
                createMirrorDialogFragment.show(getSupportFragmentManager(), "CreateMirrorDialogFragment");
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
    public void onMirrorClick(int position) {
        mMirrorDetails = mSearchDataList.get(position);

        if (mMirrorDetails != null) {
            getData(IApiEvent.REQUEST_CREATE_MIRROR_CODE);
        }
    }
}
