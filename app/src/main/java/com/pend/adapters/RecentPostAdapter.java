package com.pend.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.pend.R;
import com.pend.models.GetPostsResponseModel;
import com.pend.util.DateUtil;
import com.pend.util.LoggerUtil;
import com.pend.util.OtherUtil;
import com.pend.util.SharedPrefUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

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

    public void setPostList(ArrayList<GetPostsResponseModel.GetPostsDetails> postList) {
        this.mPostList = postList;
    }

    @NonNull
    @Override
    public RecentPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recent_post_item, parent, false);
        return new RecentPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecentPostAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final GetPostsResponseModel.GetPostsDetails postsDetails = mPostList.get(position);

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvCommentCount.setText(String.valueOf(postsDetails.commentCount));
        holder.tvLikeCount.setText(String.valueOf(postsDetails.likeCount));
        holder.tvDislikeCount.setText(String.valueOf(postsDetails.unlikeCount));

        holder.tvDescription.setText(postsDetails.postInfo != null ? postsDetails.postInfo : "");

        String time = "";
        if (postsDetails.createdDatetime != null && !postsDetails.createdDatetime.equals("")) {
            time = DateUtil.getDifferenceFromCurrentDate(postsDetails.createdDatetime);
        }
        holder.tvTime.setText(time);

        if (postsDetails.isLike) {
            holder.ivLike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like_green));
        } else {
            holder.ivLike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like));
        }

        if (postsDetails.isUnLike) {
            holder.ivDislike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dislike_red));
        } else {
            holder.ivDislike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dislike));
        }

        if (postsDetails.userImageNameURL != null && !postsDetails.userImageNameURL.equals("")) {
            Picasso.with(mContext)
                    .load(postsDetails.commentUserImageURL)
                    .into(holder.ivProfile);
        }

        if(SharedPrefUtils.getProfileImageUrl(mContext)!=null && !Objects.equals(SharedPrefUtils.getProfileImageUrl(mContext), "")){
            Picasso.with(mContext)
                    .load(SharedPrefUtils.getProfileImageUrl(mContext))
                    .into(holder.ivUser);
        }

        int finalUserId = userId;
        holder.tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIRecentPostAdapterCallBack.onUserProfileClick(position, postsDetails.userID);
            }
        });
        holder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIRecentPostAdapterCallBack.onUserProfileClick(position, postsDetails.userID);
            }
        });

        holder.tvUserName.setText(postsDetails.userFullName != null ? postsDetails.userFullName : "");
        holder.tvCreatedTime.setText(postsDetails.createdDatetime != null ? postsDetails.createdDatetime : "");

        holder.etAddAComment.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    String text = holder.etAddAComment.getText().toString().trim();
                    if (text.length() > 0) {

                        holder.etAddAComment.setText("");

                        if (finalUserId == -1) {
                            OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                        } else
                        mIRecentPostAdapterCallBack.onSendClick(position, text);
                    }
                    return true;
                }
                return false;
            }
        });

        holder.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = holder.etAddAComment.getText().toString().trim();
                if (text.length() > 0) {

                    holder.etAddAComment.setText("");

                    if (finalUserId == -1) {
                        OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                    } else
                    mIRecentPostAdapterCallBack.onSendClick(position, text);
                }
            }
        });

        if (userId == postsDetails.userID) {
            holder.ivMenu.setVisibility(View.VISIBLE);
            holder.ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIRecentPostAdapterCallBack.onMenuClick(position, holder.ivMenu);
                }
            });
        } else {
            holder.ivMenu.setVisibility(View.INVISIBLE);
        }

        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalUserId == -1) {
                    OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                } else
                mIRecentPostAdapterCallBack.onCommentIconClick(position);
            }
        });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalUserId == -1) {
                    OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                } else if (postsDetails.isLike) {
                    mIRecentPostAdapterCallBack.onLikeOrDislikeClick(position, false, false);
                } else {
                    mIRecentPostAdapterCallBack.onLikeOrDislikeClick(position, true, false);
                }
            }
        });
        holder.ivDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalUserId == -1) {
                    OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                } else if (postsDetails.isUnLike) {
                    mIRecentPostAdapterCallBack.onLikeOrDislikeClick(position, false, false);
                } else {
                    mIRecentPostAdapterCallBack.onLikeOrDislikeClick(position, false, true);
                }
            }
        });

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog((Activity) mContext);// initialize facebook shareDialog.

                if (ShareDialog.canShow(SharePhotoContent.class)) {

                    if (postsDetails.imageURL != null) {

                        try {

                            SharePhoto photo = new SharePhoto.Builder()
                                    .setBitmap(((BitmapDrawable) holder.ivPost.getDrawable()).getBitmap())
                                    .setImageUrl(Uri.parse(postsDetails.imageURL))
                                    .setCaption(postsDetails.postInfo)
                                    .build();

                            ShareContent content = new SharePhotoContent.Builder()
                                    .addPhoto(photo)
                                    .build();

                            shareDialog.show(content);  // Show facebook ShareDialog
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    OtherUtil.showAlertDialog(mContext.getString(R.string.facebook_error_message), mContext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        if (postsDetails.imageURL != null && !postsDetails.imageURL.equals("")) {

            holder.ivPost.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(postsDetails.imageURL)
                    .into(holder.ivPost);

            holder.ivPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIRecentPostAdapterCallBack.onCommentIconClick(position);
                }
            });
        } else {
            holder.ivPost.setVisibility(View.GONE);
        }

        if (postsDetails.commentText != null && !postsDetails.commentText.equals("")) {

            holder.rlComment.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.tvCommentUserName.setText(postsDetails.commentUserFullName != null ? postsDetails.commentUserFullName : "");
            holder.tvComment.setText(postsDetails.commentText != null ? postsDetails.commentText : "");
            if (postsDetails.commentUserImageURL != null && !postsDetails.commentUserImageURL.equals("")) {

                Picasso.with(mContext)
                        .load(postsDetails.commentUserImageURL)
                        .into(holder.ivCommentUserProfile);
            }
        } else {
            holder.rlComment.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }
//        holder.cbAnonymous.setChecked();
    }

    /**
     * Method is used to share a message with Social Media.
     *
     * @param message message
     */
    private void shareMessageWithSocialMedia(String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        mContext.startActivity(Intent.createChooser(sendIntent, "Share link"));
    }

    @Override
    public int getItemCount() {
        return mPostList != null ? mPostList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final View rlComment;

        private final TextView tvDescription;
        private final TextView tvTime;
        private final TextView tvCommentCount;
        private final TextView tvLikeCount;
        private final TextView tvDislikeCount;
        private final TextView tvCommentUserName;
        private final TextView tvComment;
        private final TextView tvUserName;
        private final TextView tvCreatedTime;

        private final EditText etAddAComment;
        private final CheckBox cbAnonymous;

        private final ImageView ivPost;
        private final ImageView ivCommentUserProfile;
        private final ImageView ivComment;
        private final ImageView ivLike;
        private final ImageView ivDislike;
        private final ImageView ivShare;
        private final ImageView ivSend;
        private final ImageView ivMenu;
        private final ImageView ivProfile;
        private final ImageView ivUser;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.view);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            ivUser = itemView.findViewById(R.id.iv_user);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvCreatedTime = itemView.findViewById(R.id.tv_created_time);
            rlComment = itemView.findViewById(R.id.rl_comment);
            ivPost = itemView.findViewById(R.id.iv_post);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivShare = itemView.findViewById(R.id.iv_share);

            ivComment = itemView.findViewById(R.id.iv_comment);
            ivLike = itemView.findViewById(R.id.iv_like);
            ivDislike = itemView.findViewById(R.id.iv_dislike);

            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            tvDislikeCount = itemView.findViewById(R.id.tv_dislike_count);

            etAddAComment = itemView.findViewById(R.id.et_add_a_comment);
            ivSend = itemView.findViewById(R.id.iv_send);
            ivMenu = itemView.findViewById(R.id.iv_menu);
            cbAnonymous = itemView.findViewById(R.id.cb_anonymous);

            ivCommentUserProfile = itemView.findViewById(R.id.iv_comment_user_profile);
            tvCommentUserName = itemView.findViewById(R.id.tv_comment_user_name);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }
    }

    public interface IRecentPostAdapterCallBack {
        void onCommentIconClick(int position);

        void onMenuClick(int position, View view);

        void onSendClick(int position, String commentText);

        void onUserProfileClick(int position, int userId);

        void onLikeOrDislikeClick(int position, boolean isLike, boolean isUnLike);
    }
}
