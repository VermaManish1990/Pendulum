package com.pend.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.GetPostsResponseModel;
import com.pend.util.LoggerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecentPostAdapter extends RecyclerView.Adapter<RecentPostAdapter.ViewHolder> {

    private final String TAG = RecentPostAdapter.class.getSimpleName();
    private Context mContext;
    private IRecentPostAdapterCallBack mIRecentPostAdapterCallBack;
    private ArrayList<GetPostsResponseModel.GetPostsDetails> mPostList;

    public RecentPostAdapter(Context context, ArrayList<GetPostsResponseModel.GetPostsDetails> postList) {
        mContext = context;
        mPostList = postList;
        mIRecentPostAdapterCallBack = (IRecentPostAdapterCallBack) context;
    }

    @NonNull
    @Override
    public RecentPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recent_post_item, parent, false);
        return new RecentPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentPostAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        GetPostsResponseModel.GetPostsDetails postsDetails = mPostList.get(position);

        holder.tvCommentCount.setText(String.valueOf(postsDetails.commentCount));
        holder.tvLikeCount.setText(String.valueOf(postsDetails.likeCount));
        holder.tvDislikeCount.setText(String.valueOf(postsDetails.unlikeCount));

        holder.tvDescription.setText(postsDetails.postInfo != null ? postsDetails.postInfo : "");
        holder.tvTime.setText(postsDetails.createdDatetime != null ? postsDetails.createdDatetime : "");

        holder.tvName.setText(postsDetails.commentUserFullName != null ? postsDetails.commentUserFullName : "");
        holder.tvComment.setText(postsDetails.commentText != null ? postsDetails.commentText : "");

        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIRecentPostAdapterCallBack.onCommentIconClick(position);
            }
        });

        if (postsDetails.imageURL != null && !postsDetails.imageURL.equals("")) {

            Picasso.with(mContext)
                    .load(postsDetails.imageURL)
                    .into(holder.ivPost);
        }

        if (postsDetails.commentUserImageURL != null && !postsDetails.commentUserImageURL.equals("")) {

            Picasso.with(mContext)
                    .load(postsDetails.commentUserImageURL)
                    .into(holder.ivCommentUserProfile);
        }

//        holder.cbAnonymous.setChecked();
    }

    @Override
    public int getItemCount() {
        return mPostList != null ? mPostList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private final ImageView ivPost;
        private final TextView tvDescription;
        private final TextView tvTime;
        private final TextView tvCommentCount;
        private final TextView tvLikeCount;
        private final TextView tvDislikeCount;
        private final EditText etAddAComment;
        private final CheckBox cbAnonymous;

        private final ImageView ivCommentUserProfile;
        private final TextView tvName;
        private final TextView tvComment;
        private final ImageView ivComment;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            ivPost = itemView.findViewById(R.id.iv_post);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivComment = itemView.findViewById(R.id.iv_comment);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvDislikeCount = itemView.findViewById(R.id.tv_dislike_count);
            etAddAComment = itemView.findViewById(R.id.et_add_a_comment);
            cbAnonymous = itemView.findViewById(R.id.cb_anonymous);

            ivCommentUserProfile = itemView.findViewById(R.id.iv_comment_user_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }

    public interface IRecentPostAdapterCallBack {
        void onCommentIconClick(int position);
    }
}
