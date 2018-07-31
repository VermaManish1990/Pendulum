package com.pend.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

            String sourceString = null;
            switch (timeSheetDetails.type) {
                case "Post":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.setting));
                    sourceString = "Added a new <b>post</b> on <b>" + timeSheetDetails.mirrorName + "'s"+ "</b> mirror.";
                    holder.tvMessage.setText(Html.fromHtml(sourceString));

                    break;

                case "MirrorVote":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.checkbox));
                    sourceString = "Voted <b>" + timeSheetDetails.vote + "</b> for <b>" + timeSheetDetails.mirrorName + "</b>.";
                    holder.tvMessage.setText(Html.fromHtml(sourceString));

                    break;

                case "UnLike":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dislike_red));
                    sourceString = "Disliked a <b>post</b> on <b>" + timeSheetDetails.mirrorName + "'s</b> mirror.";
                    holder.tvMessage.setText(Html.fromHtml(sourceString));

                    break;

                case "Like":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like_green));
                    sourceString = "Liked a <b>post</b> on <b>" + timeSheetDetails.mirrorName + "'s</b> mirror.";
                    holder.tvMessage.setText(Html.fromHtml(sourceString));

                    break;

                case "Comment":
                    holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message));
                    sourceString ="Commented on a <b>post</b> on <b>" + timeSheetDetails.mirrorName + "'s</b> mirror.";
                    holder.tvMessage.setText(Html.fromHtml(sourceString));

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
