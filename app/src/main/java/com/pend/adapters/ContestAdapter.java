package com.pend.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.pend.fragments.ContestVotingWith2OptionDialogFragment;
import com.pend.fragments.ContestVotingWith3OptionDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.models.GetContestsResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.OtherUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.widget.progressbar.CustomProgressBar;
import com.pend.widget.progressbar.ProgressItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_1_CONTEST = 1;  //for 2 option
    private final int TYPE_2_CONTEST = 2;  //for 3 option

    private static final String TAG = ContestAdapter.class.getSimpleName();
    private ArrayList<GetContestsResponseModel.GetContestDetails> mContestDataList;
    public Context mContext;
    private boolean mIsVoted;

    public ContestAdapter(Context context, ArrayList<GetContestsResponseModel.GetContestDetails> contestDataList) {
        mContext = context;
        mContestDataList = contestDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");

        if (viewType == TYPE_1_CONTEST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contest_type_1_view, parent, false);
            return new ViewHolderType1(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contest_type_2_view, parent, false);
            return new ViewHolderType2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderType1) {

            ViewHolderType1 viewHolder = (ViewHolderType1) holder;
            setDataForContestType1(viewHolder, position);

        } else if (holder instanceof ViewHolderType2) {

            ViewHolderType2 viewHolder = (ViewHolderType2) holder;
            setDataForContestType2(viewHolder, position);

        }
    }

    @Override
    public int getItemViewType(int position) {
        GetContestsResponseModel.GetContestDetails contestDetails = mContestDataList.get(position);

        if (contestDetails.contestType == TYPE_1_CONTEST) {
            return TYPE_1_CONTEST;
        } else {
            return TYPE_2_CONTEST;
        }
    }

    @Override
    public int getItemCount() {
        return mContestDataList != null ? mContestDataList.size() : 0;
    }

    public class ViewHolderType1 extends RecyclerView.ViewHolder {

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

        public ViewHolderType1(View itemView) {
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

    public class ViewHolderType2 extends RecyclerView.ViewHolder {
        private final View rootView;
        private final View llMirrorPercentageView;
        private final ImageView ivProfile;
        private final ImageView ivComment;

        private final TextView tvName;
        private final ImageView tvShareOnFacebook;
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

        public ViewHolderType2(View itemView) {
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

    /**
     * Method is used to set data for type 1(With option 2)
     *
     * @param viewHolder viewHolder
     * @param position   position
     */
    private void setDataForContestType1(ViewHolderType1 viewHolder, int position) {
        GetContestsResponseModel.GetContestDetails contestDetails = mContestDataList.get(position);

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
                DialogFragment votingDialogFragment = ContestVotingWith2OptionDialogFragment.newInstance(mIsVoted, contestDetails);
                votingDialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "ContestVotingWith3OptionDialogFragment");
            }

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
    private void setDataForContestType2(final ViewHolderType2 viewHolder, int position) {
        final GetContestsResponseModel.GetContestDetails contestDetails = mContestDataList.get(position);

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
                DialogFragment votingDialogFragment = ContestVotingWith3OptionDialogFragment.newInstance(mIsVoted, contestDetails);
                votingDialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "ContestVotingWith3OptionDialogFragment");
            }
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

        if(contestDetails.typeTwoOptions==2||contestDetails.TypeOneOptions==2)
        {
            viewHolder.tvMirror3View.setVisibility(View.GONE);
            viewHolder.tvMirror3Name.setVisibility(View.GONE);
        }
        else {
             {
                viewHolder.tvMirror3View.setVisibility(View.VISIBLE);
                viewHolder.tvMirror3Name.setVisibility(View.VISIBLE);
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
     * Method is used to get max percentage value.
     *
     * @param mirror1Per mirror1Per
     * @param mirror2Per mirror2Per
     * @param mirror3Per mirror3Per
     * @return int
     */
    private int getMax(int mirror1Per, int mirror2Per, int mirror3Per) {

        if (mirror1Per >= mirror2Per) {
            if (mirror1Per >= mirror3Per) {
                return mirror1Per;
            } else {
                return mirror3Per;
            }
        } else {
            if (mirror2Per >= mirror3Per) {
                return mirror2Per;
            } else {
                return mirror3Per;
            }
        }
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
}
