package com.pend.activity.mirror;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.ExitPollAdapter;
import com.pend.adapters.ExitPollViewPagerAdapter;
import com.pend.fragments.ExitPollUnVotingDialogFragment;
import com.pend.fragments.ExitPollVotingDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetExitPollListResponseModel;
import com.pend.models.GetExitPollMirrorResponseModel;
import com.pend.models.UserProfileResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class ExitPollScreenActivity extends BaseActivity implements View.OnClickListener, ExitPollAdapter.IExitPollAdapterCallBack {

    private static final String TAG = ExitPollScreenActivity.class.getSimpleName();
    private View mRootView;
    private ViewPager mViewpagerProfile;
    private TextView mTvCreatedBy;
    private TextView mTvWikiLink;
    private TextView mTvCategory;
    private int mPageNumber;
    private int mMirrorId;
    private ArrayList<GetExitPollListResponseModel.GetExitPollListDetails> mExitPollList;
    private RecyclerView mRecyclerViewExitPoll;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;
    private boolean mIsVoted;

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
        mViewpagerProfile = findViewById(R.id.viewpager_profile);
        mTvCategory = findViewById(R.id.tv_category);
        mTvWikiLink = findViewById(R.id.tv_wiki_link);
        mTvCreatedBy = findViewById(R.id.tv_created_by);
        mRecyclerViewExitPoll = findViewById(R.id.recycler_view_exit_poll);

        findViewById(R.id.tv_related_contest).setOnClickListener(this);
        findViewById(R.id.tv_reflections).setOnClickListener(this);

    }

    @Override
    protected void setInitialData() {

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
        ArrayList<UserProfileResponseModel.ImageDetails> imageData = new ArrayList<>();
        UserProfileResponseModel.ImageDetails imageDetails = new UserProfileResponseModel.ImageDetails();
        imageDetails.imageURL = "https://cdn.pixabay.com/photo/2016/06/18/17/42/image-1465348_960_720.jpg";
        imageData.add(imageDetails);
        imageData.add(imageDetails);
        imageData.add(imageDetails);
        imageData.add(imageDetails);
        imageData.add(imageDetails);
        imageData.add(imageDetails);
        imageData.add(imageDetails);
        imageData.add(imageDetails);
        imageData.add(imageDetails);

        mViewpagerProfile.setAdapter(new ExitPollViewPagerAdapter(this, imageData));
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

                            GetExitPollMirrorResponseModel.GetExitPollMirrorDetails mirrorDetails = exitPollMirrorResponseModel.Data.mirrorData;
                            mTvCategory.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");
                            mTvWikiLink.setText(mirrorDetails.mirrorWikiLink != null ? mirrorDetails.mirrorWikiLink : "");
                            mTvCreatedBy.setText(mirrorDetails.userFullName != null ? mirrorDetails.userFullName : "");
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
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
                    Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_related_contest:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.tv_reflections:

                Intent intentReflection = new Intent(ExitPollScreenActivity.this, ReflectionsActivity.class);
                intentReflection.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                startActivity(intentReflection);
                break;

            case R.id.tv_share_on_facebook:
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @Override
    public void onViewClick(int position) {
        GetExitPollListResponseModel.GetExitPollListDetails exitPollListDetails = mExitPollList.get(position);

        if (exitPollListDetails.pollAdmire || exitPollListDetails.pollHate || exitPollListDetails.pollCantSay) {
            mIsVoted = true;
        }

        if (!mIsVoted) {
            DialogFragment votingDialogFragment = ExitPollVotingDialogFragment.newInstance(exitPollListDetails.mirrorID, exitPollListDetails.exitPollID);
            votingDialogFragment.show(getSupportFragmentManager(), "ExitPollVotingDialogFragment");
        } else {
            DialogFragment unVotingDialogFragment = ExitPollUnVotingDialogFragment.newInstance(exitPollListDetails.mirrorID, exitPollListDetails.exitPollID);
            unVotingDialogFragment.show(getSupportFragmentManager(), "ExitPollUnVotingDialogFragment");
        }
    }
}
