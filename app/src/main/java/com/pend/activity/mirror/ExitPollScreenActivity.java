package com.pend.activity.mirror;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.adapters.ExitPollAdapter;
import com.pend.adapters.ExitPollViewPagerAdapter;
import com.pend.arena.view.ArenaActivity;
import com.pend.fragments.ExitPollUnVotingDialogFragment;
import com.pend.fragments.ExitPollVotingDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IExitPollVotingDialogCallBack;
import com.pend.interfaces.IWebServices;
import com.pend.models.ExitPollVoteResponseModel;
import com.pend.models.GetExitPollListResponseModel;
import com.pend.models.GetExitPollMirrorResponseModel;
import com.pend.models.UserProfileResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExitPollScreenActivity extends BaseActivity implements View.OnClickListener, ExitPollAdapter.IExitPollAdapterCallBack, IExitPollVotingDialogCallBack,
        ExitPollViewPagerAdapter.IExitPollViewPagerAdapterCallBack {

    private static final String TAG = ExitPollScreenActivity.class.getSimpleName();
    private View mRootView;
    private ViewPager mViewpagerProfile;
    private TextView mTvCreatedBy;
    private TextView mTvWikiLink;
    private int mPageNumber;
    private int mMirrorId;
    private ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> mExitPollList;
    private RecyclerView mRecyclerViewExitPoll;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;
    private boolean mIsVoted;
    private View mRlLargeView;
    private ImageView mIvLargeProfile;
    private ArrayList<UserProfileResponseModel.ImageDetails> mImageDataList;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;
    private ImageView mIvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_poll_screen);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.MIRROR_ID_KEY)) {
                mMirrorId = localBundle.getInt(Constants.MIRROR_ID_KEY, 0);
            }
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_EXIT_POLL_MIRROR_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRlLargeView = findViewById(R.id.rl_large_view);
        mIvLargeProfile = findViewById(R.id.iv_large_profile);
        mViewpagerProfile = findViewById(R.id.viewpager_profile);
        mTvWikiLink = findViewById(R.id.tv_wiki_link);
        mTvCreatedBy = findViewById(R.id.tv_created_by);
        mRecyclerViewExitPoll = findViewById(R.id.recycler_view_exit_poll);

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

        ((TextView) quarterView.findViewById(R.id.tv_mirror)).setText(String.valueOf(getResources().getString(R.string.home)));
        ((ImageView) quarterView.findViewById(R.id.iv_mirror)).setImageDrawable(getResources().getDrawable(R.drawable.home));
        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        mIvProfile = quarterView.findViewById(R.id.iv_profile);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
        mIvProfile.setOnClickListener(this);

        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_related_contest).setOnClickListener(this);
        findViewById(R.id.tv_related_contest).setOnClickListener(this);
        findViewById(R.id.iv_reflection).setOnClickListener(this);
        findViewById(R.id.tv_reflections).setOnClickListener(this);
        findViewById(R.id.iv_exit_polls).setOnClickListener(this);
        findViewById(R.id.tv_exit_polls).setOnClickListener(this);

    }

    @Override
    protected void setInitialData() {

        String imageUrl = SharedPrefUtils.getProfileImageUrl(this);

        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.with(this)
                    .load(imageUrl)
                    .into(mIvProfile);
        }

        mIsVoted = false;
        mPageNumber = 1;
        mIsHasNextPage = false;
        mIsLoading = false;
        mExitPollList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewExitPoll.setLayoutManager(linearLayoutManager);

        mRecyclerViewExitPoll.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_GET_EXIT_POLL_LIST_CODE);
            }

            @Override
            public boolean isLastPage() {
                return mIsHasNextPage;
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }
        });
        mRecyclerViewExitPoll.setAdapter(new ExitPollAdapter(this, mExitPollList));

        //TODO remove this code
        mImageDataList = new ArrayList<>();
        UserProfileResponseModel.ImageDetails imageDetails = new UserProfileResponseModel.ImageDetails();
        imageDetails.imageURL = "https://cdn.pixabay.com/photo/2016/06/18/17/42/image-1465348_960_720.jpg";
        mImageDataList.add(imageDetails);
        mImageDataList.add(imageDetails);
        mImageDataList.add(imageDetails);
        mImageDataList.add(imageDetails);
        mImageDataList.add(imageDetails);
        mImageDataList.add(imageDetails);
        mImageDataList.add(imageDetails);
        mImageDataList.add(imageDetails);
        mImageDataList.add(imageDetails);

        mViewpagerProfile.setAdapter(new ExitPollViewPagerAdapter(this, mImageDataList));
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_EXIT_POLL_MIRROR_CODE:
                if (status) {
                    GetExitPollMirrorResponseModel exitPollMirrorResponseModel = (GetExitPollMirrorResponseModel) serviceResponse;
                    if (exitPollMirrorResponseModel != null && exitPollMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, exitPollMirrorResponseModel.statusCode);

                        if (exitPollMirrorResponseModel.Data != null && exitPollMirrorResponseModel.Data.mirrorData != null) {

                            final GetExitPollMirrorResponseModel.GetExitPollMirrorDetails mirrorDetails = exitPollMirrorResponseModel.Data.mirrorData;
                            mTvCreatedBy.setText(mirrorDetails.userFullName != null ? mirrorDetails.userFullName : "");

                            String wikiLink;
                            if (mirrorDetails.mirrorWikiLink != null &&
                                    !mirrorDetails.mirrorWikiLink.equals("") && !mirrorDetails.mirrorWikiLink.equals("NA")) {

                                wikiLink = "<a href=" + mirrorDetails.mirrorWikiLink + ">Wiki link</a>";
                                mTvWikiLink.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(mirrorDetails.mirrorWikiLink));
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                wikiLink = "NA";
                            }
                            mTvWikiLink.setText(Html.fromHtml(wikiLink));
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    OtherUtil.showErrorMessage(this, serviceResponse);
                }
                getData(IApiEvent.REQUEST_GET_EXIT_POLL_LIST_CODE);
                break;

            case IApiEvent.REQUEST_GET_EXIT_POLL_LIST_CODE:
                if (status) {
                    GetExitPollListResponseModel exitPollListResponseModel = (GetExitPollListResponseModel) serviceResponse;
                    if (exitPollListResponseModel != null && exitPollListResponseModel.status) {
                        LoggerUtil.d(TAG, exitPollListResponseModel.statusCode);

                        if (exitPollListResponseModel.Data != null && exitPollListResponseModel.Data.exitPollList != null) {

                            mIsHasNextPage = !exitPollListResponseModel.Data.hasNextPage;
                            ExitPollAdapter exitPollAdapter = (ExitPollAdapter) mRecyclerViewExitPoll.getAdapter();
                            mExitPollList.addAll(exitPollListResponseModel.Data.exitPollList);

                            exitPollAdapter.setExitPollList(mExitPollList);
                            exitPollAdapter.notifyDataSetChanged();
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                mIsLoading = false;
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
            case IApiEvent.REQUEST_GET_EXIT_POLL_MIRROR_CODE:

                String exitPollMirrorUrl = IWebServices.REQUEST_GET_EXIT_POLL_MIRROR_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this) + "&" +
                        Constants.PARAM_MIRROR_ID + "=" + mMirrorId;
                RequestManager.addRequest(new GsonObjectRequest<GetExitPollMirrorResponseModel>(exitPollMirrorUrl, NetworkUtil.getHeaders(this), null,
                        GetExitPollMirrorResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetExitPollMirrorResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_GET_EXIT_POLL_LIST_CODE:

                String exitPollListUrl = IWebServices.REQUEST_GET_EXIT_POLL_LIST_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this) + "&" +
                        Constants.PARAM_MIRROR_ID + "=" + mMirrorId + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                RequestManager.addRequest(new GsonObjectRequest<GetExitPollListResponseModel>(exitPollListUrl, NetworkUtil.getHeaders(this), null,
                        GetExitPollListResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetExitPollListResponseModel response) {
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

            case R.id.iv_exit_polls:
            case R.id.tv_exit_polls:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.iv_related_contest:
            case R.id.tv_related_contest:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.iv_reflection:
            case R.id.tv_reflections:

                Intent intentReflection = new Intent(ExitPollScreenActivity.this, ReflectionsActivity.class);
                intentReflection.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                startActivity(intentReflection);
                break;

            case R.id.tv_share_on_facebook:
                break;

            case R.id.iv_close:
                mRlLargeView.setVisibility(View.GONE);
                break;

            case R.id.iv_profile:
                hideReveal();
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                finish();
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentContest = new Intent(this, ContestActivity.class);
                startActivity(intentContest);
//                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();

                break;

            case R.id.fl_area:
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

    @Override
    public void onViewClick(int position) {
        GetExitPollListResponseModel.GetExitPollListDetails exitPollListDetails = mExitPollList.get(position);

        mIsVoted = exitPollListDetails.pollAdmire || exitPollListDetails.pollHate || exitPollListDetails.pollCantSay;

        if (!mIsVoted) {
            DialogFragment votingDialogFragment = ExitPollVotingDialogFragment.newInstance(exitPollListDetails.mirrorID, exitPollListDetails.exitPollID);
            votingDialogFragment.show(getSupportFragmentManager(), "ExitPollVotingDialogFragment");
        } else {
            DialogFragment unVotingDialogFragment = ExitPollUnVotingDialogFragment.newInstance(exitPollListDetails.mirrorID, exitPollListDetails.exitPollID);
            unVotingDialogFragment.show(getSupportFragmentManager(), "ExitPollUnVotingDialogFragment");
        }
    }

    @Override
    public void onVotingOrUnVotingClick(ExitPollVoteResponseModel.ExitPollVoteDetails exitPollVoteDetails) {
        int position = 0;
        for (GetExitPollListResponseModel.GetExitPollListDetails exitPollListDetails : mExitPollList) {
            if (exitPollListDetails.exitPollID == exitPollVoteDetails.exitPollID) {

                exitPollListDetails.pollAdmirePer = exitPollVoteDetails.pollAdmirePer;
                exitPollListDetails.pollHatePer = exitPollVoteDetails.pollHatePer;
                exitPollListDetails.pollCantSayPer = exitPollVoteDetails.pollCantSayPer;
                exitPollListDetails.pollAdmire = exitPollVoteDetails.pollAdmire;
                exitPollListDetails.pollHate = exitPollVoteDetails.pollHate;
                exitPollListDetails.pollCantSay = exitPollVoteDetails.pollCantSay;

                position = mExitPollList.indexOf(exitPollListDetails);
                break;
            }
        }

        ExitPollAdapter exitPollAdapter = (ExitPollAdapter) mRecyclerViewExitPoll.getAdapter();
        exitPollAdapter.setExitPollList(mExitPollList);
        exitPollAdapter.notifyItemChanged(position);
    }

    @Override
    public void onImageClick(int position) {

        mRlLargeView.setVisibility(View.VISIBLE);

        Picasso.with(this)
                .load(mImageDataList.get(position).imageURL)
                .into(mIvLargeProfile);
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
        } else if (mRlLargeView.getVisibility() == View.VISIBLE) {
            mRlLargeView.setVisibility(View.GONE);
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
