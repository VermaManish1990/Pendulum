package com.pend.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.GetPostsResponseModel;
import com.pend.util.LoggerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePostsAdapter extends RecyclerView.Adapter<HomePostsAdapter.ViewHolder> {

    private final String TAG = HomePostsAdapter.class.getSimpleName();
    private ArrayList<GetPostsResponseModel.GetPostsDetails> mPostsDetailsList;
    private Context mContext;

    public HomePostsAdapter(Context context, ArrayList<GetPostsResponseModel.GetPostsDetails> postsDetailsList) {
        mContext = context;
        mPostsDetailsList = postsDetailsList;
    }

    public void setPostsDetailsList(ArrayList<GetPostsResponseModel.GetPostsDetails> postsDetailsList) {
        this.mPostsDetailsList = postsDetailsList;
    }

    @NonNull
    @Override
    public HomePostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_posts_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostsAdapter.ViewHolder holder, int position) {
        GetPostsResponseModel.GetPostsDetails postsDetails = mPostsDetailsList.get(position);

        holder.tvComment.setText(String.valueOf(postsDetails.commentCount));
        holder.tvLike.setText(String.valueOf(postsDetails.likeCount));
        holder.tvDislike.setText(String.valueOf(postsDetails.unlikeCount));

        holder.tvTitle.setText(postsDetails.postInfo != null ? postsDetails.postInfo : "");
        holder.tvName.setText(postsDetails.userFullName != null ? postsDetails.userFullName : "");
        holder.tvTime.setText(postsDetails.createdDatetime != null ? postsDetails.createdDatetime : "");

        if (postsDetails.imageURL != null && !postsDetails.imageURL.equals("")) {
            Picasso.with(mContext)
                    .load(postsDetails.imageURL)
                    .into(holder.ivPost);
        }

        if (postsDetails.commentUserImageURL != null && !postsDetails.commentUserImageURL.equals("")) {
            Picasso.with(mContext)
                    .load(postsDetails.commentUserImageURL)
                    .into(holder.ivProfile);
        }
    }

    @Override
    public int getItemCount() {
        return mPostsDetailsList != null ? mPostsDetailsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProfile;
        private final ImageView ivPost;
        private final TextView tvName;
        private final TextView tvTime;
        private final TextView tvShareOnFacebook;
        private final TextView tvTitle;
        private final TextView tvComment;
        private final TextView tvLike;
        private final TextView tvDislike;
        private final ImageView ivComment;
        private final ImageView ivLike;
        private final ImageView ivDislike;
        private final View llComment;
        private final View llLike;
        private final View llDislike;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_profile);
            ivPost = itemView.findViewById(R.id.iv_post);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvShareOnFacebook = itemView.findViewById(R.id.tv_share_on_facebook);
            tvTitle = itemView.findViewById(R.id.tv_title);

            tvComment = itemView.findViewById(R.id.tv_comment);
            tvLike = itemView.findViewById(R.id.tv_like);
            tvDislike = itemView.findViewById(R.id.tv_dislike);

            ivComment = itemView.findViewById(R.id.iv_comment);
            ivLike = itemView.findViewById(R.id.iv_like);
            ivDislike = itemView.findViewById(R.id.iv_dislike);

            llComment = itemView.findViewById(R.id.ll_comment);
            llLike = itemView.findViewById(R.id.ll_like);
            llDislike = itemView.findViewById(R.id.ll_dislike);
        }
    }
}
