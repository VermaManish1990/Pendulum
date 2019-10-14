package com.pend.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorDetailsActivity;
import com.pend.fragments.CommentsDialogFragment;
import com.pend.fragments.ContestVotingWith2OptionDialogFragment;
import com.pend.fragments.ContestVotingWith3OptionDialogFragment;
import com.pend.fragments.HomeCommentsDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.models.GetContestsResponseModel;
import com.pend.models.GetExitPollListResponseModel;
import com.pend.models.GetNewsFeedDataModel;
import com.pend.models.GetPostsResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.OtherUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.widget.progressbar.CustomProgressBar;
import com.pend.widget.progressbar.ProgressItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class HomePostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = HomePostsAdapter.class.getSimpleName();
    private ArrayList<GetNewsFeedDataModel.NewsFeedList> mPostsDetailsList;
    private Context mContext;
    private IHomePostsAdapterCallBack mIHomePostsAdapterCallBack;
    private boolean mIsVoted;



    private final int TYPE_1_CONTEST = 1;  //for 2 option
    private final int TYPE_2_CONTEST = 2;  //for 3 option

    private final int EXIT_POLL = 3;  //for 2 option
    private final int MIRROR_POST = 4;  //for 3 option

    class PostViewHolder extends RecyclerView.ViewHolder {

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


        public PostViewHolder(View itemView) {
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

    class Contest1ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private final ImageView ivLeftProfile;
        private final ImageView ivRightProfile;
        private final ImageView ivComment;

        private final ImageView tvShareOnFacebook;
        private final TextView tvTitle;
        private final TextView tvLeftName;
        private final TextView tvRightName;
        private final TextView tvCreatedBy;
        private final TextView tvCommentCount;
        private final CustomProgressBar progressBarProfile;
        private final View viewProgressBarProfile;

        public Contest1ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            viewProgressBarProfile = itemView.findViewById(R.id.view_progress_bar_profile);
            progressBarProfile = itemView.findViewById(R.id.progress_bar_profile);
            tvShareOnFacebook = itemView.findViewById(R.id.tv_share_on_facebook);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivLeftProfile = itemView.findViewById(R.id.iv_left_profile);
            ivRightProfile = itemView.findViewById(R.id.iv_right_profile);
            tvLeftName = itemView.findViewById(R.id.tv_left_name);
            tvRightName = itemView.findViewById(R.id.tv_right_name);
            tvCreatedBy = itemView.findViewById(R.id.tv_created_by);
            ivComment = itemView.findViewById(R.id.iv_comment);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
        }
    }

    public class Contest2ViewHolder extends RecyclerView.ViewHolder {
    private final View rootView;
    private final View llMirrorPercentageView;
    private final ImageView ivProfile;
    private final ImageView ivComment;

    private final TextView tvName;
    private final TextView tvShareOnFacebook;
    private final TextView tvTitle;
    private final TextView tvMirror1View;
    private final TextView tvMirror2View;
    private final TextView tvMirror3View;
    private final TextView tvMirror1Name;
    private final TextView tvMirror2Name;
    private final TextView tvMirror3Name;
    private final TextView tvCreatedBy;
    private final TextView tvCommentCount;
    private final View rlBottomView;

    public Contest2ViewHolder(View itemView) {
        super(itemView);

        rootView = itemView.findViewById(R.id.root_view);
        llMirrorPercentageView = itemView.findViewById(R.id.ll_right_view);
        rlBottomView = itemView.findViewById(R.id.rl_bottom_view);
        ivProfile = itemView.findViewById(R.id.iv_profile);
        tvName = itemView.findViewById(R.id.tv_name);
        tvShareOnFacebook = itemView.findViewById(R.id.tv_share_on_facebook);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvMirror1View = itemView.findViewById(R.id.tv_mirror1_view);
        tvMirror2View = itemView.findViewById(R.id.tv_mirror2_view);
        tvMirror3View = itemView.findViewById(R.id.tv_mirror3_view);
        tvMirror1Name = itemView.findViewById(R.id.tv_mirror1_name);
        tvMirror2Name = itemView.findViewById(R.id.tv_mirror2_name);
        tvMirror3Name = itemView.findViewById(R.id.tv_mirror3_name);
        tvCreatedBy = itemView.findViewById(R.id.tv_created_by);
        ivComment = itemView.findViewById(R.id.iv_comment);
        tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
    }
}

    class ExitPollViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        private final TextView tvShareOnFacebook;
        private final TextView tvAdmireView;
        private final TextView tvHateView;
        private final TextView tvCanTSayView;
        private final TextView tvTitle;
        private final View rlPollPerView;

        public ExitPollViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            rlPollPerView = itemView.findViewById(R.id.rl_poll_per_view);
            tvShareOnFacebook = itemView.findViewById(R.id.tv_share_on_facebook);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAdmireView = itemView.findViewById(R.id.tv_admire_view);
            tvHateView = itemView.findViewById(R.id.tv_hate_view);
            tvCanTSayView = itemView.findViewById(R.id.tv_can_t_say_view);
        }
    }

    public HomePostsAdapter(Context context, ArrayList<GetNewsFeedDataModel.NewsFeedList> postsDetailsList) {
        mContext = context;
        mIHomePostsAdapterCallBack = (IHomePostsAdapterCallBack) context;
        mPostsDetailsList = postsDetailsList;
    }

    public void setPostsDetailsList(ArrayList<GetNewsFeedDataModel.NewsFeedList> postsDetailsList) {
        this.mPostsDetailsList = postsDetailsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_1_CONTEST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contest_type_1_view, parent, false);
            return new HomePostsAdapter.Contest1ViewHolder(view);
        } else if(viewType == TYPE_2_CONTEST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contest_type_2_view, parent, false);
            return new HomePostsAdapter.Contest2ViewHolder(view);
        }
        else if(viewType == MIRROR_POST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_posts_item, parent, false);
            return new HomePostsAdapter.PostViewHolder(view);
        }
        else  {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exit_poll_item, parent, false);
            return new HomePostsAdapter.ExitPollViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HomePostsAdapter.Contest1ViewHolder) {

            HomePostsAdapter.Contest1ViewHolder viewHolder = (HomePostsAdapter.Contest1ViewHolder) holder;
            setDataForContestType1(viewHolder, position);

        }
        if (holder instanceof HomePostsAdapter.Contest2ViewHolder) {

            HomePostsAdapter.Contest2ViewHolder viewHolder = (HomePostsAdapter.Contest2ViewHolder) holder;
            setDataForContestType2(viewHolder, position);

        }

        if (holder instanceof HomePostsAdapter.PostViewHolder) {

            HomePostsAdapter.PostViewHolder viewHolder = (HomePostsAdapter.PostViewHolder) holder;
            setDataForMirror(viewHolder, position);

        }
        if (holder instanceof HomePostsAdapter.ExitPollViewHolder) {

            HomePostsAdapter.ExitPollViewHolder viewHolder = (HomePostsAdapter.ExitPollViewHolder) holder;
            setDataForExitPoll(viewHolder, position);

        }

    }

    @Override
    public int getItemCount() {
        return mPostsDetailsList != null ? mPostsDetailsList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        GetNewsFeedDataModel.NewsFeedList contestDetails = mPostsDetailsList.get(position);

        if (contestDetails.isExitPoll) {
            return EXIT_POLL;
        }
        else if(contestDetails.isPost)
        {
            return MIRROR_POST;
        }
       else if(contestDetails.isContest)
       {
           if(contestDetails.contestData.getContestTypeID()==TYPE_1_CONTEST)
               return TYPE_1_CONTEST;
          else
               return TYPE_2_CONTEST;

       }
       else
           return 0;
    }



    /**
     * Method is used to set data for type 1(With option 2)
     *
     * @param viewHolder viewHolder
     * @param position   position
     */
    private void setDataForContestType1(HomePostsAdapter.Contest1ViewHolder viewHolder, int position) {
        GetNewsFeedDataModel.NewsFeedList newsFeedList = mPostsDetailsList.get(position);

        GetNewsFeedDataModel.NewsFeedList.ContestData contestDetails=newsFeedList.contestData;

        if (contestDetails.option1MirrorImageURL != null && !contestDetails.option1MirrorImageURL.equals("")) {

            Picasso.with(mContext)
                    .load(contestDetails.option1MirrorImageURL)
                    .into(viewHolder.ivLeftProfile);
        }

        if (contestDetails.option2MirrorImageURL != null && !contestDetails.option2MirrorImageURL.equals("")) {

            Picasso.with(mContext)
                    .load(contestDetails.option2MirrorImageURL)
                    .into(viewHolder.ivRightProfile);
        }

        String createdBy = String.valueOf("Created by: <b>" + (contestDetails.createdUserName != null ? contestDetails.createdUserName : "") + "</b>");
        viewHolder.tvCreatedBy.setText(Html.fromHtml(createdBy));
        viewHolder.tvTitle.setText(contestDetails.questionText != null ? contestDetails.questionText : "");
        viewHolder.tvLeftName.setText(contestDetails.option1MirrorName != null ? contestDetails.option1MirrorName : "");
        viewHolder.tvRightName.setText(contestDetails.option2MirrorName != null ? contestDetails.option2MirrorName : "");

        viewHolder.progressBarProfile.getThumb().mutate().setAlpha(0);

        ArrayList<ProgressItem> progressItemList = new ArrayList<>();

        progressItemList.add(new ProgressItem(mContext.getResources().getColor(R.color.light_red_bg), contestDetails.option1Per));
        progressItemList.add(new ProgressItem(mContext.getResources().getColor(R.color.bootstrap_brand_warning), contestDetails.option2Per));

        viewHolder.progressBarProfile.initData(progressItemList);
        viewHolder.progressBarProfile.invalidate();

        mIsVoted = contestDetails.option1Vote || contestDetails.option2Vote || contestDetails.option3Vote;

        viewHolder.viewProgressBarProfile.setOnClickListener(v -> {

            int userId = -1;
            try {
                userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (userId == -1) {
                OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
            } else {
           /*     DialogFragment votingDialogFragment = ContestVotingWith2OptionDialogFragment.newInstance(mIsVoted, contestDetails);
                votingDialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "ContestVotingWith3OptionDialogFragment");
           */ }

        });

        viewHolder.tvCreatedBy.setOnClickListener(v -> onCreatedByUserClick(contestDetails.createdUserID));

        viewHolder.tvShareOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog((Activity) mContext);// initialize facebook shareDialog.

                if (ShareDialog.canShow(SharePhotoContent.class)) {

                    Bitmap bitmap = OtherUtil.loadBitmapFromView(viewHolder.rootView);
                    if (bitmap != null) {

                        try {

                            SharePhoto photo = new SharePhoto.Builder()
                                    .setBitmap(bitmap)
//                                    .setImageUrl(Uri.parse(postsDetails.imageURL))
                                    .setCaption(contestDetails.questionText)
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

    }

    /**
     * Method is used to set data for type 1(With option 3)
     *
     * @param viewHolder viewHolder
     * @param position   position
     */
    private void setDataForContestType2(final HomePostsAdapter.Contest2ViewHolder viewHolder, int position) {
        final GetNewsFeedDataModel.NewsFeedList newsFeedList = mPostsDetailsList.get(position);

         GetNewsFeedDataModel.NewsFeedList.ContestData contestDetails = newsFeedList.contestData;
        if (contestDetails.relatedMirrorImageURL != null && !contestDetails.relatedMirrorImageURL.equals("")) {

            Picasso.with(mContext)
                    .load(contestDetails.relatedMirrorImageURL)
                    .into(viewHolder.ivProfile);
        }

        if (contestDetails.relatedMirrorID != 0) {
            viewHolder.ivProfile.setOnClickListener(v -> {
                onMirrorClick(contestDetails.relatedMirrorID);
            });
            viewHolder.tvName.setOnClickListener(v -> {
               onMirrorClick(contestDetails.relatedMirrorID);
            });
        } else {
            viewHolder.ivProfile.setVisibility(View.GONE);
            viewHolder.tvName.setVisibility(View.GONE);
        }

        String createdBy = String.valueOf("Created by: <b>" + (contestDetails.createdUserName != null ? contestDetails.createdUserName : "") + "</b>");
        viewHolder.tvCreatedBy.setText(Html.fromHtml(createdBy));
        viewHolder.tvName.setText(contestDetails.relatedMirrorName != null ? contestDetails.relatedMirrorName : "");
        viewHolder.tvTitle.setText(contestDetails.questionText != null ? contestDetails.questionText : "");

        if (contestDetails.contestTypeID == 1) {

            viewHolder.tvMirror1Name.setText(contestDetails.option1Text != null ? contestDetails.option1Text : "");
            viewHolder.tvMirror2Name.setText(contestDetails.option2Text != null ? contestDetails.option2Text : "");
            viewHolder.tvMirror3Name.setText(contestDetails.option3Text != null ? contestDetails.option3Text : "I don't know");
        } else {
            viewHolder.tvMirror1Name.setText(contestDetails.option1MirrorName != null ? contestDetails.option1MirrorName : "");
            viewHolder.tvMirror2Name.setText(contestDetails.option2MirrorName != null ? contestDetails.option2MirrorName : "");
            viewHolder.tvMirror3Name.setText(contestDetails.option3MirrorName != null ? contestDetails.option3MirrorName : "");
        }


        mIsVoted = contestDetails.option1Vote || contestDetails.option2Vote || contestDetails.option3Vote;

        final int max = getMax(contestDetails.option1Per, contestDetails.option2Per, contestDetails.option3Per);

        viewHolder.llMirrorPercentageView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {

            int width = viewHolder.llMirrorPercentageView.getWidth();

            if (max > 0) {

                if (contestDetails.option1Per != 0) {

                    viewHolder.tvMirror1View.setWidth(contestDetails.option1Per * (width / max));
                    viewHolder.tvMirror1View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_sky_blue_view));
                } else {
                    viewHolder.tvMirror1View.setWidth(width / 4);
                    viewHolder.tvMirror1View.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    viewHolder.tvMirror1View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                }

                if (contestDetails.option2Per != 0) {

                    viewHolder.tvMirror2View.setWidth(contestDetails.option2Per * (width / max));
                    viewHolder.tvMirror2View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_pink_view));
                } else {
                    viewHolder.tvMirror2View.setWidth(width / 4);
                    viewHolder.tvMirror2View.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    viewHolder.tvMirror2View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                }

                if (contestDetails.option3Per != 0) {

                    viewHolder.tvMirror3View.setWidth(contestDetails.option3Per * (width / max));
                    viewHolder.tvMirror3View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_royal_blue_view));
                } else {
                    viewHolder.tvMirror3View.setWidth(width / 4);
                    viewHolder.tvMirror3View.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    viewHolder.tvMirror3View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                }
            } else {
                viewHolder.tvMirror1View.setWidth(width / 4);
                viewHolder.tvMirror1View.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                viewHolder.tvMirror1View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));

                viewHolder.tvMirror2View.setWidth(width / 4);
                viewHolder.tvMirror2View.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                viewHolder.tvMirror2View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));

                viewHolder.tvMirror3View.setWidth(width / 4);
                viewHolder.tvMirror3View.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                viewHolder.tvMirror3View.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));

            }

            viewHolder.tvMirror1View.setText(String.valueOf(contestDetails.option1Per + "%"));
            viewHolder.tvMirror2View.setText(String.valueOf(contestDetails.option2Per + "%"));
            viewHolder.tvMirror3View.setText(String.valueOf(contestDetails.option3Per + "%"));


        });

        viewHolder.tvCreatedBy.setOnClickListener(v -> onCreatedByUserClick(contestDetails.createdUserID));

        viewHolder.rlBottomView.setOnClickListener(v -> {

            int userId = -1;
            try {
                userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (userId == -1) {
                OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
            } else {
            /*   DialogFragment votingDialogFragment = ContestVotingWith3OptionDialogFragment.newInstance(mIsVoted, contestDetails);
                votingDialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "ContestVotingWith3OptionDialogFragment");
           */ }
        });

        viewHolder.tvShareOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog((Activity) mContext);// initialize facebook shareDialog.

                if (ShareDialog.canShow(SharePhotoContent.class)) {

                    Bitmap bitmap = OtherUtil.loadBitmapFromView(viewHolder.rootView);
                    if (bitmap != null) {

                        try {

                            SharePhoto photo = new SharePhoto.Builder()
                                    .setBitmap(bitmap)
//                                    .setImageUrl(Uri.parse(postsDetails.imageURL))
                                    .setCaption(contestDetails.questionText)
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

        if(contestDetails.getTypeTwoOptions()==2)
        {
            viewHolder.tvMirror3View.setVisibility(View.GONE);
            viewHolder.tvMirror3Name.setVisibility(View.GONE);
        }
        else {

            viewHolder.tvMirror3View.setVisibility(View.VISIBLE);
            viewHolder.tvMirror3Name.setVisibility(View.VISIBLE);

        }

    }

    private void setDataForMirror(final HomePostsAdapter.PostViewHolder holder, int position) {

        GetNewsFeedDataModel.NewsFeedList newsFeedList = mPostsDetailsList.get(position);
        GetNewsFeedDataModel.NewsFeedList.PostData postsDetails =newsFeedList.postData;

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }


        Picasso.with(mContext)
                .load(postsDetails.MirrorImageUrl)
//                .placeholder(R.drawable.ic_placeholder)   // optional
//                .error(R.drawable.ic_error_fallback)      // optional
//                .resize(250, 200)                        // optional
//                .rotate(90)                             // optional
                .into(holder.ivProfile);


        holder.tvComment.setText(String.valueOf(postsDetails.commentCount));
        holder.tvLike.setText(String.valueOf(postsDetails.likeCount));
        holder.tvDislike.setText(String.valueOf(postsDetails.unlikeCount));

        if (postsDetails.postInfo != null && postsDetails.postInfo.length() > 0) {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(postsDetails.postInfo);
        } else {
            holder.tvTitle.setVisibility(View.GONE);
        }

        holder.tvName.setText(postsDetails.userFullName != null ? postsDetails.mirrorName : "");
        holder.tvTime.setText(postsDetails.createdDatetime != null ? postsDetails.createdDatetime : "");
        holder.tvCreatedBy.setText(Html.fromHtml("Created by :" + postsDetails.userFullName + ""));

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
               // mIHomePostsAdapterCallBack.onUserProfileClick(position, postsDetails.userID);
                onMirrorClick(postsDetails.mirrorID);
            }
        });
        holder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIHomePostsAdapterCallBack.onUserProfileClick(position, postsDetails.userID);
            }
        });

      /*  holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommentIconClick(position);

            }});
*/

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

        int finalUserId = userId;
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

                    if (finalUserId == -1) {
                        OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                    } else
                        mIHomePostsAdapterCallBack.onSendClick(position, text);
                }
            }
        });

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
                if (finalUserId == -1) {
                    OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                } else
                    onCommentIconClick(position);
            }
        });

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalUserId == -1) {
                    OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                } else if (postsDetails.isLike) {
                    mIHomePostsAdapterCallBack.onLikeOrDislikeClick(position, false, false);
                } else {
                    mIHomePostsAdapterCallBack.onLikeOrDislikeClick(position, true, false);
                }
            }
        });
        holder.llDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalUserId == -1) {
                    OtherUtil.showAlertDialog(mContext.getString(R.string.guest_user_message), mContext, (dialog, which) -> dialog.dismiss());
                } else if (postsDetails.isUnLike) {
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
                    } else {
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


        holder.tvCreatedBy.setOnClickListener(v -> onCreatedByUserClick(postsDetails.userID));

    }

    private void setDataForExitPoll(final HomePostsAdapter.ExitPollViewHolder holder, int position)
    {
        final GetNewsFeedDataModel.NewsFeedList newsFeedList = mPostsDetailsList.get(position);
        GetNewsFeedDataModel.NewsFeedList.ExitPollData exitPollListDetails =newsFeedList.exitPollData;

        holder.tvTitle.setText(exitPollListDetails.exitPollText != null ? exitPollListDetails.exitPollText : "");

        final int max = getMax(exitPollListDetails.pollAdmirePer, exitPollListDetails.pollHatePer, exitPollListDetails.pollCantSayPer);
        holder.rlPollPerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = (int) (holder.rlPollPerView.getWidth() * (0.7));

                if (max > 0) {

                    if (exitPollListDetails.pollAdmirePer != 0) {

                        holder.tvAdmireView.setWidth(exitPollListDetails.pollAdmirePer * (width / max));
                        holder.tvAdmireView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_green_view));
                    } else {
                        holder.tvAdmireView.setWidth(width / 4);
                        holder.tvAdmireView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                        holder.tvAdmireView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                    }

                    if (exitPollListDetails.pollHatePer != 0) {

                        holder.tvHateView.setWidth(exitPollListDetails.pollHatePer * (width / max));
                        holder.tvHateView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_red_view));
                    } else {
                        holder.tvHateView.setWidth(width / 4);
                        holder.tvHateView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                        holder.tvHateView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                    }

                    if (exitPollListDetails.pollCantSayPer != 0) {

                        holder.tvCanTSayView.setWidth(exitPollListDetails.pollCantSayPer * (width / max));
                        holder.tvCanTSayView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_yellow_view));
                    } else {
                        holder.tvCanTSayView.setWidth(width / 4);
                        holder.tvCanTSayView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                        holder.tvCanTSayView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                    }
                } else {
                    holder.tvAdmireView.setWidth(width / 4);
                    holder.tvAdmireView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    holder.tvAdmireView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));

                    holder.tvHateView.setWidth(width / 4);
                    holder.tvHateView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    holder.tvHateView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));

                    holder.tvCanTSayView.setWidth(width / 4);
                    holder.tvCanTSayView.setTextColor(mContext.getResources().getColor(R.color.fontPrimary40));
                    holder.tvCanTSayView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_rounded_grey_border));
                }

                holder.tvAdmireView.setText(String.valueOf(exitPollListDetails.pollAdmirePer + "%"));
                holder.tvHateView.setText(String.valueOf(exitPollListDetails.pollHatePer + "%"));
                holder.tvCanTSayView.setText(String.valueOf(exitPollListDetails.pollCantSayPer + "%"));
            }
        });

        holder.tvShareOnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog((Activity) mContext);// initialize facebook shareDialog.

                if (ShareDialog.canShow(SharePhotoContent.class)) {

                    Bitmap bitmap = OtherUtil.loadBitmapFromView(holder.rootView);
                    if (bitmap != null) {

                        try {

                            SharePhoto photo = new SharePhoto.Builder()
                                    .setBitmap(bitmap)
//                                    .setImageUrl(Uri.parse(postsDetails.imageURL))
                                    .setCaption(exitPollListDetails.exitPollText)
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
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mIExitPollAdapterCallBack.onViewClick(position);
            }
        });
    }


    public interface IHomePostsAdapterCallBack {
        void onCommentClick(int position);

        void onMenuClick(int position, View view);

        void onSendClick(int position, String commentText);

        void onUserProfileClick(int position, int userId);

        void onLikeOrDislikeClick(int position, boolean isLike, boolean isUnLike);
    }

    /**
     * Method is used to get max percentage value.
     *
     * @param pollAdmirePer  pollAdmirePer
     * @param pollHatePer    pollHatePer
     * @param pollCantSayPer pollCantSayPer
     * @return int
     */
    private int getMax(int pollAdmirePer, int pollHatePer, int pollCantSayPer) {

        if (pollAdmirePer >= pollHatePer) {
            if (pollAdmirePer >= pollCantSayPer) {
                return pollAdmirePer;
            } else {
                return pollCantSayPer;
            }
        } else {
            if (pollHatePer >= pollCantSayPer) {
                return pollHatePer;
            } else {
                return pollCantSayPer;
            }
        }
    }

    /**
     * Method is used to go to Mirror details screen.
     *
     * @param relatedMirrorID relatedMirrorID
     */
    private void onMirrorClick(int relatedMirrorID) {
        Intent intent = new Intent(mContext, MirrorDetailsActivity.class);
        intent.putExtra(Constants.MIRROR_ID_KEY, relatedMirrorID);
        mContext.startActivity(intent);
    }

    /**
     * Method is used to go to user profile.
     *
     * @param createdUserID createdUserID
     */
    private void onCreatedByUserClick(int createdUserID) {
        try {
            Intent intent = new Intent(mContext, ProfileActivity.class);
            if (!(createdUserID == Integer.parseInt(SharedPrefUtils.getUserId(mContext)))) {

                intent.putExtra(Constants.USER_ID_KEY, createdUserID);
                intent.putExtra(Constants.IS_OTHER_PROFILE, true);
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onCommentIconClick(int position) {

//        Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();

        AppCompatActivity activity = (AppCompatActivity) mContext;
        HomeCommentsDialogFragment commentsDialogFragment = HomeCommentsDialogFragment.newInstance(mPostsDetailsList.get(position).postData);
        commentsDialogFragment.show(activity.getSupportFragmentManager(), "CommentsDialogFragment");
    }


}
