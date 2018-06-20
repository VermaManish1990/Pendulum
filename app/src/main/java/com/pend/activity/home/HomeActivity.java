package com.pend.activity.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.adapters.HomePostsAdapter;
import com.pend.adapters.RecentPostAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetPostsResponseModel;
import com.pend.models.GetReflectionUsersResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private RecyclerView mRecyclerViewPost;
    private ArrayList<GetPostsResponseModel.GetPostsDetails> mPostsDetailsList;
    private int mPageNumber;
    private int mMirrorId;
    private TextView mTvDataNotAvailable;
    private View mRootView;
    private View mRlQuarterView;
    private View mFlMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        setInitialData();

//        getData(IApiEvent.REQUEST_GET_POSTS_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRlQuarterView = findViewById(R.id.rl_quarter_view);
        mFlMenuView = findViewById(R.id.fl_menu_view);
        mRecyclerViewPost = findViewById(R.id.recycler_view_post);
        mTvDataNotAvailable = findViewById(R.id.tv_data_not_available);
        findViewById(R.id.bt_profile).setOnClickListener(this);
        findViewById(R.id.bt_mirror).setOnClickListener(this);
        findViewById(R.id.bt_contest).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {
        mPostsDetailsList = new ArrayList<>();
        mRecyclerViewPost.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewPost.setAdapter(new HomePostsAdapter(this, mPostsDetailsList));
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_POSTS_CODE:
                if (status) {
                    GetPostsResponseModel postsResponseModel = (GetPostsResponseModel) serviceResponse;
                    if (postsResponseModel != null && postsResponseModel.status) {
                        LoggerUtil.d(TAG, postsResponseModel.statusCode);

                        if (postsResponseModel.Data != null && postsResponseModel.Data.postList != null) {

                            mTvDataNotAvailable.setVisibility(View.GONE);
                            mRecyclerViewPost.setVisibility(View.VISIBLE);

                            HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
                            mPostsDetailsList.addAll(postsResponseModel.Data.postList);
                            homePostsAdapter.setPostsDetailsList(mPostsDetailsList);
                            homePostsAdapter.notifyDataSetChanged();
                        } else {
                            mTvDataNotAvailable.setVisibility(View.VISIBLE);
                            mRecyclerViewPost.setVisibility(View.GONE);
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
            case IApiEvent.REQUEST_GET_POSTS_CODE:

                //TODO Change mirrorId and add pagination
                mPageNumber = 1;
                mMirrorId = 31;
                String reflectionUserUrl = IWebServices.REQUEST_GET_POSTS_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
                        + "&" + Constants.PARAM_MIRROR_ID + "=" + mMirrorId
                        + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                RequestManager.addRequest(new GsonObjectRequest<GetPostsResponseModel>(reflectionUserUrl, NetworkUtil.getHeaders(this),
                        null, GetPostsResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetPostsResponseModel response) {
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
            case R.id.bt_profile:
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.bt_mirror:
                Intent intentMirror = new Intent(HomeActivity.this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            case R.id.bt_contest:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
//                Intent intentContest = new Intent(HomeActivity.this, ContestActivity.class);
//                startActivity(intentContest);
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
                mRlQuarterView.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (mRlQuarterView.getVisibility() == View.VISIBLE) {
            hideReveal();
            mFlMenuView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Rect outRect = new Rect();
        mRlQuarterView.getGlobalVisibleRect(outRect);

        if (mRlQuarterView.getVisibility() == View.VISIBLE && !outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
            hideReveal();
            mFlMenuView.setVisibility(View.VISIBLE);
        }
        return super.dispatchTouchEvent(event);
    }
}
