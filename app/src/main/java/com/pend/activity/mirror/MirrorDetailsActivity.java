package com.pend.activity.mirror;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.activity.home.CreatePostActivity;
import com.pend.adapters.RecentPostAdapter;
import com.pend.fragments.CommentsDialogFragment;
import com.pend.fragments.MirrorUnVotingDialogFragment;
import com.pend.fragments.MirrorVotingDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IMirrorVotingDialogCallBack;
import com.pend.interfaces.IWebServices;
import com.pend.models.AddAndUpdateCommentResponseModel;
import com.pend.models.AddAndUpdatePostResponseModel;
import com.pend.models.GetMirrorDetailsResponseModel;
import com.pend.models.GetMirrorGraphResponseModel;
import com.pend.models.GetPostsResponseModel;
import com.pend.models.PostLikeResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pend.widget.progressbar.CustomProgressBar;
import com.pend.widget.progressbar.ProgressItem;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MirrorDetailsActivity extends BaseActivity implements View.OnClickListener, RecentPostAdapter.IRecentPostAdapterCallBack,
        CommentsDialogFragment.ICommentsDialogCallBack, IMirrorVotingDialogCallBack {

    private static final String TAG = MirrorDetailsActivity.class.getSimpleName();
    private View mRootView;
    private ImageView mIvProfile;
    private GraphView mGraphView;
    private TextView mTvName;
    private CustomProgressBar mProgressBarProfile;
    private RecyclerView mRecyclerViewPost;
    private int mMirrorId;
    private boolean mIsVoted;
    private ArrayList<GetPostsResponseModel.GetPostsDetails> mPostList;
    private TextView mTvDataNotAvailable;
    private int mPostId;
    private boolean mIsLike;
    private boolean mIsUnLike;
    private boolean mIsUpdateRequired;
    private String mCommentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror_details);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.MIRROR_ID_KEY)) {
                mMirrorId = localBundle.getInt(Constants.MIRROR_ID_KEY, 0);
            }
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mIvProfile = findViewById(R.id.iv_profile);
        mGraphView = findViewById(R.id.graph_view);
        mTvName = findViewById(R.id.tv_name);
        mTvDataNotAvailable = findViewById(R.id.tv_data_not_available);
        mProgressBarProfile = findViewById(R.id.progress_bar_profile);
        mRecyclerViewPost = findViewById(R.id.recycler_view_post);

        findViewById(R.id.view_create_a_new_post).setOnClickListener(this);
        findViewById(R.id.view_progress_bar_profile).setOnClickListener(this);
        mIvProfile.setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        mIsVoted = false;
        mIsUpdateRequired = true;
        mRecyclerViewPost.setNestedScrollingEnabled(false);

        //for progressbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mProgressBarProfile.getThumb().mutate().setAlpha(0);
        }

        //for graph
        Viewport viewport = mGraphView.getViewport();
        viewport.setMinX(0);
        viewport.setMaxX(30);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setScrollable(true);

//        staticLabelsFormatter.setHorizontalLabels(new String[] {"old", "middle", "new"});

        GridLabelRenderer gridLabelRenderer = mGraphView.getGridLabelRenderer();
        gridLabelRenderer.setTextSize(18);
        gridLabelRenderer.setHorizontalLabelsColor(getResources().getColor(R.color.light_black_txt_color));
        gridLabelRenderer.setVerticalLabelsColor(getResources().getColor(R.color.light_black_txt_color));
        gridLabelRenderer.setGridColor(getResources().getColor(R.color.darkGreyBackground));
