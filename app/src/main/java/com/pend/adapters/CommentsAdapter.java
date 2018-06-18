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
import com.pend.models.GetPostCommentsResponseModel;
import com.pend.util.LoggerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private final String TAG = CommentsAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails> mCommentList;

    public CommentsAdapter(Context context, ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails> commentList) {
        mContext = context;
        mCommentList = commentList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comments_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {

        GetPostCommentsResponseModel.GetPostCommentsDetails postCommentsDetails = mCommentList.get(position);

        holder.tvName.setText(postCommentsDetails.userFullName != null ? postCommentsDetails.userFullName : "");
        holder.tvComment.setText(postCommentsDetails.commentText != null ? postCommentsDetails.commentText : "");

        if (postCommentsDetails.commentUserImageURL != null && !postCommentsDetails.commentUserImageURL.equals("")) {

            Picasso.with(mContext)
                    .load(postCommentsDetails.commentUserImageURL != null ? postCommentsDetails.commentUserImageURL : "")
                    .into(holder.ivProfile);
        }
    }

    @Override
    public int getItemCount() {
        return mCommentList != null ? mCommentList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private final ImageView ivProfile;
        private final TextView tvName;
        private final TextView tvComment;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }
}
