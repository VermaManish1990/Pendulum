package com.pend.activity.mirror;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.RecentPostAdapter;
import com.pend.fragments.CommentsDialogFragment;
import com.pend.fragments.CreateMirrorDialogFragment;
import com.pend.fragments.UnVotingDialogFragment;
import com.pend.fragments.VotingDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetMirrorDetailsResponseModel;
import com.pend.models.GetMirrorGraphResponseModel;
import com.pend.models.GetPostCommentsResponseModel;
import com.pend.models.GetPostsResponseModel;
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

public class MirrorDetailsActivity extends BaseActivity implements View.OnClickListener, RecentPostAdapter.IRecentPostAdapterCallBack {

    private static final String TAG = MirrorDetailsActivity.class.getSimpleName();
    private View mRootView;
    private ImageView mIvProfile;
    private GraphView mGraphView;
    private TextView mTvName;
    private CustomProgressBar mProgressBarProfile;
    private RecyclerView mRecyclerViewPost;
    private int mMirrorId;
    private boolean isVoted = false;
    private int mPageNumber;
    private ArrayList<GetPostsResponseModel.GetPostsDetails> mPostList;
    private GetPostsResponseModel.GetPostsDetails mPostsDetails;
    private int mPostId;
    private TextView mTvDataNotAvailable;

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

        findViewById(R.id.iv_create_post).setOnClickListener(this);
        mIvProfile.setOnClickListener(this);
        mProgressBarProfile.setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {
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
        gridLabelRenderer.setTextSize(12);
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

                getData(IApiEvent.REQUEST_GET_POSTS_CODE);
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

            case IApiEvent.REQUEST_GET_POST_COMMENT_CODE:
                if (status) {
                    GetPostCommentsResponseModel commentsResponseModel = (GetPostCommentsResponseModel) serviceResponse;
                    if (commentsResponseModel != null && commentsResponseModel.status) {
                        LoggerUtil.d(TAG, commentsResponseModel.statusCode);

                        if (commentsResponseModel.Data != null && commentsResponseModel.Data.commentList != null) {
                            CommentsDialogFragment commentsDialogFragment = CommentsDialogFragment.newInstance(commentsResponseModel.Data.commentList, mPostsDetails);
                            commentsDialogFragment.show(getSupportFragmentManager(), "CommentsDialogFragment");
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
            case IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE:

                String mirrorDetailsUrl = IWebServices.REQUEST_GET_MIRROR_DETAILS_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
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

                String mirrorGraphDataUrl = IWebServices.REQUEST_GET_MIRROR_GRAPH_DATA_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
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
                String getPostsUrl = IWebServices.REQUEST_GET_POSTS_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
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

            case IApiEvent.REQUEST_GET_POST_COMMENT_CODE:

                //TODO add pagination.
                mPageNumber = 1;
                String postCommentUrl = IWebServices.REQUEST_GET_POST_COMMENT_URL + Constants.PARAM_POST_ID + "=" + mPostId
                        + "&" + Constants.PARAM_PAGE_NUMBER + "=" + mPageNumber;
                RequestManager.addRequest(new GsonObjectRequest<GetPostCommentsResponseModel>(postCommentUrl, NetworkUtil.getHeaders(this),
                        null, GetPostCommentsResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetPostCommentsResponseModel response) {
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

            case R.id.iv_create_post:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.iv_profile:

                Intent intent = new Intent(MirrorDetailsActivity.this, ExitPollScreenActivity.class);
                intent.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                startActivity(intent);
                break;

            case R.id.progress_bar_profile:
                if (isVoted) {
                    DialogFragment votingDialogFragment = new VotingDialogFragment();
                    votingDialogFragment.show(getSupportFragmentManager(), "VotingDialogFragment");
                } else {
                    DialogFragment unVotingDialogFragment = new UnVotingDialogFragment();
                    unVotingDialogFragment.show(getSupportFragmentManager(), "UnVotingDialogFragment");
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
    }

    /**
     * Method is used to set mirror graph data.
     *
     * @param graphData graphData
     */
    private void setMirrorGraphData(ArrayList<GetMirrorGraphResponseModel.GetMirrorGraphDetails> graphData) {

        //TODO change graph data
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
    public void onCommentIconClick(int position) {

        Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();

//        mPostsDetails = mPostList.get(position);
//        mPostId = mPostsDetails.postID;
//        getData(IApiEvent.REQUEST_GET_POST_COMMENT_CODE);
    }
}