//        gridLabelRenderer.setNumHorizontalLabels(4); // only 5 because of the space

        gridLabelRenderer.setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, true);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, false) + "%";
                }
            }
        });

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 20),
                new DataPoint(5, 40),
                new DataPoint(10, 70),
                new DataPoint(15, 20),
                new DataPoint(20, 30),
                new DataPoint(25, 90),
                new DataPoint(30, 40)
        });
        mGraphView.addSeries(series);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE:
                if (status) {
                    GetMirrorDetailsResponseModel mirrorDetailsResponseModel = (GetMirrorDetailsResponseModel) serviceResponse;
                    if (mirrorDetailsResponseModel != null && mirrorDetailsResponseModel.status) {
                        LoggerUtil.d(TAG, mirrorDetailsResponseModel.statusCode);

                        if (mirrorDetailsResponseModel.Data != null && mirrorDetailsResponseModel.Data.mirrorData != null) {

                            GetMirrorDetailsResponseModel.MirrorData mirrorData = mirrorDetailsResponseModel.Data.mirrorData;
                            setMirrorDetailsData(mirrorData);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                if (mIsUpdateRequired)
                    getData(IApiEvent.REQUEST_GET_MIRROR_GRAPH_DATA_CODE);

                break;

            case IApiEvent.REQUEST_GET_MIRROR_GRAPH_DATA_CODE:
                if (status) {
                    GetMirrorGraphResponseModel mirrorGraphResponseModel = (GetMirrorGraphResponseModel) serviceResponse;
                    if (mirrorGraphResponseModel != null && mirrorGraphResponseModel.status) {
                        LoggerUtil.d(TAG, mirrorGraphResponseModel.statusCode);

                        if (mirrorGraphResponseModel.Data != null && mirrorGraphResponseModel.Data.graphData != null) {
                            setMirrorGraphData(mirrorGraphResponseModel.Data.graphData);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                break;

            case IApiEvent.REQUEST_GET_POSTS_CODE:
                if (status) {
                    GetPostsResponseModel postsResponseModel = (GetPostsResponseModel) serviceResponse;
                    if (postsResponseModel != null && postsResponseModel.status) {
                        LoggerUtil.d(TAG, postsResponseModel.statusCode);

                        if (postsResponseModel.Data != null && postsResponseModel.Data.postList != null) {
                            mTvDataNotAvailable.setVisibility(View.GONE);
                            mRecyclerViewPost.setVisibility(View.VISIBLE);

                            mPostList = postsResponseModel.Data.postList;
                            mRecyclerViewPost.setLayoutManager(new LinearLayoutManager(this));
                            mRecyclerViewPost.setAdapter(new RecentPostAdapter(this, mPostList));
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

            case IApiEvent.REQUEST_POST_LIKE_CODE:
                if (status) {
                    PostLikeResponseModel postLikeResponseModel = (PostLikeResponseModel) serviceResponse;
                    if (postLikeResponseModel != null && postLikeResponseModel.status) {
                        LoggerUtil.d(TAG, postLikeResponseModel.statusCode);

                        if (postLikeResponseModel.Data != null && postLikeResponseModel.Data.likeData != null) {

                            int position = 0;
                            for (GetPostsResponseModel.GetPostsDetails postsDetails : mPostList) {
                                if (postsDetails.postID == postLikeResponseModel.Data.likeData.postID) {

                                    postsDetails.isLike = postLikeResponseModel.Data.likeData.isLike;
                                    postsDetails.isUnLike = postLikeResponseModel.Data.likeData.isUnLike;
                                    postsDetails.likeCount = postLikeResponseModel.Data.likeData.likeCount;
                                    postsDetails.unlikeCount = postLikeResponseModel.Data.likeData.unlikeCount;

                                    position = mPostList.indexOf(postsDetails);
                                    break;
                                }
                            }
                            RecentPostAdapter homePostsAdapter = (RecentPostAdapter) mRecyclerViewPost.getAdapter();
                            homePostsAdapter.setPostList(mPostList);
                            homePostsAdapter.notifyItemChanged(position);
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_REMOVE_POST_CODE:
                if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                        int index = 0;
                        for (GetPostsResponseModel.GetPostsDetails postsDetails : mPostList) {
                            if (mPostId == postsDetails.postID) {
                                index = mPostList.indexOf(postsDetails);
                                break;
                            }
                        }
                        mPostList.remove(index);
                        RecentPostAdapter recentPostAdapter = (RecentPostAdapter) mRecyclerViewPost.getAdapter();
                        recentPostAdapter.notifyItemRemoved(index);

                        Snackbar.make(mRootView, getString(R.string.post_remove_successfully), Snackbar.LENGTH_LONG).show();

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:
                if (status) {
                    AddAndUpdateCommentResponseModel addAndUpdateCommentResponseModel = (AddAndUpdateCommentResponseModel) serviceResponse;
                    if (addAndUpdateCommentResponseModel != null && addAndUpdateCommentResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdateCommentResponseModel.statusCode);

                        if (addAndUpdateCommentResponseModel.Data != null && addAndUpdateCommentResponseModel.Data.commentData != null) {
                            Snackbar.make(mRootView, getString(R.string.add_comment_successfully), Snackbar.LENGTH_LONG).show();
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

        JsonObject jsonObject;
        String request;

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(MirrorDetailsActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE:

                String mirrorDetailsUrl = IWebServices.REQUEST_GET_MIRROR_DETAILS_URL + Constants.PARAM_USER_ID + "=" + userId
                        + "&" + Constants.PARAM_MIRROR_ID + "=" + String.valueOf(mMirrorId);
                RequestManager.addRequest(new GsonObjectRequest<GetMirrorDetailsResponseModel>(mirrorDetailsUrl, NetworkUtil.getHeaders(this),
                        null, GetMirrorDetailsResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetMirrorDetailsResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_GET_MIRROR_GRAPH_DATA_CODE:

                String mirrorGraphDataUrl = IWebServices.REQUEST_GET_MIRROR_GRAPH_DATA_URL + Constants.PARAM_USER_ID + "=" + userId
                        + "&" + Constants.PARAM_MIRROR_ID + "=" + String.valueOf(mMirrorId)
                        + "&" + Constants.PARAM_MONTH + "=" + String.valueOf("1")
                        + "&" + Constants.PARAM_YEAR + "=" + String.valueOf("2018");
                RequestManager.addRequest(new GsonObjectRequest<GetMirrorGraphResponseModel>(mirrorGraphDataUrl, NetworkUtil.getHeaders(this),
                        null, GetMirrorGraphResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetMirrorGraphResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_GET_POSTS_CODE:

                // page number should be 1 for recent post only.
                String getPostsUrl = IWebServices.REQUEST_GET_POSTS_URL + Constants.PARAM_USER_ID + "=" + userId
                        + "&" + Constants.PARAM_MIRROR_ID + "=" + String.valueOf(mMirrorId)
                        + "&" + Constants.PARAM_PAGE_NUMBER + "=" + 1;
                RequestManager.addRequest(new GsonObjectRequest<GetPostsResponseModel>(getPostsUrl, NetworkUtil.getHeaders(this),
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.view_create_a_new_post:

                Intent intentCreatePost = new Intent(MirrorDetailsActivity.this, CreatePostActivity.class);
                intentCreatePost.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                startActivity(intentCreatePost);
                break;

            case R.id.iv_profile:

                Intent intent = new Intent(MirrorDetailsActivity.this, ExitPollScreenActivity.class);
                intent.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                startActivity(intent);
                break;

            case R.id.view_progress_bar_profile:
                if (!mIsVoted) {
                    DialogFragment votingDialogFragment = MirrorVotingDialogFragment.newInstance(mMirrorId);
                    votingDialogFragment.show(getSupportFragmentManager(), "MirrorVotingDialogFragment");
                } else {
                    DialogFragment unVotingDialogFragment = MirrorUnVotingDialogFragment.newInstance(mMirrorId);
                    unVotingDialogFragment.show(getSupportFragmentManager(), "MirrorUnVotingDialogFragment");
                }
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }


    /**
     * Method is used to set mirror details data.
     *
     * @param mirrorData mirrorData
     */
    private void setMirrorDetailsData(GetMirrorDetailsResponseModel.MirrorData mirrorData) {

        ArrayList<ProgressItem> progressItemList = new ArrayList<>();

        progressItemList.add(new ProgressItem(getResources().getColor(R.color.txt_color_green), mirrorData.mirrorAdmirePer));
        progressItemList.add(new ProgressItem(getResources().getColor(R.color.light_red_bg), mirrorData.mirrorHatePer));
        progressItemList.add(new ProgressItem(getResources().getColor(R.color.bootstrap_brand_warning), mirrorData.mirrorCantSayPer));

        mProgressBarProfile.initData(progressItemList);
        mProgressBarProfile.invalidate();

        mTvName.setText(mirrorData.mirrorName != null ? mirrorData.mirrorName : "");
        if (mirrorData.imageURL != null && !mirrorData.imageURL.equals("")) {

            Picasso.with(this)
                    .load(mirrorData.imageURL != null ? mirrorData.imageURL : "")
                    .into(mIvProfile);
        }

        mIsVoted = mirrorData.mirrorAdmire || mirrorData.mirrorHate || mirrorData.mirrorCantSay;
    }

    /**
     * Method is used to set mirror graph data.
     *
     * @param graphData graphData
     */
    private void setMirrorGraphData(ArrayList<GetMirrorGraphResponseModel.GetMirrorGraphDetails> graphData) {

        //TODO change graph data
//        if(graphData!=null&&graphData.size()>0){
//            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
//            for( GetMirrorGraphResponseModel.GetMirrorGraphDetails graphDetails : graphData){
//                series
//            }
//        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 20),
                new DataPoint(5, 40),
                new DataPoint(10, 60),
                new DataPoint(15, 20),
                new DataPoint(20, 50),
                new DataPoint(25, 90),
                new DataPoint(30, 40)
        });
        mGraphView.addSeries(series);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData(IApiEvent.REQUEST_GET_POSTS_CODE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mPostList != null) {
            mPostList.clear();
        }
    }

    @Override
    public void onCommentIconClick(int position) {

//        Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
        CommentsDialogFragment commentsDialogFragment = CommentsDialogFragment.newInstance(mPostList.get(position));
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
                        intent = new Intent(MirrorDetailsActivity.this, CreatePostActivity.class);
                        intent.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                        intent.putExtra(Constants.POST_DETAILS_KEY, mPostList.get(position));
                        startActivity(intent);
                        return true;

                    case Constants.REMOVE_POST:
                        mPostId = mPostList.get(position).postID;
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
        mPostId = mPostList.get(position).postID;
        getData(IApiEvent.REQUEST_ADD_COMMENT_CODE);
    }

    @Override
    public void onLikeOrDislikeClick(int position, boolean isLike, boolean isUnLike) {
        mPostId = mPostList.get(position).postID;
        mIsLike = isLike;
        mIsUnLike = isUnLike;

        getData(IApiEvent.REQUEST_POST_LIKE_CODE);
    }

    @Override
    public void onPostLikeOrDislikeClick(GetPostsResponseModel.GetPostsDetails postDetails) {
        int position = 0;
        for (GetPostsResponseModel.GetPostsDetails tempPostDetails : mPostList) {
            if (tempPostDetails.postID == postDetails.postID) {

                tempPostDetails.isUnLike = postDetails.isUnLike;
                tempPostDetails.isLike = postDetails.isLike;
                tempPostDetails.likeCount = postDetails.likeCount;
                tempPostDetails.unlikeCount = postDetails.unlikeCount;

                position = mPostList.indexOf(tempPostDetails);
                break;
            }
        }

        RecentPostAdapter recentPostAdapter = (RecentPostAdapter) mRecyclerViewPost.getAdapter();
        recentPostAdapter.setPostList(mPostList);
        recentPostAdapter.notifyItemChanged(position);
    }

    @Override
    public void onVotingOrUnVotingClick() {
        mIsUpdateRequired = false;
        getData(IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE);
    }
}
