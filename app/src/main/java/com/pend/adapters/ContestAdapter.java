package com.pend.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.R;
import com.pend.models.ContestResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.widget.progressbar.CustomProgressBar;
import com.pend.widget.progressbar.ProgressItem;

import java.util.ArrayList;

public class ContestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_1_CONTEST = 1;
    private final int TYPE_2_CONTEST = 2;

    private static final String TAG = ContestAdapter.class.getSimpleName();
    private ArrayList<ContestResponseModel.ContestDetails> mContestDataList;
    public Context mContext;

    public ContestAdapter(Context context, ArrayList<ContestResponseModel.ContestDetails> contestDataList) {
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
        ContestResponseModel.ContestDetails contestDetails = mContestDataList.get(position);

        if (contestDetails.type == TYPE_1_CONTEST) {
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

        private final ImageView ivLeftProfile;
        private final ImageView ivRightProfile;
        private final ImageView ivComment;

        private final TextView tvShareOnFacebook;
        private final TextView tvTitle;
        private final TextView tvLeftName;
        private final TextView tvRightName;
        private final TextView tvCreatedBy;
        private final TextView tvCommentCount;
        private final CustomProgressBar progressBarProfile;

        public ViewHolderType1(View itemView) {
            super(itemView);

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
        private final View llMirrorPercentageView;
        private final ImageView ivProfile;
        private final ImageView ivComment;

        private final TextView tvName;
        private final TextView tvShareOnFacebook;
        private final TextView tvTitle;
        private final TextView tvMirror1View;
        private final TextView tvMirror2View;
        private final TextView tvMirror3View;
        private final TextView tvCreatedBy;
        private final TextView tvCommentCount;

        public ViewHolderType2(View itemView) {
            super(itemView);

            llMirrorPercentageView = itemView.findViewById(R.id.ll_right_view);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvShareOnFacebook = itemView.findViewById(R.id.tv_share_on_facebook);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMirror1View = itemView.findViewById(R.id.tv_mirror1_view);
            tvMirror2View = itemView.findViewById(R.id.tv_mirror2_view);
            tvMirror3View = itemView.findViewById(R.id.tv_mirror3_view);
            tvCreatedBy = itemView.findViewById(R.id.tv_created_by);
            ivComment = itemView.findViewById(R.id.iv_comment);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
        }
    }

    private void setDataForContestType1(ViewHolderType1 viewHolder, int position) {
        ContestResponseModel.ContestDetails contestDetails = mContestDataList.get(position);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewHolder.progressBarProfile.getThumb().mutate().setAlpha(0);
        }

        ArrayList<ProgressItem> progressItemList = new ArrayList<>();

        progressItemList.add(new ProgressItem(mContext.getResources().getColor(R.color.light_red_bg), contestDetails.mirror1Per));
        progressItemList.add(new ProgressItem(mContext.getResources().getColor(R.color.bootstrap_brand_warning), contestDetails.mirror2Per));

        viewHolder.progressBarProfile.initData(progressItemList);
        viewHolder.progressBarProfile.invalidate();
    }

    private void setDataForContestType2(final ViewHolderType2 viewHolder, int position) {
        final ContestResponseModel.ContestDetails contestDetails = mContestDataList.get(position);

        final int max = getMax(contestDetails.mirror1Per, contestDetails.mirror2Per, contestDetails.mirror3Per);

        viewHolder.llMirrorPercentageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = viewHolder.llMirrorPercentageView.getWidth();
                viewHolder.tvMirror1View.setWidth(contestDetails.mirror1Per * (width / max));
                viewHolder.tvMirror2View.setWidth(contestDetails.mirror2Per * (width / max));
                viewHolder.tvMirror3View.setWidth(contestDetails.mirror3Per * (width / max));

                viewHolder.tvMirror1View.setText(String.valueOf(contestDetails.mirror1Per + "%"));
                viewHolder.tvMirror2View.setText(String.valueOf(contestDetails.mirror2Per + "%"));
                viewHolder.tvMirror3View.setText(String.valueOf(contestDetails.mirror3Per + "%"));

            }
        });
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
}
