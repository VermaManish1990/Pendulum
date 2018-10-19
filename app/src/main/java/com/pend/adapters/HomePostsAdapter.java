package com.pend.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.pend.R;
import com.pend.models.GetPostsResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.OtherUtil;
import com.pend.util.SharedPrefUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class HomePostsAdapter extends RecyclerView.Adapter<HomePostsAdapter.ViewHolder> {

    private final String TAG = HomePostsAdapter.class.getSimpleName();
    private ArrayList<GetPostsResponseModel.GetPostsDetails> mPostsDetailsList;
    private Context mContext;
    private IHomePostsAdapterCallBack mIHomePostsAdapterCallBack;

    public HomePostsAdapter(Context context, ArrayList<GetPostsResponseModel.GetPostsDetails> postsDetailsList) {
        mContext = context;
        mIHomePostsAdapterCallBack = (IHomePostsAdapterCallBack) context;
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
    public void onBindViewHolder(@NonNull final HomePostsAdapter.ViewHolder holder, final int position) {
        final GetPostsResponseModel.GetPostsDetails postsDetails = mPostsDetailsList.get(position);

        holder.tvComment.setText(String.valueOf(postsDetails.commentCount));
        holder.tvLike.setText(String.valueOf(postsDetails.likeCount));
        holder.tvDislike.setText(String.valueOf(postsDetails.unlikeCount));

        if (postsDetails.postInfo != null && postsDetails.postInfo.length() > 0) {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(postsDetails.postInfo);
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }

        holder.tvName.setText(postsDetails.userFullName != null ? postsDetails.userFullName : "");
        holder.tvTime.setText(postsDetails.createdDatetime != null ? postsDetails.createdDatetime : "");
        holder.tvCreatedBy.setText(Html.fromHtml("<b>" + postsDetails.userFullName + "</b>"));

        if (postsDetails.imageURL != null && !postsDetails.imageURL.equals("")) {
            holder.ivPost.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(postsDetails.imageURL)
                    .into(holder.ivPost);

            holder.ivPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIHomePostsAdapterCallBack.onCommentClick(position);
                }
            });
        } else {
            holder.ivPost.setVisibility(View.GONE);
        }

        if (postsDetails.userImageNameURL != null && !postsDetails.userImageNameURL.equals("")) {
            Picasso.with(mContext)
                    .load(postsDetails.commentUserImageURL)
                    .into(holder.ivProfile);
        }

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIHomePostsAdapterCallBack.onUserProfileClick(position, postsDetails.userID);
            }
        });
        holder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIHomePostsAdapterCallBack.onUserProfileClick(position, postsDetails.userID);
            }
        });

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

        if (SharedPrefUtils.getProfileImageUrl(mContext) != null && !Objects.equals(SharedPrefUtils.getProfileImageUrl(mContext), "")) {
            Picasso.with(mContext)
                    .load(SharedPrefUtils.getProfileImageUrl(mContext))
                    .into(holder.ivUser);
        }

        holder.etAddAComment.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    String text = holder.etAddAComment.getText().toString().trim();
                    if (text.length() > 0) {

                        holder.etAddAComment.setText("");
                        mIHomePostsAdapterCallBack.onSendClick(position, text);
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
                    mIHomePostsAdapterCallBack.onSendClick(position, text);
                }
            }
        });

        int userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        if (userId == postsDetails.userID) {
            holder.ivMenu.setVisibility(View.VISIBLE);
            holder.ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIHomePostsAdapterCallBack.onMenuClick(position, holder.ivMenu);
                }
            });
        } else {
            holder.ivMenu.setVisibility(View.INVISIBLE);
        }

        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIHomePostsAdapterCallBack.onCommentClick(position);
            }
        });

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postsDetails.isLike) {
                    mIHomePostsAdapterCallBack.onLikeOrDislikeClick(position, false, false);
                } else {
                    mIHomePostsAdapterCallBack.onLikeOrDislikeClick(position, true, false);
                }
            }
        });
        holder.llDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postsDetails.isUnLike) {
                    mIHomePostsAdapterCallBack.onLikeOrDislikeClick(position, false, false);
                } else {
                    mIHomePostsAdapterCallBack.onLikeOrDislikeClick(position, false, true);
                }
            }
        });
        holder.ivShareOnFacebook.setOnClickListener(new View.OnClickListener() {
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
                    }else {
                        OtherUtil.showAlertDialog("You can not share post on facebook without image.", mContext, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
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
    }

    @Override
    public int getItemCount() {
        return mPostsDetailsList != null ? mPostsDetailsList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProfile;
        private final ImageView ivUser;
        private final ImageView ivPost;
        private final TextView tvName;
        private final TextView tvTime;
        private final ImageView ivShareOnFacebook;
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
        private final ImageView ivMenu;
        private final ImageView ivSend;
        private final EditText etAddAComment;
        private final TextView tvCreatedBy;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_profile);
            ivUser = itemView.findViewById(R.id.iv_user);
            ivPost = itemView.findViewById(R.id.iv_post);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivShareOnFacebook = itemView.findViewById(R.id.iv_share_on_facebook);
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

            ivMenu = itemView.findViewById(R.id.iv_menu);
            ivSend = itemView.findViewById(R.id.iv_send);
            etAddAComment = itemView.findViewById(R.id.et_add_a_comment);
            tvCreatedBy = itemView.findViewById(R.id.tv_created_by);
        }
    }

    public interface IHomePostsAdapterCallBack {
        void onCommentClick(int position);

        void onMenuClick(int position, View view);

        void onSendClick(int position, String commentText);

        void onUserProfileClick(int position, int userId);

        void onLikeOrDislikeClick(int position, boolean isLike, boolean isUnLike);
    }
}
