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
import com.pend.util.SharedPrefUtils;
import com.pendulum.persistence.SharedPrefsUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private final String TAG = CommentsAdapter.class.getSimpleName();
    private Context mContext;
    private ICommentsAdapterCallBack mICommentsAdapterCallBack;
    private ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails> mCommentList;

    public CommentsAdapter(Context context, ICommentsAdapterCallBack iCommentsAdapterCallBack, ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails> commentList) {
        mContext = context;
        mICommentsAdapterCallBack = iCommentsAdapterCallBack;
        mCommentList = commentList;
    }

    public void setCommentList(ArrayList<GetPostCommentsResponseModel.GetPostCommentsDetails> commentList) {
        this.mCommentList = commentList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comments_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentsAdapter.ViewHolder holder, final int position) {

        final GetPostCommentsResponseModel.GetPostCommentsDetails postCommentsDetails = mCommentList.get(position);

        int userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        if (postCommentsDetails.userID == userId) {
            holder.ivMenu.setVisibility(View.VISIBLE);
        } else {
            holder.ivMenu.setVisibility(View.GONE);
        }

        holder.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mICommentsAdapterCallBack.onMenuClick(position, holder.ivMenu);
            }
        });

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mICommentsAdapterCallBack.onUserProfileClick(position, postCommentsDetails.userID);
            }
        });

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
        private final ImageView ivMenu;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvComment = itemView.findViewById(R.id.tv_comment);
            ivMenu = itemView.findViewById(R.id.iv_menu);
        }
    }

    public interface ICommentsAdapterCallBack {
        void onMenuClick(int position, View view);

        void onUserProfileClick(int position, int userId);
    }
}
