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
import com.pend.models.UserTimeSheetResponseModel;
import com.pend.util.LoggerUtil;

import java.util.ArrayList;

public class TimeSheetAdapter extends RecyclerView.Adapter<TimeSheetAdapter.ViewHolder> {

    private static final String TAG = TimeSheetAdapter.class.getSimpleName();
    private Context mContext;
    private ITimeSheetAdapterCallBack mITimeSheetAdapterCallBack;
    private ArrayList<UserTimeSheetResponseModel.UserTimeSheetDetails> mTimeSheetDetailsList;

    public TimeSheetAdapter(Context context, ArrayList<UserTimeSheetResponseModel.UserTimeSheetDetails> timeSheetDetailsList) {
        mTimeSheetDetailsList = timeSheetDetailsList;
        mContext = context;
        mITimeSheetAdapterCallBack = (ITimeSheetAdapterCallBack) context;
    }

    public void setTimeSheetDetailsList(ArrayList<UserTimeSheetResponseModel.UserTimeSheetDetails> timeSheetDetailsList) {
        this.mTimeSheetDetailsList = timeSheetDetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LoggerUtil.v(TAG, "onCreateViewHolder");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_time_sheet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        UserTimeSheetResponseModel.UserTimeSheetDetails timeSheetDetails = mTimeSheetDetailsList.get(position);

        if (timeSheetDetails.type != null) {

            switch (timeSheetDetails.type) {
                case "Post":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.setting));
                    holder.tvMessage.setText(String.valueOf("Added a new post on " + timeSheetDetails.mirrorName + "'s mirror."));

                    break;

                case "MirrorVote":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.checkbox));
                    holder.tvMessage.setText(String.valueOf("Voted " + timeSheetDetails.vote + " for " + timeSheetDetails.mirrorName + "."));

                    break;

                case "UnLike":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dislike_red));
                    holder.tvMessage.setText(String.valueOf("Disliked a post on " + timeSheetDetails.mirrorName + "'s mirror."));

                    break;

                case "Like":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like_green));
                    holder.tvMessage.setText(String.valueOf("Liked a post on " + timeSheetDetails.mirrorName + "'s mirror."));

                    break;

                case "Comment":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message));
                    holder.tvMessage.setText(String.valueOf("Commented on a post on " + timeSheetDetails.mirrorName + "'s mirror."));

                    break;

                default:
                    break;
            }
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITimeSheetAdapterCallBack.onLogDetailsClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeSheetDetailsList != null ? mTimeSheetDetailsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;
        private final ImageView ivIcon;
        private final View rootView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_message);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

    public interface ITimeSheetAdapterCallBack{
        void onLogDetailsClick(int position);
    }

}
