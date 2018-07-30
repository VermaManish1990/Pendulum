package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.pend.models.PostLikeResponseModel;
import com.pend.util.DateUtil;
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

public class CommentsDialogFragment extends DialogFragment implements IScreen, View.OnClickListener, CommentsAdapter.ICommentsAdapterCallBack {

    private static final String ARG_POST_DETAILS = "ARG_POST_DETAILS";
    private static final String TAG = CommentsDialogFragment.class.getSimpleName();

    private GetPostsResponseModel.GetPostsDetails mPostDetails;
    private ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails> mCommentList;
    private ICommentsDialogCallBack mICommentsDialogCallBack;
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
    private boolean mIsLike;
    private boolean mIsUnLike;
    private ImageView mIvSend;
    private EditText mEtAddAComment;
    private String mCommentText;
    private boolean mIsUpdateComment;

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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);

        if (getArguments() != null) {
            mPostDetails = (GetPostsResponseModel.GetPostsDetails) getArguments().getSerializable(ARG_POST_DETAILS);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mICommentsDialogCallBack = (ICommentsDialogCallBack) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_comments, container, false);

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

        mEtAddAComment = view.findViewById(R.id.et_add_a_comment);
        mTvComment = view.findViewById(R.id.tv_comment_count);
        mTvLike = view.findViewById(R.id.tv_like_count);
        mTvDislike = view.findViewById(R.id.tv_dislike_count);

        mRecyclerViewComment = view.findViewById(R.id.recycler_view_comment);

        view.findViewById(R.id.iv_send).setOnClickListener(this);
        mIvLike.setOnClickListener(this);
        mIvDislike.setOnClickListener(this);
    }

    private void setInitialData() {

        mPageNumber = 1;
        mIsHasNextPage = false;
        mIsLoading = false;
        mIsLike = false;
        mIsUnLike = false;
        mIsUpdateComment = false;
        mCommentList = new ArrayList<>();

        mEtAddAComment.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    mCommentText = mEtAddAComment.getText().toString().trim();
                    if (mCommentText.length() > 0) {
                        if (mIsUpdateComment) {
                            mIsUpdateComment = false;
                            getData(IApiEvent.REQUEST_UPDATE_COMMENT_CODE);
                        } else {
                            getData(IApiEvent.REQUEST_ADD_COMMENT_CODE);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

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
        mRecyclerViewComment.setAdapter(new CommentsAdapter(mContext, this, mCommentList));

        setPostDetails();
    }

    /**
     * Method is used to set Post Details Data.
     */
    private void setPostDetails() {
        mTvDescription.setText(mPostDetails.postInfo != null ? mPostDetails.postInfo : "");

        String time = "";
        if (mPostDetails.createdDatetime != null && !mPostDetails.createdDatetime.equals("")) {
            time = DateUtil.getDifferenceFromCurrentDate(mPostDetails.createdDatetime);
        }
        mTvTime.setText(time);

        mTvComment.setText(String.valueOf(mPostDetails.commentCount));
        mTvLike.setText(String.valueOf(mPostDetails.likeCount));
        mTvDislike.setText(String.valueOf(mPostDetails.unlikeCount));

        if (mPostDetails.isLike) {
            mIvLike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like_green));
        } else {
            mIvLike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like));
        }

        if (mPostDetails.isUnLike) {
            mIvDislike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dislike_red));
        } else {
            mIvDislike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dislike));
        }

        if (mPostDetails.imageURL != null && !mPostDetails.imageURL.equals("")) {

            mIvPost.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(mPostDetails.imageURL)
                    .into(mIvPost);
        }else {
            mIvPost.setVisibility(View.GONE);
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

            case IApiEvent.REQUEST_POST_LIKE_CODE:
                if (status) {
                    PostLikeResponseModel postLikeResponseModel = (PostLikeResponseModel) serviceResponse;
                    if (postLikeResponseModel != null && postLikeResponseModel.status) {
                        LoggerUtil.d(TAG, postLikeResponseModel.statusCode);

                        if (postLikeResponseModel.Data != null && postLikeResponseModel.Data.likeData != null) {

                            mPostDetails.isLike = postLikeResponseModel.Data.likeData.isLike;
                            mPostDetails.isUnLike = postLikeResponseModel.Data.likeData.isUnLike;
                            mPostDetails.likeCount = postLikeResponseModel.Data.likeData.likeCount;
                            mPostDetails.unlikeCount = postLikeResponseModel.Data.likeData.unlikeCount;

                            mICommentsDialogCallBack.onPostUpdate(mPostDetails);
                            setPostDetails();
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:
                if (status) {
                    mEtAddAComment.setText("");
                    AddAndUpdateCommentResponseModel addAndUpdateCommentResponseModel = (AddAndUpdateCommentResponseModel) serviceResponse;
                    if (addAndUpdateCommentResponseModel != null && addAndUpdateCommentResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdateCommentResponseModel.statusCode);

                        if (addAndUpdateCommentResponseModel.Data != null && addAndUpdateCommentResponseModel.Data.commentData != null) {

                            AddAndUpdateCommentResponseModel.AddAndUpdateCommentDetails commentDetails = addAndUpdateCommentResponseModel.Data.commentData;
                            CommentsAdapter commentsAdapter = (CommentsAdapter) mRecyclerViewComment.getAdapter();
                            mCommentList.add(new GetPostCommentsResponseModel.GetPostCommentsDetails(commentDetails.userID, commentDetails.commentID,
                                    commentDetails.commentText, commentDetails.userFullName, commentDetails.imageName, commentDetails.commentUserImageURL,
                                    commentDetails.createdDatetime));
                            commentsAdapter.setCommentList(mCommentList);
                            commentsAdapter.notifyDataSetChanged();

                            mPostDetails.commentText = commentDetails.commentText;
                            mPostDetails.commentUserImageURL = commentDetails.commentUserImageURL;
                            mPostDetails.commentUserFullName = commentDetails.userFullName;
                            mPostDetails.commentUserImageName = commentDetails.imageName;
                            mPostDetails.commentCount += 1;

                            mTvComment.setText(String.valueOf(mPostDetails.commentCount));
                            mICommentsDialogCallBack.onPostUpdate(mPostDetails);
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
                    mEtAddAComment.setText("");

                    AddAndUpdateCommentResponseModel addAndUpdateCommentResponseModel = (AddAndUpdateCommentResponseModel) serviceResponse;
                    if (addAndUpdateCommentResponseModel != null && addAndUpdateCommentResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdateCommentResponseModel.statusCode);

                        if (addAndUpdateCommentResponseModel.Data != null && addAndUpdateCommentResponseModel.Data.commentData != null) {

                            int position = 0;
                            for (GetPostCommentsResponseModel.GetPostCommentsDetails commentsDetails : mCommentList) {
                                if (commentsDetails.commentID == addAndUpdateCommentResponseModel.Data.commentData.commentID) {

                                    commentsDetails.commentText = addAndUpdateCommentResponseModel.Data.commentData.commentText;
                                    position = mCommentList.indexOf(commentsDetails);
                                    break;
                                }
                            }

                            CommentsAdapter commentsAdapter = (CommentsAdapter) mRecyclerViewComment.getAdapter();
                            commentsAdapter.notifyItemChanged(position);

                            int size = mCommentList.size();
                            if (position == size - 1) {

                                if (size > 0) {
                                    GetPostCommentsResponseModel.GetPostCommentsDetails commentDetails = mCommentList.get(position);

                                    mPostDetails.commentText = commentDetails.commentText;
                                    mICommentsDialogCallBack.onPostUpdate(mPostDetails);
                                }
                            }
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

                        int position = 0;
                        for (GetPostCommentsResponseModel.GetPostCommentsDetails commentsDetails : mCommentList) {
                            if (commentsDetails.commentID == mCommentId) {
                                position = mCommentList.indexOf(commentsDetails);
                                break;
                            }
                        }

                        int size = mCommentList.size();
                        if (position == size - 1) {

                            if (size > 1) {
                                GetPostCommentsResponseModel.GetPostCommentsDetails commentDetails = mCommentList.get(size - 2);

                                mPostDetails.commentText = commentDetails.commentText;
                                mPostDetails.commentUserImageURL = commentDetails.commentUserImageURL;
                                mPostDetails.commentUserFullName = commentDetails.userFullName;
                                mPostDetails.commentUserImageName = commentDetails.imageName;
                            } else {
                                mPostDetails.commentText = null;
                                mPostDetails.commentUserImageURL = null;
                                mPostDetails.commentUserFullName = null;
                                mPostDetails.commentUserImageName = null;
                            }
                        }

                        mPostDetails.commentCount -= 1;
                        mTvComment.setText(String.valueOf(mPostDetails.commentCount));
                        mICommentsDialogCallBack.onPostUpdate(mPostDetails);

                        mCommentList.remove(position);
                        CommentsAdapter commentsAdapter = (CommentsAdapter) mRecyclerViewComment.getAdapter();
                        commentsAdapter.setCommentList(mCommentList);
                        commentsAdapter.notifyDataSetChanged();

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
        ((BaseActivity) mContext).

                removeProgressDialog();

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

            case IApiEvent.REQUEST_POST_LIKE_CODE:

                jsonObject = RequestPostDataUtil.postLikeApiRegParam(userId, mPostDetails.postID, mIsLike, mIsUnLike);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<PostLikeResponseModel>(IWebServices.REQUEST_POST_LIKE_URL, NetworkUtil.getHeaders(mContext),
                        request, PostLikeResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(PostLikeResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:

                jsonObject = RequestPostDataUtil.addCommentApiRegParam(userId, mPostDetails.postID, mCommentText);
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

                jsonObject = RequestPostDataUtil.updateCommentApiRegParam(userId, mPostDetails.postID, mCommentId, mCommentText);
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

                jsonObject = RequestPostDataUtil.removeCommentApiRegParam(userId, mPostDetails.postID, mCommentId);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_like:
                if (mPostDetails.isLike) {
                    mIsLike = false;
                    mIsUnLike = false;
                } else {
                    mIsLike = true;
                    mIsUnLike = false;
                }
                getData(IApiEvent.REQUEST_POST_LIKE_CODE);
                break;

            case R.id.iv_dislike:
                if (mPostDetails.isUnLike) {
                    mIsLike = false;
                    mIsUnLike = false;
                } else {
                    mIsLike = false;
                    mIsUnLike = true;
                }
                getData(IApiEvent.REQUEST_POST_LIKE_CODE);
                break;

            case R.id.iv_send:
                mCommentText = mEtAddAComment.getText().toString().trim();
                if (mCommentText.length() > 0) {
                    if (mIsUpdateComment) {
                        mIsUpdateComment = false;
                        getData(IApiEvent.REQUEST_UPDATE_COMMENT_CODE);
                    } else {
                        getData(IApiEvent.REQUEST_ADD_COMMENT_CODE);
                    }
                }
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @Override
    public void onMenuClick(final int position, View view) {
        PopupMenu popup = new PopupMenu(mContext, view, Gravity.END);
        popup.getMenuInflater().inflate(R.menu.comment_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getTitle().toString()) {
                    case Constants.UPDATE_COMMENT:

                        mEtAddAComment.setText(String.valueOf(mCommentList.get(position).commentText));
                        mCommentId = mCommentList.get(position).commentID;
                        mIsUpdateComment = true;
                        return true;

                    case Constants.REMOVE_COMMENT:

                        mCommentId = mCommentList.get(position).commentID;
                        getData(IApiEvent.REQUEST_REMOVE_COMMENT_CODE);
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    public interface ICommentsDialogCallBack {
        void onPostUpdate(GetPostsResponseModel.GetPostsDetails postDetails);
    }

}
