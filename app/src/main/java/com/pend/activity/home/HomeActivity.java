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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.adapters.HomePostsAdapter;
import com.pend.fragments.CommentsDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.AddAndUpdateCommentResponseModel;
import com.pend.models.AddAndUpdatePostResponseModel;
import com.pend.models.GetPostsResponseModel;
import com.pend.models.PostLikeResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener, HomePostsAdapter.IHomePostsAdapterCallBack, CommentsDialogFragment.ICommentsDialogCallBack {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private RecyclerView mRecyclerViewPost;
    private ArrayList<GetPostsResponseModel.GetPostsDetails> mPostsDetailsList;
    private int mPageNumber = 1;
    private int mMirrorId;
    private TextView mTvDataNotAvailable;
    private View mRootView;
    private View mRlQuarterView;
    private View mFlMenuView;
    private View mFlQuarterBlackView;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;
    private int mPostId;
    private boolean mIsLike;
    private boolean mIsUnLike;
    private String mCommentText;
    private boolean mIsUpdateRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_POSTS_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRecyclerViewPost = findViewById(R.id.recycler_view_post);
        mTvDataNotAvailable = findViewById(R.id.tv_data_not_available);

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        quarterView.findViewById(R.id.iv_profile).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {
        mIsUpdateRequired = false;
        mIsHasNextPage = false;
        mIsLoading = false;
        mPageNumber = 1;
        mPostsDetailsList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewPost.setLayoutManager(linearLayoutManager);

        mRecyclerViewPost.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_GET_POSTS_CODE);
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

                            mIsHasNextPage = !postsResponseModel.Data.hasNextPage;

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
                    mPostsDetailsList.clear();
                    HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
                    homePostsAdapter.setPostsDetailsList(mPostsDetailsList);
                    homePostsAdapter.notifyDataSetChanged();
                    OtherUtil.showErrorMessage(this, serviceResponse);

                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                mIsLoading = false;
                break;

            case IApiEvent.REQUEST_POST_LIKE_CODE:
                if (status) {
                    PostLikeResponseModel postLikeResponseModel = (PostLikeResponseModel) serviceResponse;
                    if (postLikeResponseModel != null && postLikeResponseModel.status) {
                        LoggerUtil.d(TAG, postLikeResponseModel.statusCode);

                        if (postLikeResponseModel.Data != null && postLikeResponseModel.Data.likeData != null) {

                            int position = 0;
                            for (GetPostsResponseModel.GetPostsDetails postsDetails : mPostsDetailsList) {
                                if (postsDetails.postID == postLikeResponseModel.Data.likeData.postID) {

                                    postsDetails.isLike = postLikeResponseModel.Data.likeData.isLike;
                                    postsDetails.isUnLike = postLikeResponseModel.Data.likeData.isUnLike;
                                    postsDetails.likeCount = postLikeResponseModel.Data.likeData.likeCount;
                                    postsDetails.unlikeCount = postLikeResponseModel.Data.likeData.unlikeCount;

                                    position = mPostsDetailsList.indexOf(postsDetails);
                                    break;
                                }
                            }
                            HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
                            homePostsAdapter.setPostsDetailsList(mPostsDetailsList);
                            homePostsAdapter.notifyItemChanged(position);
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_REMOVE_POST_CODE:
                if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                        int index = 0;
                        for (GetPostsResponseModel.GetPostsDetails postsDetails : mPostsDetailsList) {
                            if (mPostId == postsDetails.postID) {
                                index = mPostsDetailsList.indexOf(postsDetails);
                            }
                        }
                        mPostsDetailsList.remove(index);
                        HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
                        homePostsAdapter.setPostsDetailsList(mPostsDetailsList);
                        homePostsAdapter.notifyDataSetChanged();

                        Snackbar.make(mRootView, getString(R.string.post_remove_successfully), Snackbar.LENGTH_LONG).show();

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:
                if (status) {
                    AddAndUpdateCommentResponseModel addAndUpdateCommentResponseModel = (AddAndUpdateCommentResponseModel) serviceResponse;
                    if (addAndUpdateCommentResponseModel != null && addAndUpdateCommentResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdateCommentResponseModel.statusCode);

                        if (addAndUpdateCommentResponseModel.Data != null && addAndUpdateCommentResponseModel.Data.commentData != null) {

                            int position = 0;
                            for (GetPostsResponseModel.GetPostsDetails tempPostDetails : mPostsDetailsList) {
                                if (tempPostDetails.postID == addAndUpdateCommentResponseModel.Data.commentData.postID) {

                                    tempPostDetails.commentCount += 1;
                                    tempPostDetails.commentUserImageName = addAndUpdateCommentResponseModel.Data.commentData.imageName;
                                    tempPostDetails.commentUserImageURL = addAndUpdateCommentResponseModel.Data.commentData.commentUserImageURL;
                                    tempPostDetails.commentUserFullName = addAndUpdateCommentResponseModel.Data.commentData.userFullName;
                                    tempPostDetails.commentText = addAndUpdateCommentResponseModel.Data.commentData.commentText;
                                    position = mPostsDetailsList.indexOf(tempPostDetails);
                                    break;
                                }
                            }

                            HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
                            homePostsAdapter.notifyItemChanged(position);
                            Snackbar.make(mRootView, getString(R.string.add_comment_successfully), Snackbar.LENGTH_LONG).show();
                        }

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

        JsonObject jsonObject;
        String request;

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(HomeActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_GET_POSTS_CODE:

                //TODO Change mirrorId.
                mMirrorId = 6;
                String reflectionUserUrl = IWebServices.REQUEST_GET_POSTS_URL + Constants.PARAM_USER_ID + "=" + userId
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

            case IApiEvent.REQUEST_POST_LIKE_CODE:

                jsonObject = RequestPostDataUtil.postLikeApiRegParam(userId, mPostId, mIsLike, mIsUnLike);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<PostLikeResponseModel>(IWebServices.REQUEST_POST_LIKE_URL, NetworkUtil.getHeaders(this),
                        request, PostLikeResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(PostLikeResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_REMOVE_POST_CODE:

                jsonObject = RequestPostDataUtil.removePostApiRegParam(userId, mPostId, mMirrorId);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<BaseResponseModel>(IWebServices.REQUEST_REMOVE_POST_URL, NetworkUtil.getHeaders(this),
                        request, BaseResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(BaseResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:

                jsonObject = RequestPostDataUtil.addCommentApiRegParam(userId, mPostId, mCommentText);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<AddAndUpdateCommentResponseModel>(IWebServices.REQUEST_ADD_COMMENT_URL, NetworkUtil.getHeaders(this),
                        request, AddAndUpdateCommentResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(AddAndUpdateCommentResponseModel response) {
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
            case R.id.iv_profile:
                hideReveal();
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentMirror = new Intent(HomeActivity.this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentContest = new Intent(HomeActivity.this, ContestActivity.class);
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
    public void onCommentClick(int position) {
        CommentsDialogFragment commentsDialogFragment = CommentsDialogFragment.newInstance(mPostsDetailsList.get(position));
        commentsDialogFragment.show(getSupportFragmentManager(), "CommentsDialogFragment");
    }

    @Override
    public void onMenuClick(final int position, View view) {
        PopupMenu popup = new PopupMenu(this, view, Gravity.END);
        popup.getMenuInflater().inflate(R.menu.post_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent;

                switch (item.getTitle().toString()) {
                    case Constants.UPDATE_POST:
                        intent = new Intent(HomeActivity.this, CreatePostActivity.class);
                        intent.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                        intent.putExtra(Constants.POST_DETAILS_KEY, mPostsDetailsList.get(position));
                        startActivity(intent);
                        return true;

                    case Constants.REMOVE_POST:
                        mPostId = mPostsDetailsList.get(position).postID;
                        getData(IApiEvent.REQUEST_REMOVE_POST_CODE);
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onSendClick(int position, String commentText) {
        mCommentText = commentText;
        mPostId = mPostsDetailsList.get(position).postID;
        getData(IApiEvent.REQUEST_ADD_COMMENT_CODE);
    }

    @Override
    public void onLikeOrDislikeClick(int position, boolean isLike, boolean isUnLike) {

        mPostId = mPostsDetailsList.get(position).postID;
        mIsLike = isLike;
        mIsUnLike = isUnLike;

        getData(IApiEvent.REQUEST_POST_LIKE_CODE);
    }

    @Override
    public void onPostUpdate(GetPostsResponseModel.GetPostsDetails postDetails) {
        int position = 0;
        for (GetPostsResponseModel.GetPostsDetails tempPostDetails : mPostsDetailsList) {
            if (tempPostDetails.postID == postDetails.postID) {

                tempPostDetails.isUnLike = postDetails.isUnLike;
                tempPostDetails.isLike = postDetails.isLike;
                tempPostDetails.likeCount = postDetails.likeCount;
                tempPostDetails.unlikeCount = postDetails.unlikeCount;

                tempPostDetails.commentCount = postDetails.commentCount;
                tempPostDetails.commentUserImageName = postDetails.commentUserImageName;
                tempPostDetails.commentUserImageURL = postDetails.commentUserImageURL;
                tempPostDetails.commentUserFullName = postDetails.commentUserFullName;
                tempPostDetails.commentText = postDetails.commentText;
                position = mPostsDetailsList.indexOf(tempPostDetails);
                break;
            }
        }

        HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
        homePostsAdapter.notifyItemChanged(position);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsUpdateRequired) {
            mIsUpdateRequired = false;
            mPostsDetailsList.clear();
            getData(IApiEvent.REQUEST_GET_POSTS_CODE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsUpdateRequired = true;
    }
}
