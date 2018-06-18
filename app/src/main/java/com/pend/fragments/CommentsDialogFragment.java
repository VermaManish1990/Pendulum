package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.pend.R;
import com.pend.adapters.CommentsAdapter;
import com.pend.models.GetPostCommentsResponseModel;
import com.pend.models.GetPostsResponseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsDialogFragment extends DialogFragment {

    private static final String ARG_COMMENT_LIST = "ARG_COMMENT_LIST";
    private static final String ARG_POST_DETAILS = "ARG_POST_DETAILS";

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

    public static CommentsDialogFragment newInstance(ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails> commentList,
                                                     GetPostsResponseModel.GetPostsDetails postsDetails) {
        CommentsDialogFragment fragment = new CommentsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_COMMENT_LIST, commentList);
        args.putSerializable(ARG_POST_DETAILS, postsDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPostDetails = (GetPostsResponseModel.GetPostsDetails) getArguments().getSerializable(ARG_POST_DETAILS);
            mCommentList = (ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails>) getArguments().getSerializable(ARG_COMMENT_LIST);
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
        setInitialData();

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

        mRecyclerViewComment.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewComment.setAdapter(new CommentsAdapter(mContext, mCommentList));

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
}
