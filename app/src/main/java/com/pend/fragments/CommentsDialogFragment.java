package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.adapters.CommentsAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.AddAndUpdateCommentResponseModel;
import com.pend.models.GetPostCommentsResponseModel;
import com.pend.models.GetPostsResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.ui.IScreen;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsDialogFragment extends DialogFragment implements IScreen {

    private static final String ARG_COMMENT_LIST = "ARG_COMMENT_LIST";
    private static final String ARG_POST_DETAILS = "ARG_POST_DETAILS";
    private static final String TAG = CommentsDialogFragment.class.getSimpleName();

    private GetPostsResponseModel.GetPostsDetails mPostDetails;
    private ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails> mCommentList;
    private Context mContext;
    private RecyclerView mRecyclerViewComment;

    private ImageView mIvComment;
    private ImageView mIvLike;
    private ImageView mIvDislike;
    private ImageView mIvPost;

    private TextView mTvDescription;
    private TextView mTvTime;
    private TextView mTvComment;
    private TextView mTvLike;
    private TextView mTvDislike;
    private int mPageNumber;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;
    private int mCommentId;

    public static CommentsDialogFragment newInstance(GetPostsResponseModel.GetPostsDetails postsDetails) {
        CommentsDialogFragment fragment = new CommentsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POST_DETAILS, postsDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPostDetails = (GetPostsResponseModel.GetPostsDetails) getArguments().getSerializable(ARG_POST_DETAILS);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_comments, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        initUI(view);

        if (mPostDetails != null) {
            setInitialData();
            getData(IApiEvent.REQUEST_GET_POST_COMMENT_CODE);
        }

        return view;
    }

    private void initUI(View view) {
        mIvPost = view.findViewById(R.id.iv_post);
        mTvDescription = view.findViewById(R.id.tv_description);
        mTvTime = view.findViewById(R.id.tv_time);

        mIvComment = view.findViewById(R.id.iv_comment);
        mIvLike = view.findViewById(R.id.iv_like);
        mIvDislike = view.findViewById(R.id.iv_dislike);

        mTvComment = view.findViewById(R.id.tv_comment);
        mTvLike = view.findViewById(R.id.tv_like);
        mTvDislike = view.findViewById(R.id.tv_dislike);

        mRecyclerViewComment = view.findViewById(R.id.recycler_view_comment);
    }

    private void setInitialData() {

        mPageNumber = 1;
        mIsHasNextPage = false;
        mIsLoading = false;
        mCommentList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerViewComment.setLayoutManager(linearLayoutManager);

        mRecyclerViewComment.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_GET_POST_COMMENT_CODE);
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
        mRecyclerViewComment.setAdapter(new CommentsAdapter(mContext, mCommentList));

        setPostDetails();
    }

    private void setPostDetails() {
        mTvDescription.setText(mPostDetails.postInfo != null ? mPostDetails.postInfo : "");
        mTvTime.setText(mPostDetails.createdDatetime != null ? mPostDetails.createdDatetime : "");

        mTvComment.setText(String.valueOf(mPostDetails.commentCount));
        mTvLike.setText(String.valueOf(mPostDetails.likeCount));
        mTvDislike.setText(String.valueOf(mPostDetails.unlikeCount));

        if (mPostDetails.imageURL != null && !mPostDetails.imageURL.equals("")) {

            Picasso.with(mContext)
                    .load(mPostDetails.imageURL)
                    .into(mIvPost);
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_POST_COMMENT_CODE:
                if (status) {
                    GetPostCommentsResponseModel commentsResponseModel = (GetPostCommentsResponseModel) serviceResponse;
                    if (commentsResponseModel != null && commentsResponseModel.status) {
                        LoggerUtil.d(TAG, commentsResponseModel.statusCode);

                        if (commentsResponseModel.Data != null && commentsResponseModel.Data.commentList != null) {

                            mIsHasNextPage = !commentsResponseModel.Data.hasNextPage;

                            CommentsAdapter commentsAdapter = (CommentsAdapter) mRecyclerViewComment.getAdapter();
                            mCommentList.addAll(commentsResponseModel.Data.commentList);
                            commentsAdapter.setCommentList(mCommentList);
                            commentsAdapter.notifyDataSetChanged();
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                mIsLoading = false;
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:
                if (status) {
                    AddAndUpdateCommentResponseModel addAndUpdateCommentResponseModel = (AddAndUpdateCommentResponseModel) serviceResponse;
                    if (addAndUpdateCommentResponseModel != null && addAndUpdateCommentResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdateCommentResponseModel.statusCode);

                        if (addAndUpdateCommentResponseModel.Data != null && addAndUpdateCommentResponseModel.Data.commentData != null) {

                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_UPDATE_COMMENT_CODE:
                if (status) {
                    AddAndUpdateCommentResponseModel addAndUpdateCommentResponseModel = (AddAndUpdateCommentResponseModel) serviceResponse;
                    if (addAndUpdateCommentResponseModel != null && addAndUpdateCommentResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdateCommentResponseModel.statusCode);

                        if (addAndUpdateCommentResponseModel.Data != null && addAndUpdateCommentResponseModel.Data.commentData != null) {

                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_REMOVE_COMMENT_CODE:
                if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

//                        Snackbar.make(mRootView, R.string.post_remove_successfully, Snackbar.LENGTH_LONG).show();

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
        ((BaseActivity) mContext).removeProgressDialog();
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!ConnectivityUtils.isNetworkEnabled(mContext)) {
//            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }

        ((BaseActivity) mContext).showProgressDialog();

        JsonObject jsonObject;
        String request;

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_GET_POST_COMMENT_CODE:

                String postCommentUrl = IWebServices.REQUEST_GET_POST_COMMENT_URL + Constants.PARAM_POST_ID + "=" + mPostDetails.postID
                        + "&" + Constants.PARAM_PAGE_NUMBER + "=" + mPageNumber;
                RequestManager.addRequest(new GsonObjectRequest<GetPostCommentsResponseModel>(postCommentUrl, NetworkUtil.getHeaders(mContext),
                        null, GetPostCommentsResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetPostCommentsResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:

                //Todo commentText
                jsonObject = RequestPostDataUtil.addCommentApiRegParam(userId,mPostDetails.postID,"");
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<AddAndUpdateCommentResponseModel>(IWebServices.REQUEST_ADD_COMMENT_URL, NetworkUtil.getHeaders(mContext),
                        request, AddAndUpdateCommentResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(AddAndUpdateCommentResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_UPDATE_COMMENT_CODE:

                //Todo commentText
                jsonObject = RequestPostDataUtil.updateCommentApiRegParam(userId,mPostDetails.postID,mCommentId,"");
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<AddAndUpdateCommentResponseModel>(IWebServices.REQUEST_UPDATE_COMMENT_URL, NetworkUtil.getHeaders(mContext),
                        request, AddAndUpdateCommentResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(AddAndUpdateCommentResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_REMOVE_COMMENT_CODE:

                jsonObject = RequestPostDataUtil.removeCommentApiRegParam(userId,mPostDetails.postID,mCommentId);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<BaseResponseModel>(IWebServices.REQUEST_REMOVE_COMMENT_URL, NetworkUtil.getHeaders(mContext),
                        request, BaseResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(BaseResponseModel response) {
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
}
